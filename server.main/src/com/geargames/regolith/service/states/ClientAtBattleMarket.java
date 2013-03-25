package com.geargames.regolith.service.states;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.managers.ServerBattleMarketManager;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.serializers.requests.*;
import com.geargames.regolith.service.*;
import com.geargames.regolith.service.ServerContext;

/**
 * User: mkutuzov
 * Date: 20.06.12
 */
public class ClientAtBattleMarket extends MainState {
    private ServerContext serverContext;
    private MainServerConfiguration serverConfiguration;
    private BattleManagerContext battleManagerContext;
    private BrowseBattlesSchedulerService schedulerService;
    private boolean first;

    public ClientAtBattleMarket() {
        this.serverConfiguration = MainServerConfigurationFactory.getConfiguration();
        this.serverContext = serverConfiguration.getServerContext();
        battleManagerContext = serverContext.getBattleManagerContext();
        schedulerService = serverConfiguration.getBrowseBattlesSchedulerService();
        first = false;
    }

    @Override
    protected void execute(MicroByteBuffer from, Client client, short type) throws RegolithException {
        ServerRequest request;
        ServerBattleMarketManager battleMarketManager = serverConfiguration.getBattleMarketManager();
        if (first) {
            first = false;
            schedulerService.addListener(client);
        }
        switch (type) {
            case Packets.CREATE_BATTLE:
                request = new ServerCreateBattleRequest(serverConfiguration, battleMarketManager, schedulerService);
                break;
            case Packets.LISTEN_TO_BATTLE:
                request = new ServerListenToBattleRequest(battleManagerContext, battleMarketManager, schedulerService);
                break;
            case Packets.GO_TO_BASE:
                request = new ServerGoToBase();
                break;
            case Packets.BROWSE_BATTLE_MAPS:
                request = new ServerBrowseBattleMapsRequest(battleMarketManager);
                break;
            case Packets.FRAME_MESSAGE:
                request = new ServerGetFrameRequest();
                break;
            default:
                throw new RegolithException("Invalid message type (" + type + ")");
        }
        serverConfiguration.getWriter().addMessageToClient(request.request(from, getWriteBuffer(), client).get(0));
    }
}
