package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Тестовая кнопка для вызова диалогового окна "Создание битвы".
 */
public class PBattleCreateButton extends PTouchButton {

    public PBattleCreateButton(PObject prototype) {
        super(prototype);
    }

    public void onClick() {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.hide(panelManager.getMainMenu());
        panelManager.show(panelManager.getBattleCreate());
    }

}
