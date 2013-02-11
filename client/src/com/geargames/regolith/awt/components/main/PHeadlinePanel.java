package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PContentPanel;
import com.geargames.awt.components.PLabel;
import com.geargames.common.Graphics;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.NullRegion;
import com.geargames.common.util.Region;

/**
 * Панель "заголовок" висит посредине верхней стороны экрана.
 */
public class PHeadlinePanel extends PContentPanel {
    private PLabel label;

    public PHeadlinePanel(PObject prototype) {
        super(prototype);
    }

    public void draw(Graphics graphics, int x, int y) {

    }

    public boolean event(int code, int param, int x, int y) {
        return false;
    }

    public Region getDrawRegion() {
        return NullRegion.instance;
    }

    public Region getTouchRegion() {
        return NullRegion.instance;
    }

    public boolean isVisible() {
        return true;
    }
}
