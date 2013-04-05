package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PVerticalScrollView;
import com.geargames.common.logging.Debug;
import com.geargames.common.network.*;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.Graphics;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.serializers.answers.ClientCompleteGroupAnswer;
import com.geargames.regolith.serializers.answers.ClientEvictAccountFromAllianceAnswer;
import com.geargames.regolith.serializers.answers.ClientJoinToBattleAllianceAnswer;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov, abarakov
 * Список текущих боёв.
 */
public class PBattlesList extends PVerticalScrollView implements DataMessageListener {
    private PBattleListItemVector items;
    private short[] types;

    public PBattlesList(PObject listPrototype) {
        super(listPrototype);
        IndexObject index = (IndexObject) listPrototype.getIndexBySlot(0);
        items = new PBattleListItemVector((PObject) index.getPrototype(), getShownItemsAmount());
        types = new short[] {
                Packets.BROWSE_CREATED_BATTLES,
                Packets.JOIN_TO_BATTLE_ALLIANCE,
                Packets.EVICT_ACCOUNT_FROM_ALLIANCE,
                Packets.GROUP_COMPLETE,
                Packets.GROUP_DISBAND
        };
    }

    @Override
    public int getInterval() {
        return 1000;
    }

    @Override
    public short[] getTypes() {
        return types;
    }

    @Override
    public void initiate(Graphics graphics) {
        setInitiated(true);
    }

    @Override
    public Vector getItems() {
        return items;
    }

//    public Battle getListenedBattle() {
//        return items.getListenedBattle();
//    }
//
//    public void setListenedBattle(Battle battle) {
//        items.setListenedBattle(battle);
//    }

    public void updateList() {
        items.update();
    }

    private void updateButtonAccount(BattleGroup battleGroup) {
        Battle listenedBattle = ClientConfigurationFactory.getConfiguration().getBattle();
        if (listenedBattle != null && battleGroup.getAlliance().getBattle().getId() == listenedBattle.getId()) {
            ((PBattleListItem) items.elementAt(0)).resetButtonAccount(battleGroup);
        }
    }

    @Override
    public void onReceive(ClientDeSerializedMessage message, short type) {
        try {
//            Debug.debug("PBattlesList.onReceive(): type = " + type);
            switch (type) {
                case Packets.BROWSE_CREATED_BATTLES: {
                    Debug.debug("PBattlesList.onReceive(type = " + type + "): BROWSE_CREATED_BATTLES");
                    updateList();
                    break;
                }
                case Packets.GROUP_COMPLETE: {
                    Debug.debug("PBattlesList.onReceive(type = " + type + "): GROUP_COMPLETE");
                    ClientCompleteGroupAnswer completeGroupAnswer = (ClientCompleteGroupAnswer) message;
                    updateButtonAccount(completeGroupAnswer.getBattleGroup());
                    break;
                }
                case Packets.GROUP_DISBAND: {
                    Debug.debug("PBattlesList.onReceive(type = " + type + "): GROUP_DISBAND");
                    ClientCompleteGroupAnswer completeGroupAnswer = (ClientCompleteGroupAnswer) message;
                    updateButtonAccount(completeGroupAnswer.getBattleGroup());
                    break;
                }
                case Packets.JOIN_TO_BATTLE_ALLIANCE: {
                    Debug.debug("PBattlesList.onReceive(type = " + type + "): JOIN_TO_BATTLE_ALLIANCE");
                    ClientJoinToBattleAllianceAnswer joinToBattleAllianceAnswer = (ClientJoinToBattleAllianceAnswer) message;
                    updateButtonAccount(joinToBattleAllianceAnswer.getBattleGroup());
                    break;
                }
                //todo: Если меня выкинули из битвы, нужно почистить переменную items.listenedBattle. Учесть, что я сам себя мог выкинуть из битвы, потому сам уже почистил и присвоил новые значения этим переменным.
                case Packets.EVICT_ACCOUNT_FROM_ALLIANCE: {
                    Debug.debug("PBattlesList.onReceive(type = " + type + "): EVICT_ACCOUNT_FROM_ALLIANCE");
                    ClientEvictAccountFromAllianceAnswer evictAccountFromAllianceAnswer = (ClientEvictAccountFromAllianceAnswer) message;
                    BattleGroup battleGroup = ClientBattleHelper.tryFindBattleGroupByAccountId(
                            evictAccountFromAllianceAnswer.getAlliance().getBattle(), evictAccountFromAllianceAnswer.getAccount().getId());
                    updateButtonAccount(battleGroup);
                    break;
                }
                default:
                    Debug.error("There is a message of type = " + type);
            }
        } catch (Exception e) {
            Debug.error("Exception while processing a message of type = " + type, e);
        }
    }

    public void registerMessageListener() {
        Debug.debug("PBattlesList.registerMessageListener");
        ClientConfigurationFactory.getConfiguration().getMessageDispatcher().register(this);
    }

    public void unregisterMessageListener() {
        Debug.debug("PBattlesList.unregisterMessageListener");
        ClientConfigurationFactory.getConfiguration().getMessageDispatcher().unregister(this);
    }

}
