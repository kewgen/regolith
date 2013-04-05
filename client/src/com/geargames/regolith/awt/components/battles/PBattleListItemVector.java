package com.geargames.regolith.awt.components.battles;

import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.application.ObjectManager;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.dictionaries.ClientBattleCollection;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov, abarakov
 * Список битв.
 */
public class PBattleListItemVector extends Vector {
    private ClientBattleCollection battles;
    private Battle listenedBattle;

    private ArrayList items;
    private int amount;
    private int size;
//    private int downIndex;
    private int upIndex;

    public PBattleListItemVector(PObject itemPrototype, int shownAmount) {
        items = new ArrayList(shownAmount + 1);
        battles = ObjectManager.getInstance().getBattleCollection();

        amount = shownAmount;
        for (int i = 0; i < shownAmount + 1; i++) {
            PBattleListItem item = new PBattleListItem(itemPrototype);
            items.add(item);
//            setBattleIntoPanel(item, i);
        }
        size = 0;
        upIndex = 0;
//        downIndex = Math.min(shownAmount, battles.size()) - 1; //to do: значение -1?
        update();
    }

//    private void setBattleIntoPanel(PBattleListItem panel, int index) {
//        if (index < battles.size()) {
//            panel.setBattle(battles.get(index));
//        } else {
//            Debug.error("A NOT EXISTED BATTLE HAS BEEN RETRIEVED");
//        }
//    }
//
//    private PBattleListItem getPresentedItem(int index) {
//        if (index >= upIndex && index <= downIndex) {
//            return (PBattleListItem) items.get(index - upIndex);
//        }
//        return null;
//    }
//
//    private PBattleListItem up() {
//        upIndex--;
//        downIndex--;
//        PBattleListItem item = (PBattleListItem) items.remove(amount);
//        setBattleIntoPanel(item, upIndex);
//        items.add(0, item);
//        return item;
//    }
//
//    private PBattleListItem down() {
//        upIndex++;
//        downIndex++;
//        PBattleListItem item = (PBattleListItem) items.remove(0);
//        setBattleIntoPanel(item, downIndex);
//        items.add(item);
//        return item;
//    }

    public Object elementAt(int index) {
        PBattleListItem item;
        if (index < upIndex) { //todo: а если upIndex - index > 1 ?
            // up
            upIndex--;
            item = (PBattleListItem) items.remove(amount);
            items.add(0, item);
            item.updateBattle(battles.get(upIndex)); //todo: а если battles.get(upIndex) == listenedBattle?
        } else
        if (index >= upIndex + amount) { //todo: а если index - (upIndex + amount > 1 ?
            // down
            upIndex++;
            item = (PBattleListItem) items.remove(0);
            items.add(item);
            item.updateBattle(battles.get(upIndex)); //todo: а если battles.get(upIndex) == listenedBattle?
        } else {
            item = (PBattleListItem) items.get(index - upIndex);
        }
        return item;
    }

    public int size() {
        return size;
    }

    public Battle getListenedBattle() {
        return listenedBattle;
    }

    public void setListenedBattle(Battle battle) {
        listenedBattle = battle;
        update();
    }

    public void update() {
        size = battles.size();
        int iItem = 0;
        int iBattle = upIndex;
        if (listenedBattle != null) {
            PBattleListItem item = (PBattleListItem) items.get(0);
            item.updateBattle(listenedBattle);
            iItem++;
        }
        while (iItem < items.size() && iBattle < battles.size()) {
            Battle battle = battles.get(iBattle);
            if (battle != listenedBattle) { //todo: id или ссылки на объекты?
                PBattleListItem item = (PBattleListItem) items.get(iItem);
                item.updateBattle(battle);
                iItem++;
            }
            iBattle++;
        }
    }

}
