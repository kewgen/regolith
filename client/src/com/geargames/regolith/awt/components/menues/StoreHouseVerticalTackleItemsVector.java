package com.geargames.regolith.awt.components.menues;

import com.geargames.common.logging.Debug;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;
import com.geargames.common.String;
import com.geargames.regolith.awt.components.warrior.PAbstractTacklePanel;
import com.geargames.regolith.helpers.StoreHouseHelper;
import com.geargames.regolith.units.AmmunitionPacket;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.tackle.Ammunition;
import com.geargames.regolith.units.tackle.StateTackle;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Proxy-объект вектор элементов склада пользователя.
 */
public class StoreHouseVerticalTackleItemsVector extends Vector {
    private StoreHouse storeHouse;
    private int amount;

    private ArrayList items;
    private int downIndex;
    private int upIndex;

    public StoreHouseVerticalTackleItemsVector(StoreHouse storeHouse, int amount, PObject menuElement) {
        this.storeHouse = storeHouse;
        this.amount = amount;
        items = new ArrayList(amount + 1);
        upIndex = 0;
        downIndex = amount;
        for (int i = 0; i <= amount; i++) {
            StoreHouseVerticalTackleItem item = new StoreHouseVerticalTackleItem(menuElement);
            items.add(item);
            setTackleIntoPanel(item, i);
        }
    }

    private void setTackleIntoPanel(PAbstractTacklePanel panel, int index) {
        StateTackle tackle = StoreHouseHelper.getStateTackle(storeHouse, index);
        if (tackle != null) {
            if (panel.getTackle() != tackle) {
                panel.setTackle(tackle, 1);
            }
        } else {
            AmmunitionPacket packet = StoreHouseHelper.getAmmunition(storeHouse, index);
            if (packet != null) {
                Ammunition ammunition = packet.getAmmunition();
                int amount = packet.getCount();
                if (panel.getTackle() != ammunition || amount != panel.getAmount()) {
                    panel.setTackle(ammunition, amount);
                }
            } else {
                Debug.error(String.valueOfC("A NOT EXISTED REQUISITE HAS BEEN RETRIEVED"));
            }
        }
    }

    private StoreHouseVerticalTackleItem getPresentedItem(int index) {
        if (index >= upIndex && index <= downIndex) {
            return (StoreHouseVerticalTackleItem) items.get(index - upIndex);
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
        StoreHouseVerticalTackleItem item = getPresentedItem(index);
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
        return StoreHouseHelper.getSize(storeHouse);
    }

}
