package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PContentPanel;
import com.geargames.awt.components.PElement;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Начальная панель.
 */
public class PMainMenuPanel extends PContentPanel {

    public PMainMenuPanel(PObject object) {
        super(object);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        PElement element = null;
        switch (index.getSlot()) {
            case 10:
                element = new PBattleButton((PObject) index.getPrototype());
                break;
            case 11:
                element = new PMerсenaryButton((PObject) index.getPrototype());
                break;
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
                element = new PShopButton((PObject) index.getPrototype());
                break;
        }
        if (element != null) {
            addActiveChild(element, index);
        }
    }
}
