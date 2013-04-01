package com.geargames.regolith.awt.components.menues;

import com.geargames.awt.utils.ScrollHelper;
import com.geargames.awt.utils.motions.InertMotionListener;
import com.geargames.common.Graphics;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.units.base.StoreHouse;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Список прокрутки для содержимого склада.
 */
public class StoreHouseVerticalTackles extends VerticalTackles {
    private StoreHouseVerticalTackleItemsVector items;

    public StoreHouseVerticalTackles(PObject object, StoreHouse storeHouse) {
        super(object);
        items = new StoreHouseVerticalTackleItemsVector(storeHouse, getShownItemsAmount(), (PObject) getPrototype().getPrototype());
        setMotionListener(new InertMotionListener());
    }

    public void initiateMotionListener() {
        setMotionListener(ScrollHelper.adjustVerticalInertMotionListener((InertMotionListener)getMotionListener(), getDrawRegion(), getItemsAmount(), getItemSize()));
    }

    public void initiate(Graphics graphics) {
        initiateMotionListener();
        setInitiated(true);
    }

    public Vector getItems() {
        return items;
    }

    @Override
    public int getItemOffsetX() {
        return 0;
    }
}
