package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.service.BattleServiceConfigurationFactory;
import com.geargames.regolith.units.battle.Box;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.battle.WarriorHelper;
import com.geargames.regolith.units.dictionaries.ServerStateTackleCollection;
import com.geargames.regolith.units.tackle.StateTackle;

/**
 * User: mikhail v. kutuzov
 * Date: 16.08.12
 * Time: 14:11
 */
public class ServerTackleBox2BagRequest extends ServerBox2WarriorRequest {

    public ServerTackleBox2BagRequest(ServerBattle serverBattle) {
        super(Packets.TAKE_TACKLE_FROM_BOX_PUT_INTO_BAG, serverBattle);
    }

    @Override
    protected boolean putElement(int elementId, Box box, Warrior warrior) throws RegolithException {
        StateTackle tackle = null;

        for (StateTackle sTackle : ((ServerStateTackleCollection) box.getTackles()).getTackles()) {
            if (sTackle.getId() == elementId) {
                tackle = sTackle;
                break;
            }
        }

        if (tackle == null) {
            throw new RegolithException("There is no tackle [" + elementId + "] in the box");
        }


        boolean result = WarriorHelper.putInToBag(warrior, tackle, BattleServiceConfigurationFactory.getConfiguration().getRegolithConfiguration().getBaseConfiguration());
        if (result) {
            ((ServerStateTackleCollection) box.getTackles()).getTackles().remove(tackle);
        }
        return result;
    }
}
