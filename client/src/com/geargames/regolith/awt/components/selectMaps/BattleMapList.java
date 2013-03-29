package com.geargames.regolith.awt.components.selectMaps;

import com.geargames.awt.components.PHorizontalScrollView;
import com.geargames.awt.components.PRadioGroup;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.PFrame;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.common.util.Math;
import java.util.Vector;

/**
 * User: abarakov
 * Date: 28.03.13
 * Горизонтальный список игровых карт.
 */
public class BattleMapList extends PHorizontalScrollView {
    private Vector mapList;
    private int mapAmount;
    private PObject buttonObject;
    private PRadioGroup radioGroup;

    public BattleMapList(PObject prototype) {
        super(prototype);
        Index frameButtonIndex = prototype.getIndexBySlot(0);
        buttonObject = (PObject) frameButtonIndex.getPrototype();
        mapList = new Vector(16);
        radioGroup = new PRadioGroup(16);
    }

    public void setMapList(BattleMap[] collection) {
        mapAmount = collection.length;
        int oldSize = mapList.size();
        mapList.ensureCapacity(mapAmount);

        // Сначало изменяем уже существующие элементы списка
        int minSize = Math.min(oldSize, mapAmount);
        for (int i = 0; i < minSize; i++) {
            BattleMap map = collection[i];
            BattleMapListItem item = (BattleMapListItem) mapList.get(i);
            item.setMap(map);
        }

        // Если список карт увеличился - создаем новые элементы
        for (int i = minSize; i < collection.length; i++) {
            BattleMap map = collection[i];
            BattleMapListItem item = new BattleMapListItem(buttonObject);
            mapList.add(item);
            radioGroup.addButton(item);
            item.setMap(map);
            item.setMapPreview((PFrame) buttonObject.getIndexBySlot(0).getPrototype());
        }
    }

    @Override
    public Vector getItems() {
        return mapList;
    }

    @Override
    public int getItemsAmount() {
        return mapAmount; // getItems().size();
    }

    public BattleMapListItem getItem(int index) {
        return (BattleMapListItem) mapList.get(index);
    }

}
