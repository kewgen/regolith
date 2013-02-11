package com.geargames.regolith.service.states;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.service.*;

/**
 * User: mikhail v. kutuzov
 * Date: 09.10.12
 * Time: 23:42
 */
public class ClientAtAccidentalBattleMarket extends MainState {
    private ServerContext serverContext;
    private MainServerConfiguration serverConfiguration;
    private BattleManagerContext battleManagerContext;

    public ClientAtAccidentalBattleMarket() {
        this.serverConfiguration = MainServerConfigurationFactory.getConfiguration();
        this.serverContext = serverConfiguration.getServerContext();
        battleManagerContext = serverContext.getBattleManagerContext();
    }

    @Override
    protected void execute(MicroByteBuffer from, Client client, short type) throws RegolithException {

    }
}
