package com.geargames.regolith.units.map;

import com.geargames.common.Graphics;
import com.geargames.common.env.Environment;
import com.geargames.common.packer.PObject;

/**
 * Клиентская реализация класса ящика с аммуницией.
 * User: abarakov
 * Date: 04.05.13
 */
public class ClientBox extends Box implements DrawableElement {
    private PObject obj;

    @Override
    public int getFrameId() {
        return obj.getPID();
    }

    @Override
    public void setFrameId(int unitFrameId) {
        obj = Environment.getRender().getObject(unitFrameId);
    }

    @Override
    public void draw(Graphics graphics, int x, int y) {
        obj.draw(graphics, x, y);
    }

}
