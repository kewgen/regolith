package com.geargames.regolith.awt.components.warrior.exchange.storehouse;

import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.awt.components.warrior.exchange.PArmorExchangePanel;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.base.StoreHouseHelper;
import com.geargames.regolith.units.tackle.AbstractTackle;
import com.geargames.regolith.units.tackle.Armor;

/**
 * User: mikhail v. kutuzov
 * Date: 10.12.12
 * Time: 11:13
 */
public class PArmorFromStoreHousePanel extends PArmorExchangePanel {
    public PArmorFromStoreHousePanel(PObject prototype) {
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
