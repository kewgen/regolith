package com.geargames.regolith.awt.components.playerInfo;

import com.geargames.awt.components.PHorizontalScrollView;
import com.geargames.awt.utils.ScrollHelper;
import com.geargames.awt.utils.motions.ElasticInertMotionListener;
import com.geargames.awt.utils.motions.StubMotionListener;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;
import com.geargames.common.packer.PSprite;
import com.geargames.common.util.Mathematics;

import java.util.Vector;

/**
 * User: abarakov
 * Date: 10.04.13
 * Горизонтальный список наград игрока.
 */
public class PAwardList extends PHorizontalScrollView {
    private Vector awardItems;
    private int awardAmount;
    private PObject buttonObject;
    private ElasticInertMotionListener inertMotionListener;
    private StubMotionListener stubMotionListener;


    public PAwardList(PObject prototype) {
        super(prototype);
        Index frameButtonIndex = prototype.getIndexBySlot(0);
        buttonObject = (PObject) frameButtonIndex.getPrototype();
        awardItems = new Vector(16);
        inertMotionListener = new ElasticInertMotionListener();
        stubMotionListener = new StubMotionListener();
        initiateMotionListener();
    }

    private void initiateMotionListener() {
        setMotionListener(ScrollHelper.adjustHorzCenteredListElasticInertMotionListener(
                inertMotionListener, stubMotionListener, this));
    }

//    public void setAwardList(AwardCollection collection) {
//        awardAmount = collection.size();
//        int oldSize = awardItems.size();
//        awardItems.ensureCapacity(awardAmount);
//
//        // Сначало изменяем уже существующие элементы списка
//        int minSize = Mathematics.min(oldSize, awardAmount);
//        for (int i = 0; i < minSize; i++) {
//            Award award = collection.get(i);
//            PAwardListItem item = (PAwardListItem) awardItems.get(i);
//            item.setAward(award);
//        }
//
//        // Если список наград увеличился - создаем новые элементы
//        for (int i = minSize; i < collection.size(); i++) {
//            Award award = collection.get(i);
//            PAwardListItem item = new PAwardListItem(buttonObject);
//            awardItems.add(item);
//            item.setAward(award);
//            item.setAwardIcon((PSprite) buttonObject.getIndexBySlot(0).getPrototype());
//        }
//        initiateMotionListener();
//    }

    @Override
    public Vector getItems() {
        return awardItems;
    }

    @Override
    public int getItemsAmount() {
        return awardAmount; // getItems().size();
    }

    public PAwardListItem getItem(int index) {
        return (PAwardListItem) awardItems.get(index);
    }

}
