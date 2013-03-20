package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.units.ClientBattleHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.Warrior;


public class ClientAllyMoveAnswer extends ClientDeSerializedMessage {
    private Battle battle;
    private short x;
    private short y;
    private Warrior warrior;
    private Warrior[] enemies;

    public ClientAllyMoveAnswer(Battle battle) {
        this.battle = battle;
    }

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public Warrior[] getEnemies() {
        return enemies;
    }

    @Override
    public void deSerialize(MicroByteBuffer buffer) {
        try {
            warrior = ClientBattleHelper.findWarrior(ClientConfigurationFactory.getConfiguration().getAccount(), SimpleDeserializer.deserializeInt(buffer));
        } catch (RegolithException e) {
        }
        x = SimpleDeserializer.deserializeShort(buffer);
        y = SimpleDeserializer.deserializeShort(buffer);
        int size = SimpleDeserializer.deserializeInt(buffer);
        if (size > 0) {
            enemies = new Warrior[size];
            try {
                for (int i = 0; i < size; i++) {
                    Warrior enemy = ClientBattleHelper.findWarrior(battle, SimpleDeserializer.deserializeInt(buffer));
                    enemies[i] = enemy;
                    enemy.setX(SimpleDeserializer.deserializeShort(buffer));
                    enemy.setY(SimpleDeserializer.deserializeShort(buffer));
                }
            } catch (RegolithException e) {

            }
        }
    }
}
