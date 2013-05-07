package com.geargames.regolith.service.states;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.requests.*;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.service.*;
import com.geargames.regolith.service.ServerContext;

import java.util.HashMap;
import java.util.List;

/**
 * Клиент находится на базе.
 * Users: mkutuzov, abarakov
 */
public class ClientAtBase extends MainState {
    private ServerContext context;
    private ClientWriter writer;
    private BatchRequest batchRequest;

    private HashMap<Short, ServerRequest> processors;

    public ClientAtBase() {
        MainServerConfiguration serverConfiguration = MainServerConfigurationFactory.getConfiguration();
        this.context = serverConfiguration.getServerContext();
        this.writer = serverConfiguration.getWriter();
        processors = new HashMap<Short, ServerRequest>();
        processors.put(Packets.GO_TO_BATTLE_MARKET, new ServerGoToBattleMarketRequest());
        processors.put(Packets.GO_TO_TACKLE_MARKET, new ServerGoToTackleMarketRequest());
        processors.put(Packets.GO_TO_WARRIOR_MARKET, new ServerGoToWarriorMarketRequest());
        processors.put(Packets.TAKE_AMMUNITION_FROM_BAG_PUT_INTO_STORE_HOUSE, new ServerAmmunitionBagToStoreHouseRequest());
        processors.put(Packets.TAKE_AMMUNITION_FROM_BAG_PUT_ON_WARRIOR, new ServerAmmunitionBagToWarriorRequest());
        processors.put(Packets.TAKE_TACKLE_FROM_BAG_PUT_ON_BODY, new ServerTackleBagToWarriorRequest());
        processors.put(Packets.TAKE_TACKLE_FROM_BAG_PUT_INTO_STORE_HOUSE, new ServerTackleBagToStoreHouseRequest());
        processors.put(Packets.TAKE_AMMUNITION_FROM_STORE_HOUSE_PUT_INTO_BAG, new ServerAmmunitionStoreHouseToBagRequest());
        processors.put(Packets.TAKE_AMMUNITION_FROM_STORE_HOUSE_PUT_ONTO_WARRIOR, new ServerAmmunitionStoreHouseToWarriorRequest());
        processors.put(Packets.TAKE_TACKLE_FROM_STORE_HOUSE_PUT_INTO_BAG, new ServerTackleStoreHouseToBagRequest());
        processors.put(Packets.TAKE_TACKLE_FROM_STORE_HOUSE_PUT_ON_WARRIOR, new ServerTackleStoreHouseToWarriorRequest());
        processors.put(Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_BAG, new ServerTakleWarriorToBagRequest());
        processors.put(Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_STORE_HOUSE, new ServerTackleWarriorToStoreHouseRequest());
        batchRequest = new BatchRequest(processors);
    }


    protected void execute(MicroByteBuffer from, Client client, short type) throws RegolithException {
        List<MessageToClient> messages;
        switch (type) {
            case Packets.LOGOUT:
                new ServerSimpleLogoutRequest().clientRequest(from, client);
                return;
            case Packets.BATCH_MESSAGE:
                messages = batchRequest.request(from, getWriteBuffer(), client);
                break;
            default:
                ServerRequest request = processors.get(type);
                if (request != null) {
                    messages = request.request(from, getWriteBuffer(), client);
                } else {
                    throw new RegolithException("Invalid message type (" + type + ")");
                }
        }
        writer.addMessageToClient(messages.get(0));
    }

}
