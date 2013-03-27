package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.*;
import com.geargames.common.String;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mikhail v. kutuzov
 * Лейбл здоровья бойца.
 */
public class PHealthLabel extends PSimpleLabel {
    private Warrior warrior;

    public void draw(Graphics graphics, int x, int y) {
        setText(com.geargames.common.String.valueOfI(warrior.getHealth()).concat(String.valueOfC("/").concatI(WarriorHelper.getMaxHealth(warrior, ClientConfigurationFactory.getConfiguration().getBaseConfiguration()))));
        super.draw(graphics, x, y);
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }
}
