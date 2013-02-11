package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PContentPanel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.app.Render;

/**
 * User: mikhail v. kutuzov
 *
 */
public class PBattlesPanel extends PContentPanel {
    private PBattlesList list;

    public PBattlesPanel(PObject prototype) {
        super(prototype);

        ArrayList indexes = prototype.getIndexes();
        for (int i = 0; i < indexes.size(); i++) {
            IndexObject index = (IndexObject) indexes.get(i);
            if (index.isSlot()) {
                switch (index.getSlot()) {
                    case 0:
                        list = new PBattlesList((PObject)index.getPrototype());
                        addActiveChild(list, index);
                        break;
                    case 1:
                        addActiveChild(new PBattleCreateButton((PObject)index.getPrototype()), index);
                        break;
                }
            }
        }
    }

    public PBattlesList getList() {
        return list;
    }
}
