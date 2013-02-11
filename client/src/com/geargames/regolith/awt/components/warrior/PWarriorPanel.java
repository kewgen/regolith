package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PContentPanel;
import com.geargames.awt.components.PRadioGroup;
import com.geargames.common.Render;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.awt.components.menues.BagVerticalTackles;
import com.geargames.regolith.awt.components.menues.StoreHouseVerticalTackles;
import com.geargames.regolith.awt.components.menues.WarriorVerticalTackles;
import com.geargames.regolith.units.Account;

/**
 * User: mikhail v. kutuzov
 * Панель бойца. Отображение основных характеристик, содержимого сумки и склада пользователя.
 */
public class PWarriorPanel extends PContentPanel {
    private PWarriorCharacteristics characteristics;

    private BagVerticalTackles bagTacklesElement;
    private WarriorVerticalTackles warriorTacklesElement;
    private StoreHouseVerticalTackles storeTacklesElement;

    public PWarriorPanel(PObject prototype, Render render, Account account) {
        super(prototype);

        ArrayList indexes = prototype.getIndexes();

        PRadioGroup group = new PRadioGroup(3);
        for (int i = 0; i < indexes.size(); i++) {
            IndexObject index = (IndexObject) indexes.get(i);
            if (index.isSlot()) {
                switch (index.getSlot()) {
                    case 0:
                        IndexObject three = (IndexObject) indexes.get(3);

                        PObject object = (PObject) three.getPrototype();
                        bagTacklesElement = new BagVerticalTackles(object);
                        warriorTacklesElement = new WarriorVerticalTackles(object);
                        storeTacklesElement = new StoreHouseVerticalTackles(object, account.getBase().getStoreHouse());

                        characteristics = new PWarriorCharacteristics((PObject) index.getPrototype(), render, account, this);
                        addActiveChild(characteristics, index.getX(), index.getY());
                        addActiveChild(bagTacklesElement, three.getX(), three.getY());
                        addActiveChild(warriorTacklesElement, three.getX(), three.getY());
                        addActiveChild(storeTacklesElement, three.getX(), three.getY());
                        break;
                    case 52:
                        PStoreHouseButton houseButton = new PStoreHouseButton((PObject) index.getPrototype(), this);
                        addActiveChild(houseButton, index.getX(), index.getY());
                        group.addButton(houseButton);
                        break;
                    case 51:
                        PBagButton bagButton = new PBagButton((PObject) index.getPrototype(), this);
                        addActiveChild(bagButton, index.getX(), index.getY());
                        group.addButton(bagButton);
                        break;
                    case 50:
                        PWarriorButton warriorButton = new PWarriorButton((PObject) index.getPrototype(), this);
                        addActiveChild(warriorButton, index.getX(), index.getY());
                        group.addButton(warriorButton);
                        break;
                }
            }
        }
    }

    public PWarriorCharacteristics getCharacteristics() {
        return characteristics;
    }

    public BagVerticalTackles getBagTacklesElement() {
        return bagTacklesElement;
    }

    public WarriorVerticalTackles getWarriorTacklesElement() {
        return warriorTacklesElement;
    }

    public StoreHouseVerticalTackles getStoreTacklesElement() {
        return storeTacklesElement;
    }
}
