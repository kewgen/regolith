package com.geargames.regolith.packer;

import com.geargames.common.Graphics;
import com.geargames.common.packer.PUnit;

/**
 * User: mikhail v. kutuzov
 * Затычка для юнита.
 */
public class PUnitStub extends PUnit {

    public PUnitStub(int prototypesCount) {
        super(prototypesCount);
    }

    public void draw(Graphics graphics, int x, int y, Object unit) {
    }
}
