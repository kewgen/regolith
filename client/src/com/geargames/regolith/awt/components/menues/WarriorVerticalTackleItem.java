package com.geargames.regolith.awt.components.menues;

import com.geargames.awt.DrawablePPanel;
import com.geargames.common.Event;
import com.geargames.common.Port;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.warrior.PAbstractTacklePanel;
import com.geargames.regolith.awt.components.warrior.exchange.PExchangePanel;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.TackleType;

/**
 * User: mikhail v. kutuzov
 * Элемент вертикального меню для обвеса бойца.
 */
public class WarriorVerticalTackleItem extends PAbstractTacklePanel {

    public WarriorVerticalTackleItem(PObject prototype) {
        super(prototype);
    }

    public boolean onEvent(int code, int param, int xTouch, int yTouch) {
        if (code == Event.EVENT_SYNTHETIC_CLICK) {
            click();
        }
        return false;
    }

    private void click() {
        StateTackle tackle = (StateTackle) getTackle();
        PRegolithPanelManager fabric = PRegolithPanelManager.getInstance();
        DrawablePPanel drawable;
        PExchangePanel panel;
        switch (tackle.getType()) {
            case TackleType.WEAPON:
                drawable = fabric.getWeaponFromWarrior();
                break;
            case TackleType.ARMOR:
                drawable = fabric.getArmorFromWarrior();
                break;
            default:
                return;
        }
        panel = (PExchangePanel)drawable.getElement();
        panel.setTackle(tackle, 1);
        drawable.setX((Port.getW() / 2) - panel.getDrawRegion().getWidth() / 2);
        drawable.setY((Port.getH() / 2) - panel.getDrawRegion().getHeight() / 2);
        fabric.showModal(drawable);
    }
}
