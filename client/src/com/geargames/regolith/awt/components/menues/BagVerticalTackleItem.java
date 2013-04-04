package com.geargames.regolith.awt.components.menues;

import com.geargames.awt.DrawablePPanel;
import com.geargames.common.Event;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.warrior.PAbstractTacklePanel;
import com.geargames.regolith.awt.components.warrior.exchange.PExchangePanel;
import com.geargames.regolith.units.tackle.AbstractTackle;
import com.geargames.regolith.units.tackle.TackleType;

/**
 * User: mikhail v. kutuzov
 * Элемент меню для отображения содержимого сумки бойца.
 */
public class BagVerticalTackleItem extends PAbstractTacklePanel {

    public BagVerticalTackleItem(PObject prototype) {
        super(prototype);
    }

    public boolean onEvent(int code, int param, int xTouch, int yTouch) {
        if (Event.EVENT_SYNTHETIC_CLICK == code) {
            click(param);
        }
        return false;
    }

    private void click(int number) {
        AbstractTackle tackle = getTackle();
        PRegolithPanelManager fabric = PRegolithPanelManager.getInstance();
        DrawablePPanel element;
        switch (tackle.getType()) {
            case TackleType.ARMOR:
                element = fabric.getArmorFromBag();
                break;
            case TackleType.WEAPON:
                element = fabric.getWeaponFromBag();
                break;
            case TackleType.PROJECTILE:
                element = fabric.getProjectileFromBag();
                break;
            case TackleType.MEDIKIT:
                element = fabric.getMedikitFromBag();
                break;
            default:
                return;
        }
        PExchangePanel panel = (PExchangePanel)element.getElement();
        panel.setNumber(number);
        panel.setTackle(tackle, getAmount());
        fabric.showModal(element);
    }
}
