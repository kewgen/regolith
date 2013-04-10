package com.geargames.regolith.awt.components.playerInfo;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Date: 10.04.13
 * Кнопка для исключения игрока из боевого отряда.
 */
public class PEvictButton extends PTouchButton {

    public PEvictButton(PObject prototype) {
        super(prototype);
    }

    public void onClick() {
        PRegolithPanelManager.getInstance().getPlayerInfoPanel().onEvictButtonClick();
    }

}
