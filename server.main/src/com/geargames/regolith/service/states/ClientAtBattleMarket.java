package com.geargames.regolith.service.states;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.managers.ServerBattleMarketManager;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.serializers.requests.*;
import com.geargames.regolith.service.*;

/**
 * Users: mkutuzov, abarakov
 * Date: 20.06.12
 */
public class ClientAtBattleMarket extends MainState {
    private MainServerConfiguration serverConfiguration;
    private BattleManagerContext battleManagerContext;
    private BrowseBattlesSchedulerService schedulerService;

    public ClientAtBattleMarket() {
        this.serverConfiguration = MainServerConfigurationFactory.getConfiguration();
        battleManagerContext = serverConfiguration.getServerContext().getBattleManagerContext();
        schedulerService = serverConfiguration.getBrowseBattlesSchedulerService();
    }

    @Override
    protected void execute(MicroByteBuffer from, Client client, short type) throws RegolithException {
        ServerRequest request;
        ServerBattleMarketManager battleMarketManager = serverConfiguration.getBattleMarketManager();
        switch (type) {
            case Packets.LOGOUT:
                new ServerSimpleLogoutRequest().clientRequest(from, client);
                return;
            case Packets.CREATE_BATTLE:
                request = new ServerCreateBattleRequest(serverConfiguration, battleMarketManager, schedulerService);
                break;
            case Packets.LISTEN_TO_CREATED_BATTLE:
                request = new ServerListenToBattleRequest(battleManagerContext, battleMarketManager);
                break;
            case Packets.GO_TO_BASE:
                request = new ServerGoToBaseRequest();
                break;
            case Packets.BROWSE_BATTLE_MAPS:
                request = new ServerBrowseBattleMapsRequest();
                break;
            case Packets.BROWSE_RANDOM_BATTLE_MAP:
                request = new ServerBrowseRandomBattleMapRequest();
                break;
            case Packets.LISTEN_TO_BROWSED_CREATED_BATTLES:
                request = new ServerListenToBrowsedCreatedBattlesRequest();
                break;
            case Packets.DO_NOT_LISTEN_TO_BROWSED_CREATED_BATTLES:
                request = new ServerDoNotListenToCreatedBattlesRequest();
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
