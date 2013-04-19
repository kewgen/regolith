package com.geargames.regolith.awt.components.battle.weaponMenu;

import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.NotificationBox;
import com.geargames.regolith.awt.components.PRootContentPanel;

/**
 * User: abarakov
 * 17.04.13
 * Панель, на которой располагаются кнопки переключения оружия, его перезарядки и информация об оружии и боеприпасах.
 */
public class PBattleWeaponMenuPanel extends PRootContentPanel {

    public PBattleWeaponMenuPanel(PObject prototype) {
        super(prototype);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                super.createDefaultElementByIndex(index, prototype);
//                PSelectNextWeaponButton selectNextWeaponButton = new PSelectNextWeaponButton((PObject) index.getPrototype());
//                addActiveChild(selectNextWeaponButton, index);
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
     * Обработчик нажатия на кнопку "Выбрать следующее оружие".
     */
    public void onSelectNextWeaponButtonClick() {
        NotificationBox.info("Оружие", this);
    }

}
