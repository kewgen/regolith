package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PEntitledRadioButton;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 *
 **/
public class PBagButton extends PEntitledRadioButton {
    private PWarriorPanel panel;

    public PBagButton(PObject prototype, PWarriorPanel panel) {
        super(prototype);
        setText(com.geargames.common.String.valueOfC("СУМКА"));
        this.panel = panel;
    }

    public void onClick() {
        panel.getBagTacklesElement().setVisible(true);
        panel.getWarriorTacklesElement().setVisible(false);
        panel.getStoreTacklesElement().setVisible(false);
    }
}
