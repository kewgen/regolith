package com.geargames.regolith.awt.components.main;

import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.DefaultPContentPanel;

/**
 * User: mikhail v. kutuzov
 * Панелька висит в левом верхнем углу, на ней примостилась кнопка перехода "назад".
 */
public class PLeftPanel extends DefaultPContentPanel {
    private PBackButton button;

    public PLeftPanel(PObject prototype) {
        super(prototype);
    }

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()){
            case 1:
                button = new PBackButton((PObject)index.getPrototype());
                addActiveChild(button, index);
                break;
        }
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

}
