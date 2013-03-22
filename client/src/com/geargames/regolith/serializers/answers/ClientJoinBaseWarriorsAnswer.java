package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.serializers.*;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mikhail v. kutuzov
 * Date: 09.07.12
 */
public class ClientJoinBaseWarriorsAnswer extends ClientDeSerializedMessage {
    private ClientConfiguration configuration;
    private Warrior[] warriors;
    private boolean success;

    public ClientJoinBaseWarriorsAnswer(ClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public Warrior[] getWarriors() {
        return warriors;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setWarriors(Warrior[] warriors) {
        this.warriors = warriors;
    }

    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        success = SimpleDeserializer.deserializeBoolean(buffer);
        if (success) {
            int length = SimpleDeserializer.deserializeInt(buffer);
            for (int i = 0; i < length; i++) {
                AccountDeserializer.deserialize(warriors[i], buffer, configuration.getBaseConfiguration());
            }
        }
    }

}
