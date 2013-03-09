package com.geargames.regolith.awt.components.battleCreate;

import com.geargames.awt.components.PRadioButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Date: 04.03.13
 * Кнопка для выбора количества команд в создаваемой битве.
 */
public class PButtonSide extends PRadioButton {

    private byte sideCount;

    public PButtonSide(PObject prototype, byte sideCount) {
        super(prototype);
        this.sideCount = sideCount;
    }

    public void onClick() {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        ((PBattleCreatePanel)panelManager.getBattleCreate().getElement()).setSideCount(sideCount);
    }
}
