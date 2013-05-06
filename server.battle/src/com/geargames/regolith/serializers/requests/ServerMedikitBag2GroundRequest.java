package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.helpers.AmmunitionBagHelper;
import com.geargames.regolith.units.map.CellElement;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.Ammunition;
import com.geargames.regolith.units.tackle.Medikit;

/**
 * User: mikhail v. kutuzov
 * Date: 31.08.12
 * Time: 17:04
 */
public class ServerMedikitBag2GroundRequest extends ServerBag2GroundRequest {
    public ServerMedikitBag2GroundRequest(ServerBattle serverBattle) {
        super(serverBattle, Packets.TAKE_MEDIKIT_FROM_BAG_PUT_INTO_GROUND);
    }

    @Override
    public CellElement putOut(short number, Warrior warrior) {
        Ammunition ammunition = warrior.getAmmunitionBag().getPackets().get(number).getAmmunition();
        if (ammunition instanceof Medikit) {
            AmmunitionBagHelper.putOut(warrior.getAmmunitionBag(), ammunition, 1);
            return ammunition;
        } else {
            return null;
        }
    }
}
