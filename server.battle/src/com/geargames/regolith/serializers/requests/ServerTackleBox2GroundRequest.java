package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.units.dictionaries.ServerHumanElementCollection;
import com.geargames.regolith.units.map.CellElement;
import com.geargames.regolith.units.map.Box;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ServerStateTackleCollection;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.HumanElement;
import com.geargames.regolith.units.tackle.StateTackle;

/**
 * User: mikhail v. kutuzov
 * Date: 17.08.12
 * Time: 13:28
 */
public class ServerTackleBox2GroundRequest extends ServerBox2GroundRequest {

    public ServerTackleBox2GroundRequest(ServerBattle serverBattle) {
        super(serverBattle, Packets.TAKE_TACKLE_FROM_BOX_PUT_ON_GROUND);
    }

    @Override
    protected CellElement putOut(int elementId, Box box, Warrior warrior, short x, short y) throws RegolithException {
        StateTackle stateTackle = null;
        int i = 0;
        for (StateTackle tackle : ((ServerStateTackleCollection) box.getTackles()).getTackles()) {
            if (tackle.getId() == elementId) {
                stateTackle = tackle;
                break;
            }
            i++;
        }

        if (stateTackle == null) {
            new RegolithException();
        }

        BattleMap map = warrior.getBattleGroup().getAlliance().getBattle().getMap();
        BattleCell[][] cells = map.getCells();

        ServerHumanElementCollection units = getServerBattle().getHumanElements();
        HumanElement unit = BattleMapHelper.getHumanElementByHuman(units, warrior);
        if (BattleMapHelper.ableToPut(unit, cells, x, y)) {
            box.getTackles().remove(i);
            BattleMapHelper.putIn(stateTackle, map, x, y);
        } else {
            return null;
        }

        return stateTackle;
    }

}
