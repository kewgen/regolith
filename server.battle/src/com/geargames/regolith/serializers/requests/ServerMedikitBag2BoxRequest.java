package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.units.AmmunitionBag;
import com.geargames.regolith.units.AmmunitionBagHelper;
import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.battle.Box;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.MedikitCollection;
import com.geargames.regolith.units.dictionaries.ServerMedikitCollection;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.tackle.Ammunition;
import com.geargames.regolith.units.tackle.Medikit;

/**
 * User: mikhail v. kutuzov
 * Date: 27.09.12
 * Time: 17:50
 */
public class ServerMedikitBag2BoxRequest extends ServerBag2BoxRequest {
    public ServerMedikitBag2BoxRequest(ServerBattle serverBattle) {
        super(serverBattle, Packets.TAKE_MEDIKIT_FROM_BAG_PUT_INTO_BOX);
    }

    @Override
    protected Element moveBag2Box(short bagNumber, Box box, Warrior warrior) throws RegolithException {
        AmmunitionBag bag = warrior.getAmmunitionBag();
        if(bag.getSize() >= bagNumber){
            throw new RegolithException();
        }
        Ammunition ammunition = bag.getPackets().get(bagNumber).getAmmunition();
        AmmunitionBagHelper.putOut(bag, ammunition, 1);

        MedikitCollection medikits = box.getMedikits();
        medikits.add((Medikit) ammunition);

        return ammunition;
    }
}
