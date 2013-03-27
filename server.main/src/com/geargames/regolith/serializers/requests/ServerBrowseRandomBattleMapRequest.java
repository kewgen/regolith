package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.ServerDataBaseHelper;
import com.geargames.regolith.managers.ServerBattleMarketManager;
import com.geargames.regolith.serializers.answers.ServerBrowseRandomBattleMapAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.units.battle.BattleType;

/**
 * User: m.v.kutuzov
 * Date: 27.03.13
 */
public class ServerBrowseRandomBattleMapRequest extends MainOneToClientRequest {

    private ServerBattleMarketManager battleMarketManager;

    public ServerBrowseRandomBattleMapRequest() {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        battleMarketManager = configuration.getBattleMarketManager();
    }

    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        BattleType type = ServerDataBaseHelper.getBattleTypeById(SimpleDeserializer.deserializeInt(from));
        if(type != null){
            return ServerBrowseRandomBattleMapAnswer.answerSuccess(writeBuffer, battleMarketManager.getRandomBattleMap(type));
        }else{
            return ServerBrowseRandomBattleMapAnswer.answerFailure(writeBuffer);
        }
    }
}
