package com.geargames.regolith.awt.components.warrior.exchange;

import com.geargames.awt.components.*;
import com.geargames.common.*;
import com.geargames.common.String;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.UIUtils;
import com.geargames.regolith.app.Application;
import com.geargames.regolith.units.AmmunitionPacket;
import com.geargames.regolith.units.dictionaries.WeaponTypeCollection;
import com.geargames.regolith.units.tackle.Projectile;

/**
 * User: mikhail v. kutuzov
 * Панель
 */
public abstract class PProjectileExchangePanel extends PExchangePanel {
    private PLabel caption;

    private PLabel weight;

    private PLabel category;
    private PLabel weaponTypes;
    private PGradualSpinBox box;
    private PLabel amount;

    public PProjectileExchangePanel(PObject prototype) {
        super(prototype);
    }


    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                addButton1((PObject) index.getPrototype(), index);
                break;
            case 1:
                addButton2((PObject) index.getPrototype(), index);
                break;
            case 2:
                caption = new PSimpleLabel(index);
                addPassiveChild(caption, index);
                break;
            case 3:
                weight = new PSimpleLabel(index);
                addPassiveChild(weight, index);
                break;
            case 4:
                category = new PSimpleLabel(index);
                addPassiveChild(category, index);
                break;
            case 5:
                weaponTypes = new PSimpleLabel(index);
                addPassiveChild(weaponTypes, index);
                break;
            case 6:
                amount = new PSimpleLabel(index);
                addPassiveChild(amount, index);
                break;
            case 15:
                box = new PGradualSpinBox((PObject)index.getPrototype(), (short)0);
                box.setFps(Application.mult_fps * 8);
                addActiveChild(box, index);
                break;
        }
    }

    public PGradualSpinBox getAmountBox() {
        return box;
    }

    protected void initiate() {
        Projectile projectile = (Projectile)getTackle();
        caption.setData(String.valueOfC(projectile.getName()));
        weight.setData(UIUtils.getWeightRepresentation(projectile.getWeight()));
        category.setData(String.valueOfC(projectile.getCategory().getName()));
        WeaponTypeCollection types = projectile.getWeaponTypes();
        String weapons = String.valueOfC("");
        for (int i = 0; i < types.size(); i++) {
            weapons = weapons.concatC(types.get(i).getName());
        }
        weaponTypes.setData(weapons);
        amount.setData(String.valueOfI(getAmount()));

    }
}