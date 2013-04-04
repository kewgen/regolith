package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PContentPanel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;

/**
 * @author Mikhail_Kutuzov
 *         created: 01.04.13  21:49
 */
public class PPlayerPanel extends PContentPanel {

    public PPlayerPanel(PObject prototype) {
        super(prototype);
    }

    protected void createSlotElementByIndex(IndexObject index, PObject parentPrototype) {
        if (index.getSlot() != 110) {
            addActiveChild(new PPlayerButton((PObject)index.getPrototype()), index);
        }
    }

    public PPlayerButton getPlayerButton(int index){
        return (PPlayerButton)getActiveChildren().get(index);
    }

}
