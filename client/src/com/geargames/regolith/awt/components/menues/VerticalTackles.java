package com.geargames.regolith.awt.components.menues;

import com.geargames.awt.components.PPrototypeElement;
import com.geargames.awt.components.VerticalScrollView;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.PFrame;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.Region;

/**
 * User: mikhail v. kutuzov
 * Предок всех вертикальных списков клади и расходников.
 */
public abstract class VerticalTackles extends VerticalScrollView {
    private PPrototypeElement prototype;
    private Region region;

    public Region getDrawRegion() {
        return region;
    }

    public Region getTouchRegion() {
        return region;
    }

    public PPrototypeElement getPrototype() {
        return prototype;
    }

    public abstract void initiateMotionListener();

    public VerticalTackles(PObject object) {
        setMargin(40);
        setStuck(false);
        setStrictlyClipped(false);

        Index index = object.getIndexBySlot(0);
        prototype = new PPrototypeElement();
        prototype.setPrototype(index.getPrototype());
        index = ((PObject) prototype.getPrototype()).getIndexBySlot(110);
        PFrame frame = (PFrame) index.getPrototype();
        Region elementRegion = new Region();
        elementRegion.setMinX(index.getX());
        elementRegion.setMinY(index.getY());
        elementRegion.setHeight(frame.getHeight());
        elementRegion.setWidth(frame.getWidth());
        prototype.setRegion(elementRegion);

        index = object.getIndexBySlot(110);
        frame = (PFrame) object.getIndexBySlot(110).getPrototype();
        region = new Region();
        region.setMinX(index.getX());
        region.setMinY(index.getY());
        region.setHeight(frame.getHeight());
        region.setWidth(frame.getWidth());
    }
}
