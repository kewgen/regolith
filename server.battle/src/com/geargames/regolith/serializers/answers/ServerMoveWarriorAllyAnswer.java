package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.Ally;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ServerAllyCollection;

/**
 * User: mikhail v. kutuzov
 * Date: 23.08.12
 * Time: 13:56
 */
public class ServerMoveWarriorAllyAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Warrior warrior;
    private ServerAllyCollection enemies;
    private short type;

    public static ServerMoveWarriorAllyAnswer moveMine(MicroByteBuffer buffer, Warrior warrior, ServerAllyCollection enemies){
        return new ServerMoveWarriorAllyAnswer(buffer,warrior,enemies, Packets.MOVE_WARRIOR);
    }

    public static ServerMoveWarriorAllyAnswer moveAlly(MicroByteBuffer buffer, Warrior warrior, ServerAllyCollection enemies){
        return new ServerMoveWarriorAllyAnswer(buffer,warrior,enemies, Packets.MOVE_ALLY);
    }

    private ServerMoveWarriorAllyAnswer(MicroByteBuffer buffer, Warrior warrior, ServerAllyCollection enemies, short type) {
        this.buffer = buffer;
        this.warrior = warrior;
        this.type = type;
        this.enemies = enemies;
    }

    @Override
    public short getType() {
        return type;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    /**
     * id война
     * x и y на карте куда он пойдёт(конец пути)
     * массив врагов которых он "засветил"("засвет" противника может произойти только в конце пути)
     * @param buffer
     */
    @Override
    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(warrior, buffer);
        SimpleSerializer.serialize(warrior.getX(), buffer);
        SimpleSerializer.serialize(warrior.getY(), buffer);
        SimpleSerializer.serialize(enemies.size(), buffer);
        for (Ally human : enemies.getAllies()) {
            SerializeHelper.serializeEntityReference(human, buffer);
            SimpleSerializer.serialize(human.getX(), buffer);
            SimpleSerializer.serialize(human.getY(), buffer);
        }
    }
}
