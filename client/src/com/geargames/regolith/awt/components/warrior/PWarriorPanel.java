package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PRadioGroup;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.awt.components.menues.BagVerticalTackles;
import com.geargames.regolith.awt.components.menues.StoreHouseVerticalTackles;
import com.geargames.regolith.awt.components.menues.WarriorVerticalTackles;
import com.geargames.regolith.units.Account;

/**
 * User: mikhail v. kutuzov
 * Панель бойца. Отображение основных характеристик, содержимого сумки и склада пользователя.
 */
public class PWarriorPanel extends PRootContentPanel {
    private PWarriorCharacteristics characteristics;

    private BagVerticalTackles bagTacklesElement;
    private WarriorVerticalTackles warriorTacklesElement;
    private StoreHouseVerticalTackles storeTacklesElement;
    private PRadioGroup group;

    public PWarriorPanel(PObject prototype) {
        super(prototype);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                IndexObject view = (IndexObject) prototype.getIndexBySlot(53);

                Account account = ClientConfigurationFactory.getConfiguration().getAccount();
                PObject object = (PObject) view.getPrototype();
                bagTacklesElement = new BagVerticalTackles(object);
                warriorTacklesElement = new WarriorVerticalTackles(object);
                storeTacklesElement = new StoreHouseVerticalTackles(object, account.getBase().getStoreHouse());

                characteristics = new PWarriorCharacteristics((PObject) index.getPrototype(), this);
                characteristics.reset();
                addActiveChild(characteristics, index.getX(), index.getY());
                addActiveChild(bagTacklesElement, view.getX(), view.getY());
                addActiveChild(warriorTacklesElement, view.getX(), view.getY());
                addActiveChild(storeTacklesElement, view.getX(), view.getY());
                break;
            case 52:
                if(group == null){
                    group = new PRadioGroup(3);
                }
                PStoreHouseButton houseButton = new PStoreHouseButton((PObject) index.getPrototype(), this);
                addActiveChild(houseButton, index.getX(), index.getY());
                group.addButton(houseButton);
                break;
            case 51:
                if(group == null){
                    group = new PRadioGroup(3);
                }
                PBagButton bagButton = new PBagButton((PObject) index.getPrototype(), this);
                addActiveChild(bagButton, index.getX(), index.getY());
                group.addButton(bagButton);
                break;
            case 50:
                if(group == null){
                    group = new PRadioGroup(3);
                }
                PWarriorButton warriorButton = new PWarriorButton((PObject) index.getPrototype(), this);
                addActiveChild(warriorButton, index.getX(), index.getY());
                group.addButton(warriorButton);
                break;
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

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

}
