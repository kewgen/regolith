package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.service.BattleServiceConfigurationFactory;
import com.geargames.regolith.units.AmmunitionBagHelper;
import com.geargames.regolith.units.battle.Box;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.battle.WarriorHelper;
import com.geargames.regolith.units.dictionaries.ServerMedikitCollection;
import com.geargames.regolith.units.tackle.Medikit;

/**
 * User: mikhail v. kutuzov
 * Date: 30.08.12
 * Time: 18:20
 */
public class ServerMedikitBox2BagRequest extends ServerBox2WarriorRequest {

    public ServerMedikitBox2BagRequest(ServerBattle serverBattle) {
        super(Packets.TAKE_MEDIKIT_FROM_BOX_PUT_INTO_BAG, serverBattle);
    }

    @Override
    protected boolean putElement(int elementId, Box box, Warrior warrior) throws RegolithException {
        Medikit medikit = null;

        int i = 0;
        for (Medikit sMedikit : ((ServerMedikitCollection) box.getMedikits()).getMedikits()) {
            if (sMedikit.getId() == elementId) {
                medikit = sMedikit;
                break;
            }
            i++;
        }

        if (medikit == null) {
            throw new RegolithException("There is no tackle [" + elementId + "] in the box");
        }

        if( WarriorHelper.putInToBag(warrior, medikit, 1, BattleServiceConfigurationFactory.getConfiguration().getRegolithConfiguration().getBaseConfiguration()) == 1 ){
            box.getMedikits().remove(i);
            return true;
        } else {
            return false;
        }
    }
}
