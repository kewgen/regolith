package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PLabel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.util.Region;
import com.geargames.regolith.awt.components.DrawablePPanel;
import com.geargames.regolith.awt.components.PPanelSingletonFabric;

/**
 * User: mikhail v. kutuzov
 * Компонент отображает величину и открывает модальное окошко связанное с компонентом.
 */
public class PShowingModalLabel extends PLabel {
    private Region region;
    private DrawablePPanel panel;

    public PShowingModalLabel(IndexObject index, Region region) {
        super(index);
        this.region = region;
    }

    public PShowingModalLabel() {
    }

    public boolean event(int code, int param, int x, int y) {
        if (region.isWithIn(x, y)) {
            if (panel != null) {
                PPanelSingletonFabric.getInstance().showModal(panel);
            }
        }
        return false;
    }

    public DrawablePPanel getPanel() {
        return panel;
    }

    public void setPanel(DrawablePPanel panel) {
        this.panel = panel;
    }

    public Region getDrawRegion() {
        return region;
    }

    public Region getTouchRegion() {
        return region;
    }
}
