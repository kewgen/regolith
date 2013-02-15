package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.managers.ServerBattleMarketManager;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerListenToBattleAnswer;
import com.geargames.regolith.service.BattleManagerContext;
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
        Battle battle = battleManagerContext.getBattlesById().get(SimpleDeserializer.deserializeInt(from));
        if (battleManagerContext.getCreatedBattles().get(battle.getAuthor()) != null) {
            battleMarketManager.listenToBattle(battle, client.getAccount());
            client.setState(new ClientAtBattleCreation());
            return ServerListenToBattleAnswer.AnswerSuccess(writeBuffer, battle, Packets.LISTEN_TO_BATTLE);
        } else {
            return ServerListenToBattleAnswer.AnswerFailure(writeBuffer, Packets.LISTEN_TO_BATTLE);
        }
    }
}