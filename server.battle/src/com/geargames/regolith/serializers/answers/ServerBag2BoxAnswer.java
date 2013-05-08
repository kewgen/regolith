package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.*;
import com.geargames.regolith.units.map.CellElement;
import com.geargames.regolith.units.map.CellElementTypes;
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
        switch (element.getElementType()) {
            case CellElementTypes.MEDIKIT:
                SimpleSerializer.serialize(SerializeHelper.MEDIKIT, buffer);
                TackleSerializer.serializeMedikit((Medikit) element, buffer);
                break;
            case CellElementTypes.MAGAZINE:
                SimpleSerializer.serialize(SerializeHelper.MAGAZINE, buffer);
                BattleMapSerializer.serialize((Magazine) element, buffer);
                break;
            case CellElementTypes.WEAPON:
                SimpleSerializer.serialize(SerializeHelper.WEAPON, buffer);
                TackleSerializer.serializeWeapon((Weapon) element, buffer);
                break;
            case CellElementTypes.ARMOR:
                SimpleSerializer.serialize(SerializeHelper.ARMOR, buffer);
                TackleSerializer.serializeArmor((Armor) element, buffer);
                break;
        }
        SimpleSerializer.serialize(x, buffer);
        SimpleSerializer.serialize(y, buffer);
    }
}
