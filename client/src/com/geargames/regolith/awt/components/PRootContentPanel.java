package com.geargames.regolith.awt.components;

import com.geargames.awt.components.PContentPanel;
import com.geargames.common.packer.PObject;

/**
 * User: abarakov
 * Date: 29.03.13
 */
public abstract class PRootContentPanel extends PContentPanel {

    public PRootContentPanel(PObject prototype) {
        super(prototype);
    }

    public abstract void onHide();

    public abstract void onShow();

}