package com.geargames.regolith.awt.components.menues;

import com.geargames.awt.components.PPrototypeElement;
import com.geargames.common.Render;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.WarriorCollection;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Реализация вектора элементов элементов HorizontalFaceItem.
 * Реализация не поддерживает изменение содержимого вектора.
 */
public class HorizontalFaceItemsVector extends Vector {
    private WarriorCollection warriors;
    private Render render;
    private Warrior warrior;
    private HorizontalFaceItem item;

    public HorizontalFaceItemsVector(WarriorCollection warriors, Render render, HorizontalFaceItem item) {
        super(0);
        this.warriors = warriors;
        this.item = item;
        this.render = render;
    }

    public Object elementAt(int index) {
        Warrior tmp = warriors.get(index);
        if(tmp != warrior) {
            item.setPrototype(render.getSprite(tmp.getFrameId()));
            warrior = tmp;
            item.setWarrior(warrior);
        }
        return item;
    }

    public int size() {
        return warriors.size();
    }
}
