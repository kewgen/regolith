package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.helpers.AmmunitionBagHelper;
import com.geargames.regolith.units.AmmunitionBag;
import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.Ammunition;

/**
 * User: mikhail v. kutuzov
 * Date: 28.09.12
 * Time: 14:21
 */
public class ServerMagazineBag2GroundRequest extends ServerBag2GroundRequest {

    public ServerMagazineBag2GroundRequest(ServerBattle serverBattle) {
        super(serverBattle, Packets.TAKE_PROJECTILE_FROM_BAG_PUT_ON_GROUND);
    }

    @Override
    public Element putOut(short number, Warrior warrior) {
        AmmunitionBag bag = warrior.getAmmunitionBag();
        if (bag.getSize() > number) {
            Ammunition ammunition = bag.getPackets().get(number).getAmmunition();
            AmmunitionBagHelper.putOut(warrior.getAmmunitionBag(), ammunition, 1);
            return ammunition;
        } else {
            return null;
        }
    }
}
