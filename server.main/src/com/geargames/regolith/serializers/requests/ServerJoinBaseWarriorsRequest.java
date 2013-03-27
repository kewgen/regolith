package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.*;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.helpers.MainServerHelper;
import com.geargames.regolith.serializers.answers.ServerJoinBaseWarriorsAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.service.ServerContext;
import com.geargames.regolith.service.states.ClientAtBase;

import com.geargames.regolith.units.battle.Warrior;

import java.io.IOException;

/**
 * User: mkutuzov
 * Date: 12.07.12
 */
public class ServerJoinBaseWarriorsRequest extends MainOneToClientRequest {

    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        MainServerConfiguration serverConfiguration = MainServerConfigurationFactory.getConfiguration();
        BaseConfiguration baseConfiguration = serverConfiguration.getRegolithConfiguration().getBaseConfiguration();
        ServerContext context = serverConfiguration.getServerContext();
        int length = SimpleDeserializer.deserializeInt(from);
        if (baseConfiguration.getInitWarriorsAmount() != length) {
            throw new RegolithException();
        }
        Warrior[] warriors = new Warrior[length];
        for (int i = 0; i < length; i++) {
            int id = SimpleDeserializer.deserializeInt(from);
            Warrior found = null;
            for (Warrior warrior : context.getBaseWarriors()) {
                if (warrior.getId() == id) {
                    found = warrior;
                    break;
                }
            }
            if (found == null) {
                throw new RegolithException();
            }
            try {
                warriors[i] = MainServerHelper.copyThroughSerialization(found);
            } catch (IOException e) {
                throw new RegolithException(e);
            } catch (ClassNotFoundException e) {
                throw new RegolithException(e);
            }
            warriors[i].setName(SimpleDeserializer.deserializeString(from));
        }
        serverConfiguration.getBaseWarriorMarketManager().hireWarriors(warriors, client.getAccount());
        client.setState(new ClientAtBase());
        return ServerJoinBaseWarriorsAnswer.successAnswer(writeBuffer, warriors);
    }
}
