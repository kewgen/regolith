package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PEntitledRadioButton;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 *
 */
public class PWarriorButton extends PEntitledRadioButton {
    private PWarriorPanel panel;

    public PWarriorButton(PObject prototype, PWarriorPanel panel) {
        super(prototype, com.geargames.common.String.valueOfC("НАДЕТО"));
        this.panel = panel;
    }

    public void action() {
        panel.getBagTacklesElement().setVisible(false);
        panel.getWarriorTacklesElement().setVisible(true);
        panel.getStoreTacklesElement().setVisible(false);
    }
}
