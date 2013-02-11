package com.geargames.regolith.awt.components.warrior.exchange.bag;

import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.warrior.exchange.PWeaponExchangePanel;

/**
 * User: mikhail v. kutuzov
 * Панель для перемещения оружия из сумки на склад или в руки бойца.
 */
public class PWeaponFromBagPanel extends PWeaponExchangePanel {
    public PWeaponFromBagPanel(PObject prototype) {
        super(prototype);
    }

    protected void addButton1(PObject prototype, Index index) {
        PTBag2StoreHouseButton button = new PTBag2StoreHouseButton(prototype);
        button.setPanel(this);
        addActiveChild(button, index);
    }

    protected void addButton2(PObject prototype, Index index) {
        PTBag2WarriorButton button = new PTBag2WarriorButton(prototype);
        button.setPanel(this);
        addActiveChild(button, index);
    }
}
