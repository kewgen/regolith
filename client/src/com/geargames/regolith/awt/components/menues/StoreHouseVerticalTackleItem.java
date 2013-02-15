package com.geargames.regolith.awt.components.menues;

import com.geargames.awt.DrawablePElement;
import com.geargames.common.Port;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.common.Event;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.warrior.PAbstractTacklePanel;
import com.geargames.regolith.awt.components.warrior.exchange.PExchangePanel;
import com.geargames.regolith.units.AmmunitionPacket;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.base.StoreHouseHelper;
import com.geargames.regolith.units.tackle.Ammunition;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.TackleType;

/**
 * User: mikhail v. kutuzov
 * Элемент меню для отображения содержимого склада пользователя.
 */
public class StoreHouseVerticalTackleItem extends PAbstractTacklePanel {


    public StoreHouseVerticalTackleItem(PObject prototype) {
        super(prototype);
    }

    public boolean event(int code, int param, int xTouch, int yTouch) {
        if(code == Event.EVENT_SYNTHETIC_CLICK){
            click(param);
        }
        return false;
    }

    private void click(int number) {
        StoreHouse storeHouse = ClientConfigurationFactory.getConfiguration().getAccount().getBase().getStoreHouse();
        StateTackle tackle = StoreHouseHelper.getStateTackle(storeHouse, number);
        PRegolithPanelManager panels = PRegolithPanelManager.getInstance();

        DrawablePElement drawable;
        PExchangePanel panel;
        if (tackle != null) {
            switch (tackle.getType()) {
                case TackleType.ARMOR:
                    drawable = panels.getArmorFromStoreHouse();
                    break;
                case TackleType.WEAPON:
                    drawable = panels.getWeaponFromStoreHouse();
                    break;
                default:
                    return;
            }
            panel = (PExchangePanel) drawable.getElement();
            panel.setNumber(number);
            panel.setTackle(tackle, getAmount());
            panels.showModal(drawable);
        } else {
            AmmunitionPacket packet = StoreHouseHelper.getAmmunition(storeHouse, number);
            if (packet != null && packet.getAmmunition() != null) {
                Ammunition ammunition = packet.getAmmunition();
                switch (ammunition.getType()) {
                    case TackleType.MEDIKIT:
                        drawable = panels.getMedikitFromStoreHouse();
                        break;
                    case TackleType.PROJECTILE:
                        drawable = panels.getProjectileFromStoreHouse();
                        break;
                    default:
                        return;
                }
                panel = (PExchangePanel) drawable.getElement();
                panel.setNumber(number);
                panel.setTackle(tackle,getAmount());
                drawable.setX((Port.getW() / 2) - panel.getDrawRegion().getWidth() / 2);
                drawable.setY((Port.getH() / 2) - panel.getDrawRegion().getHeight() / 2);
                panels.showModal(drawable);
            }
        }
    }
}
