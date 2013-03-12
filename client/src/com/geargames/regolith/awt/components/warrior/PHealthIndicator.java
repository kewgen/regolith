package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PElement;
import com.geargames.common.util.Region;
import com.geargames.common.Graphics;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.battle.WarriorHelper;

/**
 * User: mikhail v. kutuzov
 * Обёртка для фрейм индикатора, предназначенная для индикации здоровья бойца.
 */
public class PHealthIndicator extends PElement {
    private PFrameProgressIndicator indicator;
    private Warrior warrior;
    private boolean initiated;

    public Region getTouchRegion() {
        return indicator.getTouchRegion();
    }

    public Region getDrawRegion() {
        return indicator.getDrawRegion();
    }

    public PHealthIndicator(PObject prototype) {
        indicator = new PFrameProgressIndicator(prototype);
    }

    public void draw(Graphics graphics, int x, int y) {
        if(!initiated){
            indicator.setValue(warrior.getHealth() * indicator.getCardinality()/WarriorHelper.getMaxHealth(warrior, ClientConfigurationFactory.getConfiguration().getBaseConfiguration()));
            initiated = true;
        }
        indicator.draw(graphics, x, y);
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
        initiated = false;
    }

}
