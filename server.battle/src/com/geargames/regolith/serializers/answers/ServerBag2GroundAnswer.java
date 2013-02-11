package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.serializers.*;
import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.Magazine;
import com.geargames.regolith.units.tackle.Medikit;
import com.geargames.regolith.units.tackle.Weapon;

/**
 * User: mikhail v. kutuzov
 * Date: 26.09.12
 * Time: 20:04
 */
public class ServerBag2GroundAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private short type;
    private Element element;
    private short x;
    private short y;

    public ServerBag2GroundAnswer(MicroByteBuffer buffer, short type, Element element, short x, short y) {
        this.buffer = buffer;
        this.type = type;
        this.element = element;
        this.x = x;
        this.y = y;
    }

    @Override
    public short getType() {
        return type;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        if (element instanceof Medikit) {
            TackleSerializer.serializeMedikit((Medikit)element, buffer);
        } else if (element instanceof Magazine) {
            BattleMapSerializer.serialize((Magazine)element, buffer);
        } else if (element instanceof Weapon) {
            TackleSerializer.serializeWeapon((Weapon)element, buffer);
        } else if (element instanceof Armor) {
            TackleSerializer.serializeArmor((Armor)element, buffer);
        }
        SimpleSerializer.serialize(x, buffer);
        SimpleSerializer.serialize(y, buffer);
    }
}
