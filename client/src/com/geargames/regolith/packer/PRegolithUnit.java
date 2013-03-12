package com.geargames.regolith.packer;

import com.geargames.common.Graphics;
import com.geargames.common.Image;
import com.geargames.common.packer.PUnit;
import com.geargames.regolith.units.Human;

/**
 * User: mkutuzov
 * Date: 06.03.13
 */
public class PRegolithUnit extends PUnit {
    private Image head;
    private Image body;
    private Image legs;

    public PRegolithUnit(int prototypesCount) {
        super(prototypesCount);
    }

    public void draw(Graphics graphics, int x, int y, Object unit) {
        Human human = (Human) unit;
        int headId = human.getHeadArmor().getArmorType().getFrameId();
        int bodyId = human.getTorsoArmor().getArmorType().getFrameId();
        int legsId = human.getLegsArmor().getArmorType().getFrameId();



    }

}
