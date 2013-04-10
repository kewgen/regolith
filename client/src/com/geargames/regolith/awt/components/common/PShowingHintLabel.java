package com.geargames.regolith.awt.components.common;

import com.geargames.awt.TextHint;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Компонент кликабельный лейбл, при щелчке по которому, отображается всплывающая подсказка.
 */
public class PShowingHintLabel extends PTouchLabel {
    private String hint;

    public PShowingHintLabel(PObject index) {
        super(index);
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void onClick() {
        if (hint != null && hint.length() > 0) {
            PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
            TextHint.show(hint, panelManager.getEventX(), panelManager.getEventY());
        }
    }

//    @Override
//    public boolean onEvent(int code, int param, int x, int y) {
//        if (code == Event.EVENT_TOUCH_PRESSED && hint != null && hint.length() > 0) {
//            PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
//            TextHint.show(hint, panelManager.getEventX(), panelManager.getEventY());
//        }
//        return super.onEvent(code, param, x, y);
//    }

}
