package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.Graphics;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.common.String;

/**
 * User: mikhail v. kutuzov
 * Date: 10.11.12
 * Time: 21:45
 */
public class PSpeedLabel extends PSimpleLabel {
    private Warrior warrior;

    public void draw(Graphics graphics, int x, int y) {
        setText(String.valueOfI(warrior.getSpeed()));
        super.draw(graphics, x, y);
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }
}
