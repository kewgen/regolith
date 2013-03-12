package com.geargames.regolith.awt.components.battleCreate;

import com.geargames.awt.components.PRadioButton;
import com.geargames.common.packer.PObject;

/**
 * User: abarakov
 * Date: 04.03.13
 * Кнопка для выбора количества бойцов на каждого игрока в создаваемой битве.
 */
public class PButtonFighter extends PRadioButton {

//    private byte fighterCount;

    public PButtonFighter(PObject prototype, byte fighterCount) {
        super(prototype);
//        this.fighterCount = fighterCount;
    }

    public void onClick() {
//        ((PBattleCreatePanel)PRegolithPanelManager.getInstance().getBattleCreate().getElement()).setFighterCount(fighterCount);
    }
}
