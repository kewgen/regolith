package com.geargames.regolith.awt.components.common;

import com.geargames.awt.DrawablePPanel;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Компонент отображает величину и открывает модальное окошко связанное с компонентом.
 */
public class PShowingModalLabel extends PTouchLabel {
    private DrawablePPanel panel;

    public PShowingModalLabel(PObject index) {
        super(index);
    }

    public DrawablePPanel getPanel() {
        return panel;
    }

    public void setPanel(DrawablePPanel panel) {
        this.panel = panel;
    }

    public void onClick() {
        if (panel != null) {
            PRegolithPanelManager.getInstance().showModal(panel);
        }
    }

}
