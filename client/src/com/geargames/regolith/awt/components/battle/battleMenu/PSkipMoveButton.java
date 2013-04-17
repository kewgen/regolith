package com.geargames.regolith.awt.components.battle.battleMenu;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Date: 17.04.13
 * Кнопка, при нажатии на которую, игрок завершает свой ход и ход передается следующему игроку.
 */
public class PSkipMoveButton extends PTouchButton {

    public PSkipMoveButton(PObject prototype) {
        super(prototype);
    }

    @Override
    public void onClick() {
        PRegolithPanelManager.getInstance().getBattleMenuPanel().onSkipMoveButtonClick();
    }

}
