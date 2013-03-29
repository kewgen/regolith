package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.units.Human;
import com.geargames.regolith.units.battle.Battle;

public class ClientShootAnswer extends ClientDeSerializedMessage {
    private Battle battle;

    private Human hunter;
    private Human victim;

    public Human getHunter() {
        return hunter;
    }

    public Human getVictim() {
        return victim;
    }

    public ClientShootAnswer(Battle battle) {
        this.battle = battle;
        this.hunter = null;
        this.victim = null;
    }

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        hunter = null;
        victim = null;
        int refId = SimpleDeserializer.deserializeInt(buffer);
        if (refId != SerializeHelper.NULL_REFERENCE) {
            try {
                hunter = ClientBattleHelper.findWarrior(battle, refId);
                hunter.getWeapon().onShoot(ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
                hunter.setHealth(SimpleDeserializer.deserializeInt(buffer));
                hunter.getWeapon().setState(SimpleDeserializer.deserializeShort(buffer));
                hunter.getWeapon().setLoad(SimpleDeserializer.deserializeShort(buffer));
                hunter.getHeadArmor().setState(SimpleDeserializer.deserializeShort(buffer));
                hunter.getTorsoArmor().setState(SimpleDeserializer.deserializeShort(buffer));
                hunter.getLegsArmor().setState(SimpleDeserializer.deserializeShort(buffer));
                hunter.getWeapon().setLoad(SimpleDeserializer.deserializeShort(buffer));
            } catch (Exception e) {
                throw new Exception("A shooter damage serialization problem", e);
            }
        }
        refId = SimpleDeserializer.deserializeInt(buffer);
        if (refId != SerializeHelper.NULL_REFERENCE) {
            try {
                victim = ClientBattleHelper.findWarrior(battle, refId);

                victim.setHealth(SimpleDeserializer.deserializeInt(buffer));
                victim.getHeadArmor().setState(SimpleDeserializer.deserializeShort(buffer));
                victim.getTorsoArmor().setState(SimpleDeserializer.deserializeShort(buffer));
                victim.getLegsArmor().setState(SimpleDeserializer.deserializeShort(buffer));
            } catch (Exception e) {
                throw new Exception("A victim damage serialization problem", e);
            }
        }
    }
}
