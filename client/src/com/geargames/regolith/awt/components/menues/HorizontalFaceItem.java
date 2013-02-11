package com.geargames.regolith.awt.components.menues;

import com.geargames.awt.components.PPrototypeElement;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mikhail v. kutuzov
 * Элемент горизонтального списка в виде лица игрока.
 */
public class HorizontalFaceItem extends PPrototypeElement {
    private Warrior warrior;

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }
}
