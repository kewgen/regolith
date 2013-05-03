package com.geargames.regolith.awt.components.battle.warriorList;

import com.geargames.awt.components.PRadioButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.units.map.ClientHumanElement;

/**
 * User: abarakov
 * Date: 17.04.13
 * Кнопка, при нажатии на которую, ход передается соответствующему бойцу.
 */
public class PWarriorButton extends PRadioButton {
    private ClientHumanElement unit;

    public PWarriorButton(PObject prototype) {
        super(prototype);
    }

    @Override
    public void onClick() {
        PRegolithPanelManager.getInstance().getBattleWarriorListPanel().onWarriorButtonClick(unit);
    }

    public void setUnit(ClientHumanElement unit) {
        this.unit = unit;
    }

}
