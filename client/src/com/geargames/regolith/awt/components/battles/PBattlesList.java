package com.geargames.regolith.awt.components.battles;


import com.geargames.awt.components.PVerticalScrollView;
import com.geargames.awt.timers.TimerManager;
import com.geargames.common.Graphics;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.network.Network;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Список текущих боёв.
 */
public class PBattlesList extends PVerticalScrollView {
    private PBattleListItemVector items;
    private Network network;

    public PBattlesList(PObject listPrototype) {
        super(listPrototype);
        IndexObject index = (IndexObject) listPrototype.getIndexBySlot(0);
        items = new PBattleListItemVector((PObject)index.getPrototype(), getShownItemsAmount());
        network = ClientConfigurationFactory.getConfiguration().getNetwork();
        TimerManager.setPeriodicTimer(1000, this);
    }

    public void onTimer(int timerId) {
    }

    public void initiate(Graphics graphics) {
        setInitiated(true);
    }

    public Vector getItems() {
        return items;
    }

    public boolean isVisible() {
        return true;
    }
}
