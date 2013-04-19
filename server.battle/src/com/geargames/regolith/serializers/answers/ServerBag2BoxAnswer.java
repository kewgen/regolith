package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.*;
import com.geargames.regolith.units.CellElement;
import com.geargames.regolith.units.tackle.*;

/**
 * User: mikhail v. kutuzov
 * Date: 27.09.12
 * Time: 10:10
 */
public class ServerBag2BoxAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private CellElement element;
    private short x;
    private short y;
    private short type;

    public ServerBag2BoxAnswer(MicroByteBuffer buffer, CellElement element, short x, short y, short type) {
        this.buffer = buffer;
        this.element = element;
        this.x = x;
        this.y = y;
        this.type = type;
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
            SerializeHelper.findTypeId(Medikit.class.getSimpleName());
            TackleSerializer.serializeMedikit((Medikit) element, buffer);
        } else if (element instanceof Magazine) {
            SerializeHelper.findTypeId(Magazine.class.getSimpleName());
            BattleMapSerializer.serialize((Magazine) element, buffer);
        } else if (element instanceof Weapon) {
            SerializeHelper.findTypeId(Weapon.class.getSimpleName());
            TackleSerializer.serializeWeapon((Weapon) element, buffer);
        } else if (element instanceof Armor) {
            SerializeHelper.findTypeId(Armor.class.getSimpleName());
            TackleSerializer.serializeArmor((Armor) element, buffer);
        }
        SimpleSerializer.serialize(x, buffer);
        SimpleSerializer.serialize(y, buffer);
    }
}
