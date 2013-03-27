package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.battle.Box;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ServerMedikitCollection;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.tackle.Medikit;

/**
 * User: mikhail v. kutuzov
 * Date: 17.09.12
 * Time: 10:44
 */
public class ServerMedikitBox2GroundRequest extends ServerBox2GroundRequest {
    public ServerMedikitBox2GroundRequest(ServerBattle serverBattle) {
        super(serverBattle, Packets.TAKE_MEDIKIT_FROM_BOX_PUT_INTO_BAG);
    }

    @Override
    protected Element putOut(int elementId, Box box, Warrior warrior, short x, short y) throws RegolithException {
        Medikit medikit = null;
        int i = 0;
        for (Medikit tackle : ((ServerMedikitCollection) box.getMedikits()).getMedikits()) {
            if (tackle.getId() == elementId) {
                medikit = tackle;
                break;
            }
            i++;
        }

        if (medikit == null) {
            new RegolithException();
        }

        BattleMap map = warrior.getBattleGroup().getAlliance().getBattle().getMap();
        BattleCell[][] cells = map.getCells();

        if (BattleMapHelper.ableToPut(warrior, cells, x, y)) {
            box.getMedikits().remove(i);
            BattleMapHelper.putIn(medikit, map, x, y);
        } else {
            return null;
        }

        return medikit;
    }
}
