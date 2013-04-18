package com.geargames.regolith.awt.components.battle.selectWarrior;

import com.geargames.awt.components.PRadioGroup;
import com.geargames.common.logging.Debug;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.units.BattleUnit;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: abarakov
 * 17.04.13
 * Панель, на которой располагается список бойцов игрока.
 */
public class PBattleSelectWarriorPanel extends PRootContentPanel {
    private PWarriorButton[] warriorButtons;
    private PRadioGroup radioGroup;

    private static final int WARRIOR_BUTTON_COUNT_MAX = 4;

    public PBattleSelectWarriorPanel(PObject prototype) {
        super(prototype);
        for (int i = 0; i < WARRIOR_BUTTON_COUNT_MAX; i++) {
            if (warriorButtons[i] == null) {
                Debug.error("PBattleSelectWarriorPanel: warriorButtons[" + i + "] == null");
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

    public void updatePanel() {
//        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
//        BattleGroup battleGroup = ClientBattleHelper.tryFindBattleGroupByAccountId(
//                configuration.getBattle(), configuration.getAccount().getId());
//        WarriorCollection warriors = battleGroup.getWarriors();


        ArrayList group = PRegolithPanelManager.getInstance().getBattleScreen().getGroup();
        int size = group.size();
        if (size > WARRIOR_BUTTON_COUNT_MAX) {
            Debug.error("PBattleSelectWarriorPanel: size > WARRIOR_BUTTON_COUNT_MAX (" + size + " > " + WARRIOR_BUTTON_COUNT_MAX + ")");
            size = WARRIOR_BUTTON_COUNT_MAX;
        }
        int index = 0;
        for (int i = 0; i < size; i++) {
            BattleUnit battleUnit = (BattleUnit) group.get(i);
//            if (!battleUnit.getUnit().isDie()) { //todo: где взять isDie() ?
                warriorButtons[index].setWarrior(battleUnit.getUnit().getWarrior());
                warriorButtons[index].setVisible(true);
                index++;
//            }
        }
        for (int i = index; i < WARRIOR_BUTTON_COUNT_MAX; i++) {
            warriorButtons[i].setVisible(false);
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
    public void onWarriorButtonClick(Warrior warrior) {

    }

}
