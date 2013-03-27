package com.geargames.regolith.awt.components.warrior.exchange.storehouse;

import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.warrior.exchange.PMedikitExchangePanel;

/**
 * User: mikhail v. kutuzov
 * Панель для перемещения аптечки со склада в сумку бойца.
 */
public class PMedikitFromStoreHousePanel extends PMedikitExchangePanel {

    public PMedikitFromStoreHousePanel(PObject prototype) {
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
