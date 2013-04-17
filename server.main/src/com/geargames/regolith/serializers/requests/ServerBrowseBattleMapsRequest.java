package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.ServerDataBaseHelper;
import com.geargames.regolith.managers.ServerBattleMarketManager;
import com.geargames.regolith.serializers.answers.ServerBrowseBattleMapsAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.map.BattleMap;

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
        BattleType type = ServerDataBaseHelper.getBattleTypeById(SimpleDeserializer.deserializeInt(from));
        if (type != null) {
            return new ServerBrowseBattleMapsAnswer(writeBuffer, battleMarketManager.browseBattleMaps(type).toArray(new BattleMap[]{}));
        } else {
            return new ServerBrowseBattleMapsAnswer(writeBuffer, new BattleMap[]{});
        }
    }

}
