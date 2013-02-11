package com.geargames.regolith.awt.components.menues;

import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.warrior.PAbstractTacklePanel;
import com.geargames.regolith.awt.components.warrior.PWarriorCharacteristics;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.AbstractTackle;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * То, что надето на войне засунуто в вектор ) для работы со списками прокрутки.
 */
public class WarriorVerticalTackleItemsVector extends Vector {
    private Warrior warrior;
    private WarriorVerticalTackleItem[] items;

    public WarriorVerticalTackleItemsVector(PObject menuElement) {
        items = new WarriorVerticalTackleItem[4];
        for(int i = 0; i < items.length; i++){
            WarriorVerticalTackleItem item = new WarriorVerticalTackleItem(menuElement);
            items[i] = item;
        }
    }

    public void reset(Warrior warrior) {
        this.warrior = warrior;
    }


    private void setIfDifferent(AbstractTackle tackle, WarriorVerticalTackleItem item) {
        if (item.getTackle() != tackle) {
            item.setTackle(tackle, 1);
        }
    }

    public synchronized Object elementAt(int index) {
        WarriorVerticalTackleItem item = items[index];
        switch (index) {
            case 0:
                if (warrior.getWeapon() != null) {
                    setIfDifferent(warrior.getWeapon(), item);
                } else if (warrior.getHeadArmor() != null) {
                    setIfDifferent(warrior.getHeadArmor(), item);
                } else if (warrior.getTorsoArmor() != null) {
                    setIfDifferent(warrior.getTorsoArmor(), item);
                } else if (warrior.getLegsArmor() != null) {
                    setIfDifferent(warrior.getLegsArmor(), item);
                }
                break;
            case 1:
                if (warrior.getHeadArmor() != null) {
                    setIfDifferent(warrior.getHeadArmor(), item);
                } else if (warrior.getTorsoArmor() != null) {
                    setIfDifferent(warrior.getTorsoArmor(), item);
                } else if (warrior.getLegsArmor() != null) {
                    setIfDifferent(warrior.getLegsArmor(), item);
                }
                break;
            case 2:
                if (warrior.getTorsoArmor() != null) {
                    setIfDifferent(warrior.getTorsoArmor(), item);
                } else if (warrior.getLegsArmor() != null) {
                    setIfDifferent(warrior.getLegsArmor(), item);
                }
                break;
            case 3:
                if (warrior.getLegsArmor() != null) {
                    setIfDifferent(warrior.getLegsArmor(), item);
                }
                break;
        }
        return item;
    }

    public synchronized int size() {
        return (warrior.getWeapon() != null ? 1 : 0) + (warrior.getHeadArmor() != null ? 1 : 0) + (warrior.getTorsoArmor() != null ? 1 : 0) + (warrior.getLegsArmor() != null ? 1 : 0);
    }
}
