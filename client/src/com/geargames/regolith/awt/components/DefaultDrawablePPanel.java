package com.geargames.regolith.awt.components;

import com.geargames.awt.Anchors;
import com.geargames.awt.DrawablePPanel;
import com.geargames.common.Event;
import com.geargames.common.util.Region;

/**
 * User: abarakov
 * Date: 10.04.13
 */
public abstract class DefaultDrawablePPanel extends DrawablePPanel {
    private boolean modalAutoClose;

    public DefaultDrawablePPanel() {
        modalAutoClose = false;
        setAnchor(Anchors.CENTER_ANCHOR);
    }

    public boolean getModalAutoClose() {
        return modalAutoClose;
    }

    public void setModalAutoClose(boolean modalAutoClose) {
        this.modalAutoClose = modalAutoClose;
    }

    public void show() {
        PRegolithPanelManager.getInstance().show(this);
    }

    public void showModal() {
        PRegolithPanelManager.getInstance().showModal(this);
    }

    public void hide() {
        PRegolithPanelManager.getInstance().hide(this);
    }

    @Override
    public void onShow() {
        ((PRootContentPanel) getElement()).onShow();
    }

    @Override
    public void onHide() {
        ((PRootContentPanel) getElement()).onHide();
    }

//    // onAutoClose, onAutoHide
//    public void onCancel() {
//        ((PRootContentPanel) getElement()).onCancel();
//    }

    @Override
    public boolean onModalEvent(int code, int param, int xTouch, int yTouch) {
        if (modalAutoClose && code == Event.EVENT_TOUCH_PRESSED) {
            Region region = getElement().getTouchRegion();
            int xLocal = xTouch - getX() + region.getMinX();
            int yLocal = yTouch - getY() + region.getMinY();
            if (!region.isWithIn(xLocal, yLocal)) {
                PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
//                onCancel();
                panelManager.hide(this);
                return true;
            }
        }
        return super.onEvent(code, param, xTouch, yTouch);
    }

}
