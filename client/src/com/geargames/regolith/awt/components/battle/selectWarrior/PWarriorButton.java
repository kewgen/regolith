package com.geargames.regolith.awt.components.battle.selectWarrior;

import com.geargames.awt.components.PRadioButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: abarakov
 * Date: 17.04.13
 * Кнопка, при нажатии на которую, ход передается соответствующему бойцу.
 */
public class PWarriorButton extends PRadioButton {
    private Warrior warrior;

    public PWarriorButton(PObject prototype) {
        super(prototype);
    }

    @Override
    public void onClick() {
        PRegolithPanelManager.getInstance().getBattleWarriorListPanel().onWarriorButtonClick(warrior);
    }

    public void setWarrior(Warrior warrior) {

    }

}
