package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PContentPanel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Панель на которой отображены текущие битвы + кнопка [создать битву].
 */
public class PBattlesPanel extends PContentPanel {
    private PBattlesList battleList;
    private PBattleCreateButton createButton;

    public PBattlesPanel(PObject prototype) {
        super(prototype);
    }

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                battleList = new PBattlesList((PObject)index.getPrototype());
                addActiveChild(battleList, index);
                break;
            case 1:
                createButton = new PBattleCreateButton((PObject)index.getPrototype());
                addActiveChild(createButton, index);
                break;
        }
    }

    public PBattlesList getBattleList() {
        return battleList;
    }

    public PBattleCreateButton getCreateButton() {
        return createButton;
    }
}
