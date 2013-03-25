package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.answers.ServerListenToBattleAnswer;
import com.geargames.regolith.service.BrowseBattlesSchedulerService;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.managers.ServerBattleMarketManager;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.states.ClientAtBattleCreation;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.map.BattleMap;
import org.hibernate.Session;

/**
 * User: mkutuzov
 * Date: 12.07.12
 */
public class ServerCreateBattleRequest extends MainOneToClientRequest {
    private MainServerConfiguration serverConfiguration;
    private ServerBattleMarketManager battleMarketManager;
    private BrowseBattlesSchedulerService schedulerService;

    public ServerCreateBattleRequest(MainServerConfiguration serverConfiguration, ServerBattleMarketManager battleMarketManager, BrowseBattlesSchedulerService schedulerService) {
        this.serverConfiguration = serverConfiguration;
        this.battleMarketManager = battleMarketManager;
        this.schedulerService = schedulerService;
    }

    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        Session session = serverConfiguration.getSessionFactory().openSession();
        BattleMap battleMap = (BattleMap) session.load(BattleMap.class, SimpleDeserializer.deserializeInt(from));
        session.close();
        int index = from.get();
        Battle battle = battleMarketManager.createBattle(battleMap, index, client.getAccount());
        client.setState(new ClientAtBattleCreation());
        schedulerService.removeListener(client);
        schedulerService.addBattle(battle);
        return ServerListenToBattleAnswer.AnswerSuccess(writeBuffer, battle, Packets.CREATE_BATTLE);
    }
}
