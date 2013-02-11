package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.service.BattleServiceConfigurationFactory;
import com.geargames.regolith.units.AmmunitionBagHelper;
import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.battle.WarriorHelper;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.tackle.Magazine;

/**
 * User: mikhail v. kutuzov
 * Date: 25.09.12
 * Time: 18:11
 */
public class ServerMagazineGround2BagRequest extends ServerGround2WarriorRequest {

    public ServerMagazineGround2BagRequest(ServerBattle serverBattle) {
        super(serverBattle, Packets.TAKE_PROJECTILE_FROM_GROUND_PUT_INTO_BAG);
    }

    @Override
    protected boolean putStateTackle(Element element, BattleCell cell, Warrior warrior) {
        if (element instanceof Magazine) {
            Magazine magazine = (Magazine) element;
            short count = magazine.getCount();
            return WarriorHelper.putInToBag(warrior, magazine.getProjectile(), count, BattleServiceConfigurationFactory.getConfiguration().getRegolithConfiguration().getBaseConfiguration()) == count;
        }
        return false;
    }
}
