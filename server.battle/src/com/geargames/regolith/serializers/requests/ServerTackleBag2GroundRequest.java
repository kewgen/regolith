package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mikhail v. kutuzov
 * Date: 20.08.12
 * Time: 18:34
 */
public class ServerTackleBag2GroundRequest extends ServerBag2GroundRequest {

    public ServerTackleBag2GroundRequest(ServerBattle serverBattle) {
        super(serverBattle, Packets.TAKE_TACKLE_FROM_BAG_PUT_ON_GROUND);
    }

    @Override
    public Element putOut(short number, Warrior warrior) {
        return WarriorHelper.putOutOfBag(warrior, number);
    }
}
