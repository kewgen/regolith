package com.geargames.regolith.awt.components.battles;


import com.geargames.awt.components.PPrototypeElement;
import com.geargames.awt.components.PVerticalScrollView;
import com.geargames.awt.components.VerticalScrollView;
import com.geargames.common.Graphics;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PFrame;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.Region;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Список текущих боёв.
 */
public class PBattlesList extends PVerticalScrollView {
    private PBattleListItemVector items;

    public PBattlesList(PObject listPrototype) {
        super(listPrototype);
        IndexObject index = (IndexObject) listPrototype.getIndexBySlot(0);
        items = new PBattleListItemVector((PObject)index.getPrototype(), getShownItemsAmount());
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
