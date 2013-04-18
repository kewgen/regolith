package com.geargames.regolith.awt.components.battle.battleMenu;

import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRootContentPanel;

/**
 * User: abarakov
 * 17.04.13
 * Панель, на которой располагаются кнопки меню битвы, такие как, выйти из битвы через зону выхода на карте, пропустить
 * ход или открыть чат.
 */
public class PBattleMenuPanel extends PRootContentPanel {
    private PLeaveBattleButton leaveBattleButton;

    public PBattleMenuPanel(PObject prototype) {
        super(prototype);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                PSkipMoveButton skipMoveButton = new PSkipMoveButton((PObject) index.getPrototype());
                addActiveChild(skipMoveButton, index);
                break;
            case 1:
                POpenChatButton openChatButton = new POpenChatButton((PObject) index.getPrototype());
                addActiveChild(openChatButton, index);
                break;
            case 2:
                leaveBattleButton = new PLeaveBattleButton((PObject) index.getPrototype());
                leaveBattleButton.setVisible(false);
                addActiveChild(leaveBattleButton, index);
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
     * Обработчик нажатия на кнопку "Передать ход следующему игроку".
     */
    public void onSkipMoveButtonClick() {

    }

    /**
     * Обработчик нажатия на кнопку "Открыть чат".
     */
    public void onOpenChatButtonClick() {

    }

    /**
     * Обработчик нажатия на кнопку "Выйти из битвы".
     */
    public void onLeaveBattleButtonClick() {

    }

}
