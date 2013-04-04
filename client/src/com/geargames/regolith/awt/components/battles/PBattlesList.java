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
import com.geargames.regolith.units.battle.Battle;

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
                Packets.BROWSE_CREATED_BATTLES
//                Packets.JOIN_TO_BATTLE_ALLIANCE,
//                Packets.EVICT_ACCOUNT_FROM_ALLIANCE
//                Packets.,
//                Packets.,
//                Packets.
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

    public Battle getListenedBattle() {
        return items.getListenedBattle();
    }

    public void setListenedBattle(Battle battle) {
        items.setListenedBattle(battle);
    }

    @Override
    public void onReceive(ClientDeSerializedMessage message, short type) {
        try {
            Debug.debug("PBattlesList.onReceive(): type = " + type);
            switch (type) {
                case Packets.BROWSE_CREATED_BATTLES:
                    items.update();
//                    for (int i = 0; i < items.size(); i++) {
//                        ((PBattleListItem) items.elementAt(i)).update();
//                    }
                    break;
//                case Packets.JOIN_TO_BATTLE_ALLIANCE:
//                    ClientJoinToBattleAllianceAnswer joinToBattleAllianceAnswer = (ClientJoinToBattleAllianceAnswer) message;
//                    BattleGroup battleGroup = joinToBattleAllianceAnswer.getBattleGroup();
//                    if (items.getListenedBattle() != null) {
//                        ((PBattleListItem) items.elementAt(0)).resetButtonAccount(
//                                battleGroup.getAlliance().getNumber(),
//                                battleGroup.getAlliance().getAllies().indexById(battleGroup.getId()));
//                    }
//                    break;
//                case Packets.EVICT_ACCOUNT_FROM_ALLIANCE:
//                    ClientEvictAccountFromAllianceAnswer evictAccountFromAllianceAnswer = (ClientEvictAccountFromAllianceAnswer) message;
//                    if (items.getListenedBattle() != null) {
//                        ((PBattleListItem) items.elementAt(0)).resetButtonAccount(
//                        );
//                        evictAccountFromAllianceAnswer.getAlliance();
//                    }
//                    break;
                default:
                    Debug.error("There is a message of type = " + type);
            }
        } catch (Exception e) {
            Debug.error("Exception while processing a message of type = " + type, e);
        }
    }

    public void registerMessageListener() {
        ClientConfigurationFactory.getConfiguration().getMessageDispatcher().register(this);
    }

    public void unregisterMessageListener() {
        ClientConfigurationFactory.getConfiguration().getMessageDispatcher().unregister(this);
    }

}
