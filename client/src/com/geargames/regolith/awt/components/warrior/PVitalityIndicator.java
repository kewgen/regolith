package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PSimpleIndicator;
import com.geargames.common.Graphics;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mikhail v. kutuzov
 * Индикатор живучести бойца.
 * Эта реализация опирается на то что количество спрайтов отвечающих за разные уровни развития соответсвует
 * количеству этих уровней.
 */
public class PVitalityIndicator extends PSimpleIndicator {
    private Warrior warrior;

    public PVitalityIndicator(PObject prototype) {
        super(prototype);
    }

    public void draw(Graphics graphics, int x, int y) {
        setValue(warrior.getVitality());
        super.draw(graphics, x, y);
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }
}
