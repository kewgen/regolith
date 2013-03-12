package com.geargames.regolith.awt.components.battleCreate;

import com.geargames.awt.components.PRadioButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Date: 04.03.13
 * Кнопка для выбора количества игроков на команду в создаваемой битве.
 */
public class PButtonPlayer extends PRadioButton {

//    private byte playerCount;

    public PButtonPlayer(PObject prototype, byte playerCount) {
        super(prototype);
//        this.playerCount = playerCount;
    }

    public void onClick() {
//        ((PBattleCreatePanel)PRegolithPanelManager.getInstance().getBattleCreate().getElement()).setPlayerCount(playerCount);
    }
}
