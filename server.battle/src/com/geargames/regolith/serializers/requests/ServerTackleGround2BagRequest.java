package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.CellElement;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.tackle.StateTackle;

/**
 * Сложить оружие с клетки поля сумку бойца.
 */
public class ServerTackleGround2BagRequest extends ServerGround2WarriorRequest {
    private BaseConfiguration baseConfiguration;

    public ServerTackleGround2BagRequest(ServerBattle serverBattle, BaseConfiguration baseConfiguration) {
        super(serverBattle, Packets.TAKE_TACKLE_FROM_GROUND_PUT_INTO_BAG);
        this.baseConfiguration = baseConfiguration;
    }

    @Override
    protected boolean putStateTackle(CellElement element, BattleCell cell, Warrior warrior) {
        if (element instanceof StateTackle) {
            boolean result = WarriorHelper.putInToBag(warrior, (StateTackle) element, baseConfiguration);
            if (result) {
                cell.removeElement(element);
            }
            return result;
        } else {
            return false;
        }
    }
}
