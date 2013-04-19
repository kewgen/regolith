package com.geargames.regolith.units;

import com.geargames.awt.Screen;
import com.geargames.common.Graphics;
import com.geargames.common.env.Environment;
import com.geargames.regolith.Graph;

/**
 * User: mkutuzov
 * Date: 10.05.12
 */
public class MainScreen extends Screen {

    @Override
    public void draw(Graphics graphics) {
        Environment.getRender().getSprite(Graph.SPR_PICTURE_BACK).draw(graphics, getX(), getY());
    }

    @Override
    public boolean onEvent(int code, int param, int x, int y) {
        return false;
    }

    @Override
    public void onShow() {
    }

    @Override
    public void onHide() {
    }
}
