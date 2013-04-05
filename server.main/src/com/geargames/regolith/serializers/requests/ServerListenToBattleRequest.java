package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.managers.ServerBattleMarketManager;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerListenToBattleAnswer;
import com.geargames.regolith.service.BattleManagerContext;
import com.geargames.regolith.service.BrowseBattlesSchedulerService;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.states.ClientAtBattleCreation;
import com.geargames.regolith.units.battle.Battle;

/**
 * User: mkutuzov
 * Date: 12.07.12
 */
public class ServerListenToBattleRequest extends MainOneToClientRequest {
    private BattleManagerContext battleManagerContext;
    private ServerBattleMarketManager battleMarketManager;

    public ServerListenToBattleRequest(BattleManagerContext battleManagerContext, ServerBattleMarketManager battleMarketManager) {
        this.battleManagerContext = battleManagerContext;
        this.battleMarketManager = battleMarketManager;
    }

    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        int battleId = SimpleDeserializer.deserializeInt(from);
        Battle battle = battleManagerContext.getBattlesById().get(battleId);
        if (battleManagerContext.getCreatedBattles().get(battle.getAuthor()) == battle) {
            battleMarketManager.listenToBattle(battle, client.getAccount());
            client.setState(new ClientAtBattleCreation());
            return ServerListenToBattleAnswer.AnswerSuccess(writeBuffer, battle, Packets.LISTEN_TO_CREATED_BATTLE);
        } else {
            return ServerListenToBattleAnswer.AnswerFailure(writeBuffer, Packets.LISTEN_TO_CREATED_BATTLE);
        }
    }

}
