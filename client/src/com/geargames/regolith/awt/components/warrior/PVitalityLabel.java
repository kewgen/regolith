package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PLabel;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.Graphics;
import com.geargames.common.String;
import com.geargames.common.packer.IndexObject;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mikhail v. kutuzov
 * Лейбл для отображения живучести бойца.
 */
public class PVitalityLabel extends PSimpleLabel {
    private Warrior warrior;

    public void draw(Graphics graphics, int x, int y) {
        setData(String.valueOfI(warrior.getVitality()));
        super.draw(graphics, x, y);
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }
}
