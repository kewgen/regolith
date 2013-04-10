package com.geargames.regolith.awt.components.playerInfo;

import com.geargames.awt.components.PHorizontalScrollView;
import com.geargames.awt.utils.ScrollHelper;
import com.geargames.awt.utils.motions.ElasticInertMotionListener;
import com.geargames.awt.utils.motions.StubMotionListener;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;
import com.geargames.common.packer.PSprite;
import com.geargames.common.util.Mathematics;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.WarriorCollection;

import java.util.Vector;

/**
 * User: abarakov
 * Date: 10.04.13
 * Горизонтальный список бойцов.
 */
public class PAwardList extends PHorizontalScrollView {
    private Vector warriorItems;
    private int warriorAmount;
    private PObject buttonObject;
    private ElasticInertMotionListener inertMotionListener;
    private StubMotionListener stubMotionListener;


    public PAwardList(PObject prototype) {
        super(prototype);
        Index frameButtonIndex = prototype.getIndexBySlot(0);
        buttonObject = (PObject) frameButtonIndex.getPrototype();
        warriorItems = new Vector(16);
        inertMotionListener = new ElasticInertMotionListener();
        stubMotionListener = new StubMotionListener();
        initiateMotionListener();
    }

    private void initiateMotionListener() {
        setMotionListener(ScrollHelper.adjustHorzCenteredListElasticInertMotionListener(
                inertMotionListener, stubMotionListener, this));
    }

    public void setWarriorList(WarriorCollection collection) {
        warriorAmount = collection.size();
        int oldSize = warriorItems.size();
        warriorItems.ensureCapacity(warriorAmount);

        // Сначало изменяем уже существующие элементы списка
        int minSize = Mathematics.min(oldSize, warriorAmount);
        for (int i = 0; i < minSize; i++) {
            Warrior warrior = collection.get(i);
            PAwardListItem item = (PAwardListItem) warriorItems.get(i);
            item.setWarrior(warrior);
        }

        // Если список карт увеличился - создаем новые элементы
        for (int i = minSize; i < collection.size(); i++) {
            Warrior map = collection.get(i);
            PAwardListItem item = new PAwardListItem(buttonObject);
            warriorItems.add(item);
            item.setWarrior(map);
            item.setWarriorAvatar((PSprite) buttonObject.getIndexBySlot(0).getPrototype());
        }
        initiateMotionListener();
    }

    @Override
    public Vector getItems() {
        return warriorItems;
    }

    @Override
    public int getItemsAmount() {
        return warriorAmount; // getItems().size();
    }

    public PAwardListItem getItem(int index) {
        return (PAwardListItem) warriorItems.get(index);
    }

}
