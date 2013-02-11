package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PContentPanel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;

/**
 * User: mikhail v. kutuzov
 * Панель информации о бойце(перед выбором\выкидыванием его в\из отряд(а)).
 */
public class PWarriorInformation extends PContentPanel {

    public PWarriorInformation(PObject prototype) {
        super(prototype);

        ArrayList indexes = prototype.getIndexes();
        for (int i = 0; i < indexes.size(); i++) {
            IndexObject index = (IndexObject) indexes.get(i);
            if (index.isSlot()) {
                switch (index.getSlot()) {
                    case 0:

                        break;
                    case 1:
                        break;
                }
            }
        }
    }
}
