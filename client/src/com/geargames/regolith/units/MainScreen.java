package com.geargames.regolith.units;

import com.geargames.awt.Screen;
import com.geargames.common.Graphics;
import com.geargames.common.env.Environment;
import com.geargames.regolith.application.Graph;

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
    public void onShow() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onHide() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
