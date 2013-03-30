package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.Graphics;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mikhail v. kutuzov
 *
 */
public class PMarksmanshipLabel extends PSimpleLabel {
    private Warrior warrior;

    public void draw(Graphics graphics, int x, int y) {
        setText(""+warrior.getMarksmanship());
        super.draw(graphics, x, y);
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }
}
