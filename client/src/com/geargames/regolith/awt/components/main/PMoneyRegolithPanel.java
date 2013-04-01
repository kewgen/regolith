package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PLabel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PFrame;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.Region;
import com.geargames.regolith.awt.components.PRootContentPanel;

/**
 * User: mikhail v. kutuzov
 * Date: 11.01.13
 * Time: 11:39
 */
public class PMoneyRegolithPanel extends PRootContentPanel {
    private PLabel money;
    private PLabel regolith;

    public PMoneyRegolithPanel(PObject prototype) {
        super(prototype);
    }

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 1:
                PObject labelObject = (PObject) index.getPrototype();
                index = (IndexObject) labelObject.getIndexBySlot(110);
                PFrame frame = (PFrame) index.getPrototype();
                Region region = new Region();
                region.setHeight(frame.getHeight());
                region.setWidth(frame.getWidth());
                region.setMinX(index.getX());
                region.setMinY(index.getY());
                money = new PShowingModalLabel(index, region);
                addActiveChild(money, index);
                break;
            case 2:
                labelObject = (PObject) index.getPrototype();
                index = (IndexObject) labelObject.getIndexBySlot(110);
                frame = (PFrame) index.getPrototype();
                region = new Region();
                region.setHeight(frame.getHeight());
                region.setWidth(frame.getWidth());
                region.setMinX(index.getX());
                region.setMinY(index.getY());
                regolith = new PShowingModalLabel(index, region);
                addActiveChild(regolith, index);
                break;
        }
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

}
