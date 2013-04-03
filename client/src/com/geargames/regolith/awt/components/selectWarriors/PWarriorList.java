package com.geargames.regolith.awt.components.selectWarriors;

import com.geargames.awt.components.PHorizontalScrollView;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;
import com.geargames.common.packer.PSprite;
import com.geargames.common.util.Math;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.WarriorCollection;

import java.util.Vector;

/**
 * User: abarakov
 * Date: 29.03.13
 * Горизонтальный список бойцов.
 */
public class PWarriorList extends PHorizontalScrollView {
    private Vector warriorItems;
    private int warriorAmount;
    private PObject buttonObject;

    public PWarriorList(PObject prototype) {
        super(prototype);
        Index frameButtonIndex = prototype.getIndexBySlot(0);
        buttonObject = (PObject) frameButtonIndex.getPrototype();
        warriorItems = new Vector(16);
    }

    public void setWarriorList(WarriorCollection collection) {
        warriorAmount = collection.size();
        int oldSize = warriorItems.size();
        warriorItems.ensureCapacity(warriorAmount);

        // Сначало изменяем уже существующие элементы списка
        int minSize = Math.min(oldSize, warriorAmount);
        for (int i = 0; i < minSize; i++) {
            Warrior warrior = collection.get(i);
            PWarriorListItem item = (PWarriorListItem) warriorItems.get(i);
            item.setWarrior(warrior);
        }

        // Если список карт увеличился - создаем новые элементы
        for (int i = minSize; i < collection.size(); i++) {
            Warrior map = collection.get(i);
            PWarriorListItem item = new PWarriorListItem(buttonObject);
            warriorItems.add(item);
            item.setWarrior(map);
            item.setWarriorAvatar((PSprite) buttonObject.getIndexBySlot(0).getPrototype());
        }
    }

    @Override
    public Vector getItems() {
        return warriorItems;
    }

    @Override
    public int getItemsAmount() {
        return warriorAmount; // getItems().size();
    }

    public PWarriorListItem getItem(int index) {
        return (PWarriorListItem) warriorItems.get(index);
    }

}
