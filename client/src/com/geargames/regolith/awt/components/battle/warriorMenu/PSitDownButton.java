package com.geargames.regolith.awt.components.battle.warriorMenu;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Date: 17.04.13
 * Кнопка, при нажатии на которую, боец садится.
 */
public class PSitDownButton extends PTouchButton {

    public PSitDownButton(PObject prototype) {
        super(prototype);
    }

    @Override
    public void onClick() {
        PRegolithPanelManager.getInstance().getBattleWarriorMenuPanel().onSitDownButtonClick();
    }

}
