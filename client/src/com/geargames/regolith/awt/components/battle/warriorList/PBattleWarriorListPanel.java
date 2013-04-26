package com.geargames.regolith.awt.components.battle.warriorList;

import com.geargames.awt.components.PRadioGroup;
import com.geargames.common.logging.Debug;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.NotificationBox;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.BattleUnit;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: abarakov
 * 17.04.13
 * Панель, на которой располагается список бойцов игрока.
 */
public class PBattleWarriorListPanel extends PRootContentPanel {
    private PWarriorButton[] warriorButtons;
    private PRadioGroup radioGroup;
    private boolean isBusy = false;

    private static final int WARRIOR_BUTTON_COUNT_MAX = 4;

    public PBattleWarriorListPanel(PObject prototype) {
        super(prototype);
        for (int i = 0; i < WARRIOR_BUTTON_COUNT_MAX; i++) {
            if (warriorButtons[i] == null) {
                Debug.error("PBattleWarriorListPanel: warriorButtons[" + i + "] == null");
            }
        }
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
            case 1:
            case 2:
            case 3:
                if (warriorButtons == null) {
                    warriorButtons = new PWarriorButton[WARRIOR_BUTTON_COUNT_MAX];
                }
                if (radioGroup == null) {
                    radioGroup = new PRadioGroup(WARRIOR_BUTTON_COUNT_MAX);
                }
                PWarriorButton warriorButton = new PWarriorButton((PObject) index.getPrototype());
                warriorButtons[index.getSlot()] = warriorButton;
                warriorButton.setVisible(false);
                radioGroup.addButton(warriorButton);
                addActiveChild(warriorButton, index);
                break;
        }
    }

    /**
     * Обновить кнопки с информацией о бойцах.
     */
    public void updatePanel() {
        ArrayList group = PRegolithPanelManager.getInstance().getBattleScreen().getGroup();
        int size = group.size();
        if (size > WARRIOR_BUTTON_COUNT_MAX) {
            Debug.error("PBattleWarriorListPanel: size > WARRIOR_BUTTON_COUNT_MAX (" + size + " > " + WARRIOR_BUTTON_COUNT_MAX + ")");
            size = WARRIOR_BUTTON_COUNT_MAX;
        }
        BattleUnit activeUnit = PRegolithPanelManager.getInstance().getBattleScreen().getUser();
        int index = 0;
        for (int i = 0; i < size; i++) {
            BattleUnit battleUnit = (BattleUnit) group.get(i);
            if (!WarriorHelper.isDead(battleUnit.getUnit().getWarrior())) {
                warriorButtons[index].setUnit(battleUnit);
                warriorButtons[index].setVisible(true);
                warriorButtons[index].setChecked(battleUnit == activeUnit);
                index++;
            }
        }
        for (int i = index; i < WARRIOR_BUTTON_COUNT_MAX; i++) {
            warriorButtons[i].setVisible(false);
        }
    }

    /**
     * Обработчик события изменения активного бойца.
     */
    public void onActiveUnitChanged(BattleUnit activeUnit) {
        ArrayList group = PRegolithPanelManager.getInstance().getBattleScreen().getGroup();
        int index = 0;
        for (int i = 0; i < group.size(); i++) {
            BattleUnit battleUnit = (BattleUnit) group.get(i);
            if (!WarriorHelper.isDead(battleUnit.getUnit().getWarrior())) {
                if (battleUnit == activeUnit) {
                    isBusy = true;
                    try {
                        warriorButtons[index].setChecked(true);
                    } finally {
                        isBusy = false;
                    }
                    break;
                }
                index++;
            }
        }
    }

    @Override
    public void onShow() {
        updatePanel();
    }

    @Override
    public void onHide() {

    }

    /**
     * Обработчик нажатия на кнопку выбора бойца.
     */
    public void onWarriorButtonClick(BattleUnit unit) {
        if (!isBusy) {
            NotificationBox.info("Боец '" + unit.getUnit().getWarrior().getName() + "' (id = " + unit.getUnit().getWarrior().getId() + ")", this);
            PRegolithPanelManager.getInstance().getBattleScreen().setActiveUnit(unit);
            PRegolithPanelManager.getInstance().getBattleScreen().displayWarrior(unit);
        }
    }

}
