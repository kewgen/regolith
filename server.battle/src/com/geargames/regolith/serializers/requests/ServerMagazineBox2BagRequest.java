package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SimpleDeserializer;
import com.geargames.regolith.service.BattleServiceConfigurationFactory;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.AmmunitionBagHelper;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ServerMagazineCollection;
import com.geargames.regolith.units.dictionaries.ServerMedikitCollection;
import com.geargames.regolith.units.tackle.Magazine;
import com.geargames.regolith.units.tackle.Medikit;

import java.util.List;

/**
 * User: mikhail v. kutuzov
 * Date: 26.09.12
 * Time: 11:09
 */
public class ServerMagazineBox2BagRequest extends ServerBox2WarriorRequest {

    public ServerMagazineBox2BagRequest(ServerBattle serverBattle) {
        super(Packets.TAKE_PROJECTILE_FROM_BOX_PUT_INTO_BAG, serverBattle);
    }

    @Override
    protected boolean putElement(int elementId, Box box, Warrior warrior) throws RegolithException {
        Magazine magazine = null;

        int i = 0;
        for (Magazine sMagazine : ((ServerMagazineCollection) box.getMagazines()).getMagazines()) {
            if (sMagazine.getId() == elementId) {
                magazine = sMagazine;
                break;
            }
            i++;
        }

        if (magazine == null) {
            throw new RegolithException("There is no tackle [" + elementId + "] in the box");
        }

        short count = magazine.getCount();
        short put = WarriorHelper.putInToBag(warrior, magazine.getProjectile(), count, BattleServiceConfigurationFactory.getConfiguration().getRegolithConfiguration().getBaseConfiguration());
        if (put == count) {
            box.getMagazines().remove(i);
            return true;
        } else {
            box.getMagazines().get(i).setCount((short) (count - put));
            return false;
        }
    }
}
