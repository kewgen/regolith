package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PVerticalScrollView;
import com.geargames.common.logging.Debug;
import com.geargames.common.network.*;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.Graphics;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.Packets;

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

    @Override
    public int getInterval() {
        return 1000;
    }

    @Override
    public short[] getTypes() {
        return types;
    }

    @Override
    public void onReceive(ClientDeSerializedMessage message, short type) {
        try {
            Debug.debug("PBattlesList.onReceive(): type = " + type);
            switch (type) {
                case Packets.BROWSE_CREATED_BATTLES:
                    for (int i = 0; i < items.size(); i++) {
                        ((PBattleListItem) items.elementAt(i)).update();
                    }
                    break;
                default:
                    Debug.error("There is a message of type = " + type);
            }
        } catch (Exception e) {
            Debug.error("Could not deserialize a message of type = " + type, e);
        }
    }

    @Override
    public void initiate(Graphics graphics) {
        setInitiated(true);
    }

    @Override
    public Vector getItems() {
        return items;
    }

}
