package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Тестовая кнопка для вызова окна "Символы ASCII" (для тестирования шрифтов).
 */
public class PFontTestButton extends PTouchButton {

    public PFontTestButton(PObject prototype) {
        super(prototype);
    }

    public void onClick() {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.hide(panelManager.getMainMenu());
        panelManager.show(panelManager.getFontTestWindow());
    }

}
