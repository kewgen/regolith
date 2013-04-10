package com.geargames.regolith.awt.components;

import com.geargames.awt.Anchors;
import com.geargames.awt.DrawablePPanel;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 25.12.12
 */
public class DefaultDrawablePPanel extends DrawablePPanel {

    public DefaultDrawablePPanel() {
        setAnchor(Anchors.CENTER_ANCHOR);
    }

    @Override
    public void onHide() {
        ((PRootContentPanel) getElement()).onHide();
    }

    @Override
    public void onShow() {
        ((PRootContentPanel) getElement()).onShow();
    }

    @Override
    public byte getLayer() {
        return MIDDLE_LAYER;
    }

}
