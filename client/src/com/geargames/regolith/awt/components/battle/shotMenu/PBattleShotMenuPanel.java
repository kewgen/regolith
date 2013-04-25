package com.geargames.regolith.awt.components.battle.shotMenu;

import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.NotificationBox;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.BattleUnit;

/**
 * User: abarakov
 * 17.04.13
 * Панель, на которой располагаются кнопки выстрела наспех и прицельного выстрела.
 */
public class PBattleShotMenuPanel extends PRootContentPanel {

    public PBattleShotMenuPanel(PObject prototype) {
        super(prototype);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                PAccurateShotButton accurateShotButton = new PAccurateShotButton((PObject) index.getPrototype());
                addActiveChild(accurateShotButton, index);
                break;
            case 1:
                PHastilyShotButton hastilyShotButton = new PHastilyShotButton((PObject) index.getPrototype());
                addActiveChild(hastilyShotButton, index);
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
     * Показать панельку для управления типом выстрела по вражескому бойцу battleUnit.
     * @param battleUnit
     */
    public void showPanel(BattleUnit battleUnit) {

    }

    /**
     * Обработчик нажатия на кнопку "Выстрел наспех".
     */
    public void onHastilyShotButtonClick() {
        NotificationBox.info("Выстрел наспех", this);
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        BattleUnit battleUnit = panelManager.getBattleScreen().getUser();
        //todo: Боец может стрелять только в случае, если сейчас наш ход и он сейчас не выполняет других команд
        if (panelManager.getBattleScreen().isMyTurn() && WarriorHelper.mayHastilyShot(battleUnit.getUnit().getWarrior())) {
            battleUnit.getUnit().stop(); //todo: Нужно ли?
            battleUnit.getUnit().shoot();
            WarriorHelper.doHastilyShot(battleUnit.getUnit().getWarrior());
        }
    }

    /**
     * Обработчик нажатия на кнопку "Прицельный выстрел".
     */
    public void onAccurateShotButtonClick() {
        NotificationBox.info("Прицельный выстрел", this);
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        BattleUnit battleUnit = panelManager.getBattleScreen().getUser();
        //todo: Боец может стрелять только в случае, если сейчас наш ход и он сейчас не выполняет других команд
        if (panelManager.getBattleScreen().isMyTurn()) {
            battleUnit.getUnit().stop(); //todo: Нужно ли?
            battleUnit.getUnit().hit();
        }
    }

}
