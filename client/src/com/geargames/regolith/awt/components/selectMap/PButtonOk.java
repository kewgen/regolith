package com.geargames.regolith.awt.components.selectMap;

import com.geargames.awt.components.PEntitledTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Date: 26.03.13
 * Кнопка Ok для подтверждения выбора карты.
 */
public class PButtonOk extends PEntitledTouchButton {

    public PButtonOk(PObject prototype) {
        super(prototype);
    }

    public void onClick() {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getSelectMapPanel().onButtonOkClick();
    }

}
