package com.geargames.regolith.awt.components.battle.shotMenu;

import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.NotificationBox;
import com.geargames.regolith.awt.components.PRootContentPanel;
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
                PHastilyShotButton hastilyShotButton = new PHastilyShotButton((PObject) index.getPrototype());
                addActiveChild(hastilyShotButton, index);
                break;
            case 1:
                PAimedShotButton aimedShotButton = new PAimedShotButton((PObject) index.getPrototype());
                addActiveChild(aimedShotButton, index);
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
    }

    /**
     * Обработчик нажатия на кнопку "Прицельный выстрел".
     */
    public void onAimedShotButtonClick() {
        NotificationBox.info("Прицельный выстрел", this);
    }

}
