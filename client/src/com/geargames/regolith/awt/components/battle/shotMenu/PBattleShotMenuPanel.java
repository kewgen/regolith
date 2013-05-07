package com.geargames.regolith.awt.components.battle.shotMenu;

import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.NotificationBox;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.map.ClientWarriorElement;

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
     * Показать панельку для управления типом выстрела по вражескому бойцу victimUnit.
     *
     * @param victimUnit
     */
    public void showPanel(ClientWarriorElement victimUnit) {

    }

    /**
     * Обработчик события изменения активного бойца.
     */
    public void onActiveUnitChanged(ClientWarriorElement activeUnit) {

    }

    /**
     * Обработчик нажатия на кнопку "Выстрел наспех".
     */
    public void onHastilyShotButtonClick() {
        NotificationBox.info("Выстрел наспех", this);
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        ClientWarriorElement unit = panelManager.getBattleScreen().getActiveUnit();
        //todo: Боец может стрелять только в случае, если сейчас наш ход и он сейчас не выполняет других команд
        if (unit.getLogic().isIdle() && WarriorHelper.mayHastilyShot(unit)) {
            unit.getLogic().doHastilyShot();
            WarriorHelper.doHastilyShot(unit);
            panelManager.getBattleScreen().doMapReachabilityUpdate(); //todo: пусть это делается в WarriorHelper.doHastilyShot()
        }
    }

    /**
     * Обработчик нажатия на кнопку "Прицельный выстрел".
     */
    public void onAccurateShotButtonClick() {
        NotificationBox.info("Прицельный выстрел", this);
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        ClientWarriorElement unit = panelManager.getBattleScreen().getActiveUnit();
        //todo: Боец может стрелять только в случае, если сейчас наш ход и он сейчас не выполняет других команд
        //todo: Здесь определено другое действие - "Получение урона" вместо "Прицельный выстрел"
        if (unit.getLogic().isIdle()) {
            unit.getLogic().doHit();
        }
    }

}
