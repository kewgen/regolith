package com.geargames.regolith.awt.components.battles;

import com.geargames.common.logging.Debug;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;
//import com.geargames.common.util.Math;
import com.geargames.regolith.ClientConfigurationFactory;
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

    private ArrayList items;
    private int amount;
    private int size;
    private int upIndex;

    public PBattleListItemVector(PObject itemPrototype, int shownAmount) {
        items = new ArrayList(shownAmount + 1);
        battles = ObjectManager.getInstance().getBattleCollection();

        amount = shownAmount;
        for (int i = 0; i < shownAmount + 1; i++) {
            PBattleListItem item = new PBattleListItem(itemPrototype);
            items.add(item);
        }
        size = 0;
        upIndex = 0;
        update();
    }

    public Object elementAt(int index) {
        PBattleListItem item = null;
        if (index < upIndex) {
            int difference = upIndex - index;
            for (int i = difference; i > 0; i--) {
                item = (PBattleListItem) items.remove(amount);
                items.add(0, item);
                item.updateBattle(battles.get(upIndex - i));
            }
            upIndex -= difference;
        } else {
            if (index >= upIndex + amount) {
                int difference = index - (upIndex + amount);
                for (int i = 0; i < difference; i++) {
                    item = (PBattleListItem) items.remove(0);
                    items.add(item);
                    item.updateBattle(battles.get(upIndex + i));
                }
                upIndex += difference;
            } else {
                item = (PBattleListItem) items.get(index - upIndex);
            }
        }
        return item;
    }

    public int size() {
        return size;
    }

    public void update() {
        Debug.debug("PBattleListItemVector.update: size=" + battles.size());
        size = battles.size();
        int iItem = 0;
        int iBattle = upIndex;
        Battle listenedBattle = ClientConfigurationFactory.getConfiguration().getBattle();
        if (listenedBattle != null) {
            PBattleListItem item = (PBattleListItem) items.get(0);
            item.updateBattle(listenedBattle);
            iItem++;
        }
        while (iItem < items.size() && iBattle < battles.size()) {
            Battle battle = battles.get(iBattle);
            if (listenedBattle == null || battle.getId() != listenedBattle.getId()) { //todo: id или ссылки на объекты?
                PBattleListItem item = (PBattleListItem) items.get(iItem);
                item.updateBattle(battle);
                iItem++;
            }
            iBattle++;
        }
    }

}
