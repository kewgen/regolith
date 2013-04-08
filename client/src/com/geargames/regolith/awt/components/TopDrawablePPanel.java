package com.geargames.regolith.awt.components;

import com.geargames.awt.Anchors;
import com.geargames.awt.DrawablePPanel;

/**
 * Предок всех панелек, которые отображаются поверх всех остальных панелек.
 * User: abarakov
 * Date: 25.12.12
 */
public class TopDrawablePPanel extends DrawablePPanel {

    public TopDrawablePPanel() {
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
        return TOP_LAYER;
    }

}
