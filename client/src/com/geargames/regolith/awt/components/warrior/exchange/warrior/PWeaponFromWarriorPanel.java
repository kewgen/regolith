package com.geargames.regolith.awt.components.warrior.exchange.warrior;

import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.warrior.exchange.PWeaponExchangePanel;
import com.geargames.regolith.units.tackle.Weapon;

/**
 * User: mikhail v. kutuzov
 *
 */
public class PWeaponFromWarriorPanel extends PWeaponExchangePanel {
    public PWeaponFromWarriorPanel(PObject prototype) {
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
