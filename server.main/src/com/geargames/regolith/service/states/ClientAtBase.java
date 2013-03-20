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
        processors.put(Packets.GO_TO_BATTLE_MARKET, new GoToBattleMarketRequest());
        processors.put(Packets.GO_TO_TACKLE_MARKET, new GoToTackleMarketRequest());
        processors.put(Packets.GO_TO_WARRIOR_MARKET, new GoToWarriorMarketRequest());
        processors.put(Packets.TAKE_AMMUNITION_FROM_BAG_PUT_INTO_STORE_HOUSE, new ServerAmmunitionBag2StoreHouseRequest());
        processors.put(Packets.TAKE_AMMUNITION_FROM_BAG_PUT_ON_WARRIOR, new ServerAmmunitionBag2WarriorRequest());
        processors.put(Packets.TAKE_TACKLE_FROM_BAG_PUT_ON_BODY, new ServerTackleBag2WarriorRequest());
        processors.put(Packets.TAKE_TACKLE_FROM_BAG_PUT_INTO_STORE_HOUSE, new ServerTackleBag2StoreHouseRequest());
        processors.put(Packets.TAKE_AMMUNITION_FROM_STORE_HOUSE_PUT_INTO_BAG, new ServerAmmunitionStoreHouse2Bag());
        processors.put(Packets.TAKE_AMMUNITION_FROM_STORE_HOUSE_PUT_ONTO_WARRIOR, new ServerAmmunitionStoreHouse2Warrior());
        processors.put(Packets.TAKE_TACKLE_FROM_STORE_HOUSE_PUT_INTO_BAG, new ServerTackleStoreHouse2BagRequest());
        processors.put(Packets.TAKE_TACKLE_FROM_STORE_HOUSE_PUT_ON_WARRIOR, new ServerTackleStoreHouse2WarriorRequest());
        processors.put(Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_BAG, new ServerTakleWarrior2BagRequest());
        processors.put(Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_STORE_HOUSE, new ServerTackleWarrior2StoreHouseRequest());
        batchRequest = new BatchRequest(processors);
    }


    protected void execute(MicroByteBuffer from, Client client, short type) throws RegolithException {
        List<MessageToClient> messages;
        switch (type) {
            case Packets.LOGOUT:
                client.setState(new ClientNotLoggedIn());
                context.removeChannel(client.getAccount());
                return;
            case Packets.BATCH_MESSAGE:
                messages = batchRequest.request(from, getWriteBuffer(), client);
                break;
            default:
                ServerRequest request = processors.get(type);
                if (request != null) {
                    messages = request.request(from, getWriteBuffer(), client);
                } else {
                    throw new RegolithException();
                }
        }
        writer.addMessageToClient(messages.get(0));
    }

}
