package com.geargames.regolith.awt.components.battle.battleMenu;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Date: 17.04.13
 * Кнопка, при нажатии на которую, если игрок находится в зоне выхода из битвы, то игрок покидает битву.
 */
public class PLeaveBattleButton extends PTouchButton {

    public PLeaveBattleButton(PObject prototype) {
        super(prototype);
    }

    @Override
    public void onClick() {
        PRegolithPanelManager.getInstance().getBattleMenuPanel().onLeaveBattleButtonClick();
    }

}
