package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.managers.ServerBattleMarketManager;
import com.geargames.regolith.serializers.answers.ServerBrowseBattleMapsAnswer;
import com.geargames.regolith.service.Client;

/**
 * User: mkutuzov
 * Date: 13.07.12
 */
public class ServerBrowseBattleMapsRequest extends MainOneToClientRequest {
    private ServerBattleMarketManager battleMarketManager;

    public ServerBrowseBattleMapsRequest(ServerBattleMarketManager battleMarketManager) {
        this.battleMarketManager = battleMarketManager;
    }

    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        return new ServerBrowseBattleMapsAnswer(writeBuffer, battleMarketManager.browseBattleMaps());
    }
}
