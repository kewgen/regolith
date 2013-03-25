package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PContentPanel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Панель на которой отображены текущие битвы + кнопка [создать битву].
 */
public class PBattlesPanel extends PContentPanel {

    public PBattlesPanel(PObject prototype) {
        super(prototype);
    }

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                addActiveChild(new PBattlesList((PObject)index.getPrototype()), index);
                break;
            case 1:
                addActiveChild(new PBattleCreateButton((PObject)index.getPrototype()), index);
                break;
        }
    }

}
