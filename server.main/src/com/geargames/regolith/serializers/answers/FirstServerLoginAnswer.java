package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.service.ConcurrentServerContext;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.service.ServerContext;
import com.geargames.regolith.serializers.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Warrior;

import java.util.Set;


public class FirstServerLoginAnswer extends SerializedMessage {
    private Account account;
    private MicroByteBuffer buffer;
    private BaseConfiguration baseConfiguration;
    private ServerContext context;

    public FirstServerLoginAnswer(MainServerConfiguration serverConfiguration, Account account, MicroByteBuffer buffer) {
        this.account = account;
        this.buffer = buffer;
        this.baseConfiguration = serverConfiguration.getRegolithConfiguration().getBaseConfiguration();
        this.context = serverConfiguration.getServerContext();
    }

    @Override
    public short getType() {
        return Packets.LOGIN;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(true, buffer);

        ConfigurationSerializer.serialize(baseConfiguration, buffer);
        AccountSerializer.serialize(account, buffer);

        if (context.getBaseWarriors() == null) {
            synchronized (this) {
                if (context.getBaseWarriors() == null) {
                    ((ConcurrentServerContext) context).setWarriors(MainServerConfigurationFactory.getConfiguration().getBaseWarriorMarketManager().browseWarriors());
                }
            }
        }
        Set<Warrior> warriors = context.getBaseWarriors();

        SimpleSerializer.serialize(warriors.size(), buffer);
        for (Warrior warrior : warriors) {
            AccountSerializer.serialize(warrior, buffer);
        }
    }
}

