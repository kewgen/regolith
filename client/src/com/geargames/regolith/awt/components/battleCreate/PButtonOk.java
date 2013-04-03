package com.geargames.regolith.awt.components.battleCreate;

import com.geargames.awt.components.PEntitledTouchButton;
import com.geargames.common.logging.Debug;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Date: 04.03.13
 * Кнопка Ok для создания битвы.
 */
public class PButtonOk extends PEntitledTouchButton {

    public PButtonOk(PObject prototype) {
        super(prototype);
    }

    public void onClick() {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getBattleCreatePanel().onButtonOkClick();
    }

}
