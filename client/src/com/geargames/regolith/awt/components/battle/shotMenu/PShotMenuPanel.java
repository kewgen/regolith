package com.geargames.regolith.awt.components.battle.shotMenu;

import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRootContentPanel;

/**
 * User: abarakov
 * 17.04.13
 * Панель, на которой располагается кнопки выстрела наспех и прицельного выстрела.
 */
public class PShotMenuPanel extends PRootContentPanel {

    public PShotMenuPanel(PObject prototype) {
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
     * Обработчик нажатия на кнопку "Выстрел наспех".
     */
    public void onHastilyShotButtonClick() {

    }

    /**
     * Обработчик нажатия на кнопку "Прицельный выстрел".
     */
    public void onAimedShotButtonClick() {

    }

}
