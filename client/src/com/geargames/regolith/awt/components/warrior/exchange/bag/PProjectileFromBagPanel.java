package com.geargames.regolith.awt.components.warrior.exchange.bag;

import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.warrior.exchange.PExchangePanel;
import com.geargames.regolith.awt.components.warrior.exchange.PProjectileExchangePanel;
import com.geargames.regolith.units.AmmunitionPacket;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.Weapon;

/**
 * User: mikhail v. kutuzov
 * Панель для отображения патронов и перемещения их сумки бойца.
 */
public class PProjectileFromBagPanel extends PProjectileExchangePanel {
    public PProjectileFromBagPanel(PObject prototype) {
        super(prototype);
    }

    protected void addButton1(PObject prototype, Index index) {
        PABag2StoreHouseButton button = new PABag2StoreHouseButton(prototype);
        button.setPanel(this);
        addActiveChild(button, index);
    }

    protected void addButton2(PObject prototype, Index index) {
        PABag2WarriorButton button = new PABag2WarriorButton(prototype);
        button.setPanel(this);
        addActiveChild(button, index);
    }
}
