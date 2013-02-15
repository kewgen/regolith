package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.serializers.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mkutuzov
 * Date: 06.07.12
 */
public class ClientLoginAnswer extends ClientDeSerializedMessage {
    private ClientConfiguration clientConfiguration;
    private BaseConfiguration baseConfiguration;
    private Account account;
    private String error;
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

    public String getError() {
        return error;
    }

    public ClientLoginAnswer(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    protected void deSerialize(MicroByteBuffer buffer) {
        if (SimpleDeserializer.deserializeBoolean(buffer)) {
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
        } else {
            error = SimpleDeserializer.deserializeString(buffer);
        }
    }
}