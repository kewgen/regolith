package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PLabel;
import com.geargames.common.Graphics;
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

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
    }

    public void draw(Graphics graphics, int x, int y) {

    }

    public Region getDrawRegion() {
        return NullRegion.instance;
    }

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
