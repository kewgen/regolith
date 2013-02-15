package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: mikhail v. kutuzov
 * Отмена выбрать\выкинуть бойца из отряда.
 */
public class PWarriorInformationCancel extends PTouchButton {
    public PWarriorInformationCancel(PObject prototype) {
        super(prototype);
    }

    public void action() {
        PRegolithPanelManager fabric = PRegolithPanelManager.getInstance();
        fabric.hideModal();
    }
}