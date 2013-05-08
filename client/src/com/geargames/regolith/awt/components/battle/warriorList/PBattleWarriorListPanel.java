package com.geargames.regolith.awt.components.battle.warriorList;

import com.geargames.awt.components.PRadioGroup;
import com.geargames.common.logging.Debug;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.NotificationBox;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.map.ClientWarriorElement;

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
        ClientWarriorCollection group = PRegolithPanelManager.getInstance().getBattleScreen().getGroupUnits();
        int size = group.size();
        if (size > WARRIOR_BUTTON_COUNT_MAX) {
            Debug.error("PBattleWarriorListPanel: size > WARRIOR_BUTTON_COUNT_MAX (" + size + " > " + WARRIOR_BUTTON_COUNT_MAX + ")");
            size = WARRIOR_BUTTON_COUNT_MAX;
        }
        ClientWarriorElement activeUnit = PRegolithPanelManager.getInstance().getBattleScreen().getActiveUnit();
        int index = 0;
        //todo: использовать PRegolithPanelManager.getInstance().getBattleScreen().getGroupUnits()
        for (int i = 0; i < size; i++) {
            ClientWarriorElement unit = (ClientWarriorElement) group.get(i);
            if (!WarriorHelper.isDead(unit)) {
                warriorButtons[index].setUnit(unit);
                warriorButtons[index].setVisible(true);
                warriorButtons[index].setChecked(unit == activeUnit);
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
    public void onActiveUnitChanged(ClientWarriorElement activeUnit) {
        ClientWarriorCollection group = PRegolithPanelManager.getInstance().getBattleScreen().getGroupUnits();
        int index = 0;
        for (int i = 0; i < group.size(); i++) {
            ClientWarriorElement unit = (ClientWarriorElement) group.get(i);
            if (!WarriorHelper.isDead(unit)) {
                if (unit == activeUnit) {
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
    public void onWarriorButtonClick(ClientWarriorElement unit) {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        if (!isBusy && panelManager.getBattleScreen().isMyTurn()) {
            NotificationBox.info("Боец '" + unit.getName() + "' (id = " + unit.getId() + ")", this);
            PRegolithPanelManager.getInstance().getBattleScreen().setActiveUnit(unit);
            PRegolithPanelManager.getInstance().getBattleScreen().displayWarrior(unit);
        }
    }

}
