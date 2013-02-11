package com.geargames.regolith.awt.components.warrior.exchange.warrior;

import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.warrior.exchange.PArmorExchangePanel;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.StateTackle;

/**
 * User: mikhail v. kutuzov
 * Панель для перемещения брони с бойца на склад либо в сумку.
 */
public class PArmorFromWarriorPanel extends PArmorExchangePanel {
    public PArmorFromWarriorPanel(PObject prototype) {
        super(prototype);
    }

    protected void addButton1(PObject prototype, Index index) {
        PTWarrior2BagButton button = new PTWarrior2BagButton(prototype);
        button.setPanel(this);
        addActiveChild(button, index);
    }

    protected void addButton2(PObject prototype, Index index) {
        PTWarrior2StoreHouseButton button = new PTWarrior2StoreHouseButton(prototype);
        button.setPanel(this);
        addActiveChild(button, index);
    }
}
