package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.serializers.*;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mikhail v. kutuzov
 * Date: 09.07.12
 * Time: 17:37
 */
public class ClientJoinBaseWarriorsAnswer extends ClientDeSerializedMessage {
    private Warrior[] warriors;
    private boolean success;

    private BaseConfiguration baseConfiguration;

    public Warrior[] getWarriors() {
        return warriors;
    }

    public boolean isSuccess() {
        return success;
    }


    public ClientJoinBaseWarriorsAnswer(Warrior[] warriors, BaseConfiguration baseConfiguration) {
        this.baseConfiguration = baseConfiguration;
        this.warriors = warriors;
    }

    @Override
    protected void deSerialize(MicroByteBuffer buffer) {
        success = SimpleDeserializer.deserializeBoolean(buffer);
        if (success) {
            int length = SimpleDeserializer.deserializeInt(buffer);
            for(int i = 0; i<length ; i++){
                AccountDeserializer.deserialize(warriors[i], buffer, baseConfiguration);
            }
        }
    }
}
