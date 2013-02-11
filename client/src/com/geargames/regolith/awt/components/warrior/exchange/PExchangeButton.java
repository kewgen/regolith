package com.geargames.regolith.awt.components.warrior.exchange;

import com.geargames.awt.components.PEntitledTouchButton;
import com.geargames.common.String;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PPanelSingletonFabric;

/**
 * User: mikhail v. kutuzov
 * Предок всех кнопок которые занимаются перемещением вещей.
 */
public abstract class PExchangeButton extends PEntitledTouchButton {
    private PExchangePanel panel;

    protected PExchangeButton(PObject prototype, String title) {
        super(prototype, title);
    }

    public PExchangePanel getPanel() {
        return panel;
    }

    public void setPanel(PExchangePanel panel) {
        this.panel = panel;
    }

    public void action() {
        doAction();
        PPanelSingletonFabric.getInstance().hideModal();
    }

    protected abstract void doAction();

}
