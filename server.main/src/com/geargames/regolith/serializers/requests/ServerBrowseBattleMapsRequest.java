package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.ErrorCodes;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.ServerDataBaseHelper;
import com.geargames.regolith.managers.ServerBattleMarketManager;
import com.geargames.regolith.serializers.answers.ServerBrowseBattleMapsAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.map.BattleMap;

/**
 * Users: mkutuzov, abarakov
 * Date: 13.07.12
 */
public class ServerBrowseBattleMapsRequest extends MainOneToClientRequest {
    private ServerBattleMarketManager battleMarketManager;

    public ServerBrowseBattleMapsRequest() {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        battleMarketManager = configuration.getBattleMarketManager();
    }

    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        int battleTypeId = SimpleDeserializer.deserializeInt(from);
        BattleType type = ServerDataBaseHelper.getBattleTypeById(battleTypeId);
        if (type != null) {
            return ServerBrowseBattleMapsAnswer.answerSuccess(writeBuffer, battleMarketManager.browseBattleMaps(type).toArray(new BattleMap[]{}));
        } else {
            return ServerBrowseBattleMapsAnswer.answerFailure(writeBuffer, ErrorCodes.INVALID_BATTLE_TYPE);
        }
    }

}
