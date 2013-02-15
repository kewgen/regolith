package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.serializers.BattleDeserializer;
import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SimpleDeserializer;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;

/**
 * User: mkutuzov
 * Date: 05.07.12
 */
public class ClientStartBattleAnswer extends ClientDeSerializedMessage {
    private Battle battle;
    private String host;
    private int port;
    private boolean success;

    private Account account;
    private BaseConfiguration baseConfiguration;

    public ClientStartBattleAnswer(Account account, BaseConfiguration baseConfiguration) {
        this.account = account;
        this.baseConfiguration = baseConfiguration;
        this.success = false;
    }

    protected void deSerialize(MicroByteBuffer buffer) {
        success = SimpleDeserializer.deserializeBoolean(buffer);
        if (success) {
            battle = BattleDeserializer.deserializeBattle(buffer, baseConfiguration, account);
            host = SimpleDeserializer.deserializeString(buffer);
            port = SimpleDeserializer.deserializeInt(buffer);
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Battle getBattle() {
        return battle;
    }

    public boolean isSuccess() {
        return success;
    }
}