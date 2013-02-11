package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.Armor;

import java.util.List;

/**
 * @author Mikhail_Kutuzov
 *         created: 30.05.12  12:03
 */
public class ServerArmorCollection extends ArmorCollection {

    private List<Armor> armors;

    public List<Armor> getArmors() {
        return armors;
    }

    public void setArmors(List<Armor> armors) {
        this.armors = armors;
    }

    @Override
    public Armor get(int index) {
        return armors.get(index);
    }

    @Override
    public void add(Armor armor) {
        armors.add(armor);
    }

    @Override
    public void insert(Armor armor, int index) {
        armors.add(index, armor);
    }

    @Override
    public void set(Armor armor, int index) {
        armors.set(index, armor);
    }

    @Override
    public void remove(int index) {
        armors.remove(index);
    }

    @Override
    public int size() {
        return armors.size();
    }
}
