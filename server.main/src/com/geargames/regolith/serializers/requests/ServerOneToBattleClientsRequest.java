package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.serializers.MainServerRequestUtils;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.service.*;
import com.geargames.regolith.units.battle.Battle;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mkutuzov
 * Date: 13.07.12
 */
public abstract class ServerOneToBattleClientsRequest extends ServerRequest {
    protected BattleManagerContext battleManagerContext;
    protected ServerContext serverContext;
    protected Battle battle;

    protected ServerOneToBattleClientsRequest(Battle battle) {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        this.battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        this.serverContext = configuration.getServerContext();
        this.battle = battle;
    }

    protected abstract SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException;

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        List<MessageToClient> messages = new ArrayList<MessageToClient>(1);
        List<SocketChannel> recipients = MainServerRequestUtils.recipientsByCreatedBattle(battle);
        messages.add(new MainMessageToClient(recipients, clientRequest(from, to, client).serialize()));
        return messages;
    }
}
