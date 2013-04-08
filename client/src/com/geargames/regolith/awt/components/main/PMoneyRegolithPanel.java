package com.geargames.regolith.awt.components.main;

import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.awt.components.common.PShowingModalLabel;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 11.01.13
 * Time: 11:39
 */
public class PMoneyRegolithPanel extends PRootContentPanel {
    private PShowingModalLabel moneyLabel;
    private PShowingModalLabel regolithLabel;

    public PMoneyRegolithPanel(PObject prototype) {
        super(prototype);
    }

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                createDefaultElementByIndex(index, prototype);
                break;
            case 1:
                moneyLabel = new PShowingModalLabel((PObject)index.getPrototype());
                moneyLabel.setText("0");
                addActiveChild(moneyLabel, index);
                break;
            case 2:
                regolithLabel = new PShowingModalLabel((PObject)index.getPrototype());
                regolithLabel.setText("0");
                addActiveChild(regolithLabel, index);
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
