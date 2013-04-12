package com.geargames.regolith.awt.components.warrior.exchange;

import com.geargames.awt.DrawablePPanel;
import com.geargames.awt.components.PEntitledTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: mikhail v. kutuzov
 * Предок всех кнопок которые занимаются перемещением вещей.
 */
public abstract class PExchangeButton extends PEntitledTouchButton {
    private PExchangePanel panel;

    protected PExchangeButton(PObject prototype) {
        super(prototype);
    }

    public PExchangePanel getPanel() {
        return panel;
    }

    public void setPanel(PExchangePanel panel) {
        this.panel = panel;
    }

    public void onClick() {
        doAction();
        PRegolithPanelManager fabric = PRegolithPanelManager.getInstance();
        DrawablePPanel modalPanel = fabric.getModalPanel();
        if (modalPanel != null) {
            fabric.hide(modalPanel);
        }
    }

    protected abstract void doAction();

}
