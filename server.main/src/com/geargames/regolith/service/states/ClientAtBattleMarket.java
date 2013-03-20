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

    public ClientAtBattleMarket() {
        this.serverConfiguration = MainServerConfigurationFactory.getConfiguration();
        this.serverContext = serverConfiguration.getServerContext();
        battleManagerContext = serverContext.getBattleManagerContext();
    }

    @Override
    protected void execute(MicroByteBuffer from, Client client, short type) throws RegolithException {
        ServerRequest request;
        ServerBattleMarketManager battleMarketManager = serverConfiguration.getBattleMarketManager();

        switch (type) {
            case Packets.CREATE_BATTLE:
                request = new ServerCreateBattleRequest(serverConfiguration, battleMarketManager);
                break;
            case Packets.LISTEN_TO_BATTLE:
                request = new ServerListenToBattleRequest(battleManagerContext, battleMarketManager);
                break;
            case Packets.GO_TO_BASE:
                request = new ServerGoToBase();
                break;
            case Packets.BROWSE_BATTLE_MAPS:
                request = new ServerBrowseBattleMapsRequest(battleMarketManager);
                break;
            case Packets.BROWSE_CREATED_BATTLES:
                request = new ServerBrowseCreatedBattlesRequest(battleMarketManager);
                break;
            case Packets.FRAME_MESSAGE:
                request = new ServerGetFrameRequest();
                break;
            default:
                throw new RegolithException();
        }
        serverConfiguration.getWriter().addMessageToClient(request.request(from, getWriteBuffer(), client).get(0));
    }
}
