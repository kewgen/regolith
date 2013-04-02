package com.geargames.regolith.awt.components.menues;

import com.geargames.awt.components.PVerticalScrollView;
import com.geargames.common.Graphics;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Предок всех вертикальных списков клади и расходников.
 */
public abstract class VerticalTackles extends PVerticalScrollView {

    public abstract void initiateMotionListener();

    protected VerticalTackles(PObject prototype) {
        super(prototype);
    }

    public void initiate(Graphics graphics) {
        initiateMotionListener();
        setInitiated(true);
    }

}
