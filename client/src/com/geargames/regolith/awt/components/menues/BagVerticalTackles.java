package com.geargames.regolith.awt.components.menues;

import com.geargames.awt.utils.ScrollHelper;
import com.geargames.awt.utils.motions.InertMotionListener;
import com.geargames.common.Graphics;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.units.battle.Warrior;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Содержимое сумки в виде вертикального списка.
 */
public class BagVerticalTackles extends VerticalTackles {
    private BagVerticalTackleItemsVector items;

    public BagVerticalTackles(PObject object){
        super(object);
        items = new BagVerticalTackleItemsVector(getShownItemsAmount(), (PObject) getPrototype().getPrototype());
        setMotionListener(new InertMotionListener());
    }

    public void initiateMotionListener() {
        setMotionListener(ScrollHelper.adjustVerticalInertMotionListener((InertMotionListener)getMotionListener(), getDrawRegion(), getItemsAmount(), getItemSize()));
    }

    public void initiate(Graphics graphics) {
        initiateMotionListener();
        setInitiated(true);
    }

    public void setWarrior(Warrior warrior){
        items.reset(warrior);
    }

    public Vector getItems() {
        return items;
    }

    @Override
    public int getItemOffsetX() {
        return 0;
    }
}
