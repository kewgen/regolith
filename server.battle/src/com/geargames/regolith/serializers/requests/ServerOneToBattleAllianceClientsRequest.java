package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.ServerBattle;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mkutuzov
 * Date: 23.07.12
 */
public abstract class ServerOneToBattleAllianceClientsRequest extends ServerRequest {
    private ServerBattle serverBattle;
    private BattleAlliance alliance;

    public ServerOneToBattleAllianceClientsRequest(ServerBattle serverBattle, BattleAlliance alliance) {
        this.serverBattle = serverBattle;
        this.alliance = alliance;
    }

    protected abstract SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException;

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        int size = alliance.getAllies().size();
        List<MessageToClient> messages = new ArrayList<MessageToClient>(1);
        List<SocketChannel> recipients = new ArrayList<SocketChannel>(size);
        for(Client ally : serverBattle.getAlliances().get(alliance.getNumber())){
            recipients.add(ally.getChannel());
        }
        messages.add(new BattleMessageToClient(recipients, clientRequest(from,to,client).serialize()));
        return messages;
    }
}
