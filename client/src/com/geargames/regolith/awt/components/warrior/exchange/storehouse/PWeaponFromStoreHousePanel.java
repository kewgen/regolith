package com.geargames.regolith.awt.components.warrior.exchange.storehouse;

import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.awt.components.warrior.exchange.PExchangePanel;
import com.geargames.regolith.awt.components.warrior.exchange.PWeaponExchangePanel;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.base.StoreHouseHelper;
import com.geargames.regolith.units.tackle.AbstractTackle;
import com.geargames.regolith.units.tackle.Weapon;

/**
 * User: mikhail v. kutuzov
 * Панель для перемещения оружия со склада в руки или сумку бойца.
 */
public class PWeaponFromStoreHousePanel extends PWeaponExchangePanel {
    public PWeaponFromStoreHousePanel(PObject prototype) {
        super(prototype);
    }

    protected void addButton1(PObject prototype, Index index) {
        PTStoreHouse2BagButton button = new PTStoreHouse2BagButton(prototype);
        button.setPanel(this);
        addActiveChild(button, index);
    }

    protected void addButton2(PObject prototype, Index index) {
        PTStoreHouse2WarriorButton button = new PTStoreHouse2WarriorButton(prototype);
        button.setPanel(this);
        addActiveChild(button, index);
    }
}
