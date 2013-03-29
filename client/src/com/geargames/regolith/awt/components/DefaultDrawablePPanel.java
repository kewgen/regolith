package com.geargames.regolith.awt.components;

import com.geargames.awt.Anchors;
import com.geargames.awt.DrawablePPanel;

/**
 * User: mikhail v. kutuzov
 * Date: 25.12.12
 */
public class DefaultDrawablePPanel extends DrawablePPanel {

    public DefaultDrawablePPanel() {
        setAnchor(Anchors.CENTER_ANCHOR);
    }

    @Override
    public void onHide() {
        ((DefaultPContentPanel) getElement()).onHide();
    }

    public void onShow() {
        ((DefaultPContentPanel) getElement()).onShow();
    }

}
