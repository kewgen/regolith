package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.map.*;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.tackle.Magazine;

/**
 * User: mikhail v. kutuzov
 * Date: 28.09.12
 * Time: 18:18
 */
public class ServerMagazineGround2BoxRequest extends ServerGround2BoxRequest {

    public ServerMagazineGround2BoxRequest(ServerBattle serverBattle) {
        super(serverBattle, Packets.TAKE_PROJECTILE_FROM_GROUND_PUT_INTO_BOX);
    }

    @Override
    protected void moveGround2Box(BattleCell cell, Box to) throws RuntimeException {
        CellElement element = BattleMapHelper.getElementByType(cell, CellElementTypes.MAGAZINE);
        if (element != null && element instanceof Magazine) {
            Magazine magazine = (Magazine) element;
            to.getMagazines().add(magazine);
            cell.removeElement(magazine);
        } else {
            throw new RuntimeException();
        }
    }
}
