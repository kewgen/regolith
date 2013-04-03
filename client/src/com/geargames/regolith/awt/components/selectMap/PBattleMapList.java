package com.geargames.regolith.awt.components.selectMap;

import com.geargames.awt.components.PHorizontalScrollView;
import com.geargames.awt.components.PRadioGroup;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.common.util.Math;
import java.util.Vector;

/**
 * User: abarakov
 * Date: 28.03.13
 * Горизонтальный список игровых карт.
 */
public class PBattleMapList extends PHorizontalScrollView {
    private Vector mapItems;
    private int mapAmount;
    private PObject buttonObject;
    private PRadioGroup radioGroup;

    public PBattleMapList(PObject prototype) {
        super(prototype);
        Index frameButtonIndex = prototype.getIndexBySlot(0);
        buttonObject = (PObject) frameButtonIndex.getPrototype();
        mapItems = new Vector(16);
        radioGroup = new PRadioGroup(16);
    }

    public void setMapItems(BattleMap[] collection) {
        mapAmount = collection.length;
        int oldSize = mapItems.size();
        mapItems.ensureCapacity(mapAmount);

        // Сначало изменяем уже существующие элементы списка
        int minSize = Math.min(oldSize, mapAmount);
        for (int i = 0; i < minSize; i++) {
            BattleMap map = collection[i];
            PBattleMapListItem item = (PBattleMapListItem) mapItems.get(i);
            item.setMap(map);
        }

        // Если список карт увеличился - создаем новые элементы
        for (int i = minSize; i < collection.length; i++) {
            BattleMap map = collection[i];
            PBattleMapListItem item = new PBattleMapListItem(buttonObject);
            mapItems.add(item);
            radioGroup.addButton(item);
            item.setMap(map);
//            item.setMapPreview((PSprite) buttonObject.getIndexBySlot(0).getPrototype());
        }
    }

    @Override
    public Vector getItems() {
        return mapItems;
    }

    @Override
    public int getItemsAmount() {
        return mapAmount; // getItems().size();
    }

    public PBattleMapListItem getItem(int index) {
        return (PBattleMapListItem) mapItems.get(index);
    }

}
