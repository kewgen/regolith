package com.geargames.regolith.awt.components.battles;

import com.geargames.Debug;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.dictionaries.WarriorCollection;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Вектор маскирующий в себе конечный набор панелей - морд бойцов пользователя.
 * Пользователь кликает на морды и с помощю всплывающего окошка либо изменяет их состояние(боец попадает\покидает
 * боевую группу) , либо оставляет как есть.
 */
public class PHorizontalAccountWarriorItemVector extends Vector {
    private WarriorCollection warriors;
    private ClientWarriorCollection selected;

    private ArrayList items;
    private int amount;
    private int downIndex;
    private int upIndex;

    public PHorizontalAccountWarriorItemVector(PObject itemPrototype, int shownAmount) {
        warriors = ClientConfigurationFactory.getConfiguration().getAccount().getWarriors();
        selected = new ClientWarriorCollection(new Vector());

        amount = shownAmount;
        downIndex = shownAmount;
        upIndex = 0;

        items = new ArrayList(shownAmount + 1);
        for (int i = 0; i <= shownAmount; i++) {
            PHorizontalAccountWarriorItem item = new PHorizontalAccountWarriorItem(itemPrototype, selected);
            items.add(item);
            setBattleIntoPanel(item, i);
        }

    }


    private void setBattleIntoPanel(PHorizontalAccountWarriorItem panel, int index) {
        if (index < warriors.size()) {
            panel.setWarrior(warriors.get(index));
        } else {
            Debug.log(com.geargames.common.String.valueOfC("A NOT EXISTED BATTLE HAS BEEN RETRIEVED"));
        }
    }

    private PHorizontalAccountWarriorItem getPresentedItem(int index) {
        if (index >= upIndex && index <= downIndex) {
            return (PHorizontalAccountWarriorItem) items.get(index - upIndex);
        }
        return null;
    }

    private PHorizontalAccountWarriorItem down() {
        upIndex++;
        downIndex++;
        PHorizontalAccountWarriorItem item = (PHorizontalAccountWarriorItem) items.remove(0);
        setBattleIntoPanel(item, downIndex);
        items.add(item);
        return item;
    }

    private PHorizontalAccountWarriorItem up() {
        upIndex--;
        downIndex--;
        PHorizontalAccountWarriorItem item = (PHorizontalAccountWarriorItem) items.remove(amount);
        setBattleIntoPanel(item, upIndex);
        items.add(0, item);
        return item;
    }

    public Object elementAt(int index) {
        PHorizontalAccountWarriorItem item = getPresentedItem(index);
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
        return warriors.size();
    }


}
