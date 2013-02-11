package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.Armor;

import java.util.Vector;

/**
 * @author Mikhail_Kutuzov
 *         created: 30.05.12  12:07
 */
public class ClientArmorCollection extends ArmorCollection {
    private Vector armors;

    public Vector getArmors() {
        return armors;
    }

    public void setArmors(Vector armors) {
        this.armors = armors;
    }

    public Armor get(int index) {
        return (Armor)armors.elementAt(index);
    }

    public void add(Armor armor) {
        armors.addElement(armor);
    }

    public void insert(Armor armor, int index) {
        armors.insertElementAt(armor, index);
    }

    public void set(Armor armor, int index) {
        armors.setElementAt(armor, index);
    }

    public void remove(int index) {
        armors.removeElementAt(index);
    }

    public int size() {
        return armors.size();
    }
}
