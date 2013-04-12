package com.geargames.regolith.awt.components.menues;

import com.geargames.common.logging.Debug;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.awt.components.warrior.PAbstractTacklePanel;
import com.geargames.regolith.units.AmmunitionBag;
import com.geargames.regolith.units.AmmunitionPacket;
import com.geargames.regolith.units.Bag;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.Ammunition;
import com.geargames.regolith.units.tackle.StateTackle;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Набор элементов списка соответствующих содержимому сумок бойца.
 */
public class BagVerticalTackleItemsVector extends Vector {
    private Bag bag;
    private AmmunitionBag ammunitionBag;
    private int amount;

    private ArrayList items;
    private int downIndex;
    private int upIndex;

    public BagVerticalTackleItemsVector(int amount, PObject menuElement) {
        this.amount = amount;
        items = new ArrayList(amount + 1);
        upIndex = 0;
        downIndex = amount;
        for (int i = 0; i <= amount; i++) {
            items.add(new BagVerticalTackleItem(menuElement));
        }
    }

    public void reset(Warrior warrior) {
        bag = warrior.getBag();
        ammunitionBag = warrior.getAmmunitionBag();
        upIndex = 0;
        downIndex = amount;
        for (int i = 0; i < items.size(); i++) {
            setTackleIntoPanel((PAbstractTacklePanel) items.get(i), i);
        }
    }


    private void setTackleIntoPanel(PAbstractTacklePanel panel, int index) {
        if (index < bag.getTackles().size()) {
            StateTackle tackle = bag.getTackles().get(index);
            if (tackle != panel.getTackle()) {
                panel.setTackle(tackle, 1);
            }
        } else if (index - bag.getTackles().size() < ammunitionBag.getSize()) {
            index -= bag.getTackles().size();
            AmmunitionPacket packet = ammunitionBag.getPackets().get(index);
            Ammunition ammunition = packet.getAmmunition();
            int amount = packet.getCount();
            if (ammunition != panel.getTackle() || amount != panel.getAmount()) {
                panel.setTackle(packet.getAmmunition(), packet.getCount());
            }
        } else {
            Debug.warning("A not existed requisite has been retrieved");
        }
    }

    private BagVerticalTackleItem getPresentedItem(int index) {
        if (index >= upIndex && index <= downIndex) {
            return (BagVerticalTackleItem) items.get(index - upIndex);
        }
        return null;
    }

    private PAbstractTacklePanel down() {
        upIndex++;
        downIndex++;
        PAbstractTacklePanel item = (PAbstractTacklePanel) items.remove(0);
        setTackleIntoPanel(item, downIndex);
        items.add(item);
        return item;
    }

    private PAbstractTacklePanel up() {
        upIndex--;
        downIndex--;
        PAbstractTacklePanel item = (PAbstractTacklePanel) items.remove(amount);
        setTackleIntoPanel(item, upIndex);
        items.add(0, item);
        return item;
    }

    public Object elementAt(int index) {
        BagVerticalTackleItem item = getPresentedItem(index);
        if (item == null) {
            if (index == downIndex + 1) {
                return down();
            } else {
                return up();
            }
        } else {
            setTackleIntoPanel(item, index);
            return item;
        }
    }

    public int size() {
        return bag.getTackles().size() + ammunitionBag.getSize();
    }
}
