package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.DrawablePPanel;
import com.geargames.regolith.awt.components.PPanelSingletonFabric;

/**
 * User: mikhail v. kutuzov
 * Кнопка выхода на панель наёмников.
 */
public class PMerсenaryButton extends PTouchButton {
    public PMerсenaryButton(PObject prototype) {
        super(prototype);
    }

    public void action() {
        PPanelSingletonFabric instance = PPanelSingletonFabric.getInstance();
        instance.hide((DrawablePPanel)instance.getMainMenu());
        instance.show((DrawablePPanel)instance.getWarrior());
    }
}
