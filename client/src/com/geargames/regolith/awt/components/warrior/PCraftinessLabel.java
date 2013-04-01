package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.Graphics;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mikhail v. kutuzov
 * Лейбл для отображения ловкости бойца.
 */
public class PCraftinessLabel extends PSimpleLabel {
    private Warrior warrior;

    public void draw(Graphics graphics, int x, int y) {
        setText(""+warrior.getCraftiness());
        super.draw(graphics, x, y);
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }
}
