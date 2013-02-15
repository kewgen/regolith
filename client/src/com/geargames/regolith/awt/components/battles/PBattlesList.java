package com.geargames.regolith.awt.components.battles;


import com.geargames.awt.components.PPrototypeElement;
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
public class PBattlesList extends VerticalScrollView {
    private PPrototypeElement prototype;
    private Region region;
    private PBattleListItemVector items;

    public PBattlesList(PObject listPrototype) {

        IndexObject index0 = (IndexObject) listPrototype.getIndexBySlot(0);
        IndexObject index1 = (IndexObject) listPrototype.getIndexBySlot(1);
        setMargin(index1.getY() - index0.getY());

        prototype = new PPrototypeElement();
        prototype.setPrototype(index0.getPrototype());

        region = new Region();
        IndexObject index = (IndexObject) listPrototype.getIndexBySlot(110);
        region.setMinX(index.getX());
        region.setMinY(index.getY());
        PFrame frame = (PFrame)index.getPrototype();
        region.setWidth(frame.getWidth());
        region.setHeight(frame.getHeight());

        Region tmp = new Region();
        PObject objectPrototype = (PObject)index0.getPrototype();
        index = (IndexObject) objectPrototype.getIndexBySlot(110);
        prototype.setRegion(tmp);
        tmp.setMinX(index.getX());
        tmp.setMinY(index.getY());
        frame = (PFrame)index.getPrototype();
        tmp.setWidth(frame.getWidth());
        tmp.setHeight(frame.getHeight());

        items = new PBattleListItemVector((PObject)index0.getPrototype(), getShownItemsAmount());
    }

    public void initiate(Graphics graphics) {
        setInitiated(true);
    }

    public PPrototypeElement getPrototype() {
        return prototype;
    }

    public Vector getItems() {
        return items;
    }

    public Region getDrawRegion() {
        return region;
    }

    public Region getTouchRegion() {
        return region;
    }

    public boolean isVisible() {
        return true;
    }
}
