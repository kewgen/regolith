package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PEntitledRadioButton;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 *
 */
public class PStoreHouseButton extends PEntitledRadioButton {
    private PWarriorPanel panel;

    public PStoreHouseButton(PObject prototype, PWarriorPanel panel) {
        super(prototype, com.geargames.common.String.valueOfC("СКЛАД"));
        this.panel = panel;
    }

    public void onClick() {
        panel.getBagTacklesElement().setVisible(false);
        panel.getWarriorTacklesElement().setVisible(false);
        panel.getStoreTacklesElement().setVisible(true);
    }
}
