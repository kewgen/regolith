package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PVerticalScrollView;
import com.geargames.common.logging.Debug;
import com.geargames.common.network.DataMessage;
import com.geargames.common.network.DataMessageListener;
import com.geargames.common.network.MessageDispatcher;
import com.geargames.common.network.Network;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.timers.TimerListener;
import com.geargames.common.timers.TimerManager;
import com.geargames.common.Graphics;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.regolith.application.ObjectManager;
import com.geargames.regolith.serializers.answers.ClientBrowseBattlesAnswer;
import com.geargames.regolith.serializers.answers.ClientEvictAccountFromAllianceAnswer;
import com.geargames.regolith.serializers.answers.ClientJoinToBattleAllianceAnswer;
import com.geargames.regolith.serializers.answers.ClientListenToBattleAnswer;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.dictionaries.BattleCollection;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Список текущих боёв.
 */
public class PBattlesList extends PVerticalScrollView implements DataMessageListener {
    private ClientBrowseBattlesAnswer browseBattlesAnswer;
    private PBattleListItemVector items;
    private MicroByteBuffer buffer;
    private short[] types;

    public PBattlesList(PObject listPrototype) {
        super(listPrototype);
        IndexObject index = (IndexObject) listPrototype.getIndexBySlot(0);
        items = new PBattleListItemVector((PObject) index.getPrototype(), getShownItemsAmount());
        browseBattlesAnswer = new ClientBrowseBattlesAnswer();
        types = new short[]{Packets.BROWSE_CREATED_BATTLES, Packets.LISTEN_TO_BROWSED_CREATED_BATTLES, Packets.DO_NOT_LISTEN_TO_BROWSED_CREATED_BATTLES};
        buffer = new MicroByteBuffer();
        browseBattlesAnswer.setBuffer(buffer);
    }

    public int getInterval() {
        return 1000;
    }

    public short[] getTypes() {
        return types;
    }

    @Override
    public void onReceive(DataMessage dataMessage) {
        try {
            switch (dataMessage.getMessageType()) {
                case Packets.BROWSE_CREATED_BATTLES:
                    buffer.initiate(dataMessage.getData(), dataMessage.getLength());
                    browseBattlesAnswer.deSerialize();
                    ArrayList answers = browseBattlesAnswer.getAnswers();
                    int size = answers.size();
                    BattleCollection battles = ObjectManager.getInstance().getBattleCollection();
                    //battles.
                    for (int i = 0; i < size; i++ ){



                        ((ClientListenToBattleAnswer)answers.get(i)).getBattle();

                    }
                    break;
                case Packets.LISTEN_TO_BROWSED_CREATED_BATTLES:
                    break;
                case Packets.DO_NOT_LISTEN_TO_BROWSED_CREATED_BATTLES:
                    break;
                default:
                    Debug.error("There is a message of type " + dataMessage.getMessageType());

            }
        } catch (Exception e) {
            Debug.error("Could not deserialize a message of type " + dataMessage.getMessageType(), e);
        }
    }

    public void initiate(Graphics graphics) {
        setInitiated(true);
    }

    public Vector getItems() {
        return items;
    }

}
