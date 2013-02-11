package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.units.AmmunitionBag;
import com.geargames.regolith.units.AmmunitionBagHelper;
import com.geargames.regolith.units.AmmunitionPacket;
import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.battle.Box;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.Magazine;
import com.geargames.regolith.units.tackle.Projectile;

/**
 * User: mikhail v. kutuzov
 * Date: 28.09.12
 * Time: 14:41
 */
public class ServerMagazineBag2Box extends ServerBag2BoxRequest {

    public ServerMagazineBag2Box(ServerBattle serverBattle) {
        super(serverBattle, Packets.TAKE_PROJECTILE_FROM_BAG_PUT_INTO_BOX);
    }

    @Override
    protected Element moveBag2Box(short bagNumber, Box box, Warrior warrior) throws RegolithException {
        AmmunitionBag bag = warrior.getAmmunitionBag();
        if(bag.getSize() >= bagNumber){
            throw new RegolithException();
        }
        AmmunitionPacket packet = bag.getPackets().get(bagNumber);
        Projectile ammunition = (Projectile)packet.getAmmunition();
        AmmunitionBagHelper.putOut(bag, ammunition, packet.getCount());
        Magazine magazine = new Magazine();
        magazine.setProjectile(ammunition);
        magazine.setCount(packet.getCount());
        box.getMagazines().add(magazine);

        return magazine;
    }
}
