package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: mikhail v. kutuzov
 * Кнопка выхода на панель наёмников.
 */
public class PMerсenaryButton extends PTouchButton {
    public PMerсenaryButton(PObject prototype) {
        super(prototype);
    }

    public void action() {
        PRegolithPanelManager instance = PRegolithPanelManager.getInstance();
        instance.hide(instance.getMainMenu());
        instance.show(instance.getWarrior());
    }
}
