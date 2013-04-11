package com.geargames.regolith.awt.components;

/**
 * Предок всех панелек, которые отображаются поверх всех остальных панелек.
 * User: abarakov
 * Date: 08.04.13
 */
public class TopDrawablePPanel extends DefaultDrawablePPanel {

    @Override
    public byte getLayer() {
        return TOP_LAYER;
    }

}
