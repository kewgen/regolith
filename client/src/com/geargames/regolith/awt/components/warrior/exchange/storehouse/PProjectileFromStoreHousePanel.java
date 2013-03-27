package com.geargames.regolith.awt.components.warrior.exchange.storehouse;

import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.warrior.exchange.PProjectileExchangePanel;

/**
 * User: mikhail v. kutuzov
 * Панель для перемещения патронов со склада, в сумку или оружие бойца.
 */
public class PProjectileFromStoreHousePanel extends PProjectileExchangePanel {
    public PProjectileFromStoreHousePanel(PObject prototype) {
        super(prototype);
    }

    protected void addButton1(PObject prototype, Index index) {
        PAStoreHouse2BagButton button = new PAStoreHouse2BagButton(prototype);
        button.setPanel(this);
        addActiveChild(button, index);
    }

    protected void addButton2(PObject prototype, Index index) {
        PAStoreHouse2WarriorButton button = new PAStoreHouse2WarriorButton(prototype);
        button.setPanel(this);
        addActiveChild(button, index);
    }
}
