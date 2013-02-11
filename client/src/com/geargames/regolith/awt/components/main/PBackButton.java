package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PPanelSingletonFabric;

/**
 * User: mikhail v. kutuzov
 * Кнопка возврата в предыдущее состояние.
 */
public class PBackButton extends PTouchButton {

    public PBackButton(PObject prototype) {
        super(prototype);
    }

    public void action() {
        PPanelSingletonFabric fabric = PPanelSingletonFabric.getInstance();
        fabric.hideAll();
        fabric.show(fabric.getMainMenu());
    }
}
