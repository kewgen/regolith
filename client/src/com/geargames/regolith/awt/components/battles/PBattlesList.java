package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PVerticalScrollView;
import com.geargames.common.logging.Debug;
import com.geargames.common.network.*;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.Graphics;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.Packets;
import com.geargames.regolith.application.ObjectManager;
import com.geargames.regolith.serializers.answers.ClientBrowseBattlesAnswer;
import com.geargames.regolith.serializers.answers.ClientListenToBattleAnswer;
import com.geargames.regolith.units.dictionaries.ClientBattleCollection;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Список текущих боёв.
 */
public class PBattlesList extends PVerticalScrollView implements DataMessageListener {
    private PBattleListItemVector items;
    private short[] types;

    public PBattlesList(PObject listPrototype) {
        super(listPrototype);
        IndexObject index = (IndexObject) listPrototype.getIndexBySlot(0);
        items = new PBattleListItemVector((PObject) index.getPrototype(), getShownItemsAmount());
        types = new short[]{Packets.BROWSE_CREATED_BATTLES};
    }

    public int getInterval() {
        return 1000;
    }

    public short[] getTypes() {
        return types;
    }

    @Override
    public void onReceive(ClientDeSerializedMessage message, short type) {
        try {
            switch (type) {
                case Packets.BROWSE_CREATED_BATTLES:
                    ClientBattleCollection newBattles = ((ClientBrowseBattlesAnswer) message).getBattles();
                    int size = newBattles.size();
                    ClientBattleCollection oldBattles = ObjectManager.getInstance().getBattleCollection();
                    for (int i = 0; i < size; i++) {
                        oldBattles.add(newBattles.get(i));
                    }
                    break;
                default:
                    Debug.error("There is a message of type " + type);
            }
        } catch (Exception e) {
            Debug.error("Could not deserialize a message of type " + type, e);
        }
    }

    public void initiate(Graphics graphics) {
        setInitiated(true);
    }

    public Vector getItems() {
        return items;
    }

}
