package com.geargames.regolith.awt.components.battleList;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Кнопка для создания битвы.
 */
public class PBattleCreateButton extends PTouchButton {

    public PBattleCreateButton(PObject prototype) {
        super(prototype);
    }

    @Override
    public void onClick() {
        PRegolithPanelManager.getInstance().getBattlesPanel().onBattleCreateButtonClick();
    }

}
