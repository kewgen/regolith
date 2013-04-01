package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.HorizontalScrollView;
import com.geargames.awt.components.PPrototypeElement;
import com.geargames.awt.utils.ScrollHelper;
import com.geargames.awt.utils.motions.InertMotionListener;
import com.geargames.common.Graphics;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PFrame;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.Region;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Горизонтальный список из бойцов учётной записи.
 */
public class PHorizontalAccountWarriors extends HorizontalScrollView {
    private PPrototypeElement prototype;
    private Region region;
    private PHorizontalAccountWarriorItemVector items;

    private InertMotionListener motionListener;

    public PHorizontalAccountWarriors(PObject listPrototype) {

        Index index0 = listPrototype.getIndexBySlot(0);
        Index index1 = listPrototype.getIndexBySlot(1);
        setMargin(index1.getX() - index0.getX());

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

        items = new PHorizontalAccountWarriorItemVector((PObject)index0.getPrototype(), getShownItemsAmount());
        motionListener = new InertMotionListener();
    }

    public void initiateMotionListener(){
        setMotionListener(ScrollHelper.adjustHorizontalInertMotionListener(
                motionListener, region, getShownItemsAmount(), getItemSize()));
    }

    public void initiate(Graphics graphics) {
        initiateMotionListener();
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

    @Override
    public int getItemOffsetY() {
        return 0;
    }
}
