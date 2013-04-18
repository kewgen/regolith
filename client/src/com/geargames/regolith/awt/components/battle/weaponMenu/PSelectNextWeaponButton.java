package com.geargames.regolith.awt.components.battle.weaponMenu;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Date: 17.04.13
 * Кнопка, при нажатии на которую, бойцом будет выбрано следующее оружие.
 */
public class PSelectNextWeaponButton extends PTouchButton {

    public PSelectNextWeaponButton(PObject prototype) {
        super(prototype);
    }

    @Override
    public void onClick() {
        PRegolithPanelManager.getInstance().getBattleWeaponMenuPanel().onSelectNextWeaponButtonClick();
    }

}
