package com.geargames.regolith.awt.components.warrior.exchange.bag;

import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.warrior.exchange.PMedikitExchangePanel;

/**
 * User: mikhail v. kutuzov
 * Панель для перемещения аптечки из сумки.
 */
public class PMedikitFromBagPanel extends PMedikitExchangePanel {

    public PMedikitFromBagPanel(PObject prototype) {
        super(prototype);
    }

    protected void addButton1(PObject prototype, Index index) {
        PABag2StoreHouseButton button = new PABag2StoreHouseButton(prototype);
        button.setPanel(this);
        addActiveChild(button, index);
    }

    protected void addButton2(PObject prototype, Index index) {
    }
}
