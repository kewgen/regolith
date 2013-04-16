package com.geargames.regolith.awt.components.common;

import com.geargames.awt.components.PLabel;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRootContentPanel;

/**
 * User: m/v/kutuzov
 * Date: 16.04.13
 * Time: 9:49
 */
public abstract class PWaitingWindow extends PRootContentPanel {
    private PLabel messageLabel;

    public PWaitingWindow(PObject prototype) {
        super(prototype);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject parentPrototype) {
        switch (index.getSlot()){
            case 109:
                messageLabel = new PSimpleLabel(index);
                addPassiveChild(messageLabel, index);
                break;
        }
    }

    public PLabel getMessageLabel() {
        return messageLabel;
    }
}
