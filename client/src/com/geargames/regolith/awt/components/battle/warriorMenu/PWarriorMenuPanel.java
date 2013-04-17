package com.geargames.regolith.awt.components.battle.warriorMenu;

import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRootContentPanel;

/**
 * User: abarakov
 * 17.04.13
 * Панель, на которой располагается кнопка переключения положения Стоит/Сидит у активного бойца.
 */
public class PWarriorMenuPanel extends PRootContentPanel {
    private PStandUpButton leaveBattleButton;

    public PWarriorMenuPanel(PObject prototype) {
        super(prototype);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                PStandUpButton standUpButton = new PStandUpButton((PObject) index.getPrototype());
                standUpButton.setVisible(false);
                addActiveChild(standUpButton, index);
                break;
            case 1:
                PSitDownButton sitDownButton = new PSitDownButton((PObject) index.getPrototype());
                addActiveChild(sitDownButton, index);
                break;
        }
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    /**
     * Обработчик нажатия на кнопку "Посадить игрока".
     */
    public void onStandUpButtonClick() {

    }

    /**
     * Обработчик нажатия на кнопку "Поднять игрока".
     */
    public void onSitDownButtonClick() {

    }

}
