package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Date: 11.04.13
 * Кнопка начала битвы.
 */
public class PStartBattleButton extends PTouchButton {

    public PStartBattleButton(PObject prototype) {
        super(prototype);
    }

    @Override
    public void onClick() {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getBattlesPanel().onStartBattleButtonClick();
    }

}
