package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PLabel;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.NullRegion;
import com.geargames.common.util.Region;
import com.geargames.regolith.awt.components.PRootContentPanel;

/**
 * Панель "заголовок" висит посредине верхней стороны экрана.
 */
public class PHeadlinePanel extends PRootContentPanel {
    private PLabel label;

    public PHeadlinePanel(PObject prototype) {
        super(prototype);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                createDefaultElementByIndex(index, prototype);
                break;
            case 109:
                label = new PSimpleLabel(index);
//                label.setFont(PFontCollection.getFontTitle());
                addPassiveChild(label, index);
                break;
        }
    }

    @Override
    public Region getDrawRegion() {
        return NullRegion.instance;
    }

    @Override
    public Region getTouchRegion() {
        return NullRegion.instance;
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

}
