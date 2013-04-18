package com.geargames.regolith.awt.components.battle.battleMenu;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Date: 17.04.13
 * Кнопка, при нажатии на которую, будет открыт чат между игроками военного союза.
 */
public class POpenChatButton extends PTouchButton {

    public POpenChatButton(PObject prototype) {
        super(prototype);
    }

    @Override
    public void onClick() {
        PRegolithPanelManager.getInstance().getBattleMenuPanel().onOpenChatButtonClick();
    }

}
