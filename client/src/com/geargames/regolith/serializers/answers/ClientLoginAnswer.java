package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.ErrorCodes;
import com.geargames.regolith.serializers.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Warrior;

/**
 * Users: mkutuzov, abarakov
 * Date: 06.07.12
 */
public class ClientLoginAnswer extends ClientDeSerializedMessage {
    private BaseConfiguration baseConfiguration;
    private short errorCode;
    private Account account;
    private Warrior[] warriors;

    public Warrior[] getWarriors() {
        return warriors;
    }

    public BaseConfiguration getBaseConfiguration() {
        return baseConfiguration;
    }

    public Account getAccount() {
        return account;
    }

    public short getErrorCode() {
        return errorCode;
    }

    public boolean isSuccess() {
        return errorCode == ErrorCodes.SUCCESS;
    }

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        baseConfiguration = null;
        account = null;
        warriors = null;
        errorCode = SimpleDeserializer.deserializeShort(buffer);
        if (errorCode == ErrorCodes.SUCCESS) {
            baseConfiguration = ConfigurationDeserializer.deserializeBaseConfiguration(buffer);
            account = AccountDeserializer.deserialize(buffer, baseConfiguration);
            if (account.getWarriors() == null || account.getWarriors().size() == 0) {
                int length = SimpleDeserializer.deserializeInt(buffer);
                warriors = new Warrior[length];
                for (int i = 0; i < length; i++) {
                    Warrior warrior = new Warrior();
                    AccountDeserializer.deserialize(warrior, buffer, baseConfiguration);
                    warriors[i] = warrior;
                }
            }
        }
    }

}
