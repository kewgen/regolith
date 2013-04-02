package com.geargames.regolith.awt.components.battles;

import com.geargames.common.logging.Debug;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.application.ObjectManager;
import com.geargames.regolith.units.dictionaries.ClientBattleCollection;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Список битв.
 */
public class PBattleListItemVector extends Vector {
    private ClientBattleCollection battles;

    private ArrayList items;
    private int amount;
    private int downIndex;
    private int upIndex;

    public PBattleListItemVector(PObject itemPrototype, int shownAmount) {
        items = new ArrayList(shownAmount + 1);
        battles = ObjectManager.getInstance().getBattleCollection();

        this.amount = shownAmount;
        for (int i = 0; i <= shownAmount; i++) {
            PBattleListItem item = new PBattleListItem(itemPrototype);
            items.add(item);
            setBattleIntoPanel(item, i);
        }
        upIndex = 0;
        downIndex = shownAmount;
    }

    private void setBattleIntoPanel(PBattleListItem panel, int index) {
        if (index < battles.size()) {
            panel.setBattle(battles.get(index));
        } else {
            Debug.error("A NOT EXISTED BATTLE HAS BEEN RETRIEVED");
        }
    }

    private PBattleListItem getPresentedItem(int index) {
        if (index >= upIndex && index <= downIndex) {
            return (PBattleListItem) items.get(index - upIndex);
        }
        return null;
    }

    private PBattleListItem down() {
        upIndex++;
        downIndex++;
        PBattleListItem item = (PBattleListItem) items.remove(0);
        setBattleIntoPanel(item, downIndex);
        items.add(item);
        return item;
    }

    private PBattleListItem up() {
        upIndex--;
        downIndex--;
        PBattleListItem item = (PBattleListItem) items.remove(amount);
        setBattleIntoPanel(item, upIndex);
        items.add(0, item);
        return item;
    }

    public Object elementAt(int index) {
        PBattleListItem item = getPresentedItem(index);
        if (item == null) {
            if (index == downIndex + 1) {
                return down();
            } else {
                return up();
            }
        } else {
            setBattleIntoPanel(item, index);
            return item;
        }
    }

    public int size() {
        return battles.size();
    }

}
