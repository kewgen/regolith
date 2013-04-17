package com.geargames.regolith.awt.components.battle.shotMenu;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Date: 17.04.13
 * Кнопка, при нажатии на которую, боец делает выстрел наспех.
 */
public class PHastilyShotButton extends PTouchButton {

    public PHastilyShotButton(PObject prototype) {
        super(prototype);
    }

    @Override
    public void onClick() {
        PRegolithPanelManager.getInstance().getBattleShotMenuPanel().onHastilyShotButtonClick();
    }

}
