package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: mikhail v. kutuzov
 * Кнопка "Назад" или для возврата в главное меню.
 */
public class PBackButton extends PTouchButton {

    public PBackButton(PObject prototype) {
        super(prototype);
    }

    public void onClick() {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.hideAll();
        panelManager.show(panelManager.getMainMenu());
    }

}
