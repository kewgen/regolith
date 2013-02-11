package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.*;
import com.geargames.regolith.managers.ServerTrainingBattleCreationManager;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleDeserializer;
import com.geargames.regolith.serializers.answer.ServerConfirmationAnswer;
import com.geargames.regolith.service.BattleManagerContext;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.units.BattleHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mkutuzov
 * Date: 13.07.12
 */
public class ServerGroupCompleteRequest extends MainOneToClientRequest {
    private BattleManagerContext battleManagerContext;
    private ServerTrainingBattleCreationManager battleCreationManager;

    public ServerGroupCompleteRequest() {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        this.battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        this.battleCreationManager = configuration.getBattleCreationManager();
    }

    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        Battle battle = battleManagerContext.getBattlesById().get(SimpleDeserializer.deserializeInt(from));

        if (battleManagerContext.getCreatedBattles().get(battle.getAuthor()) != null) {
            BattleAlliance alliance = BattleHelper.findAlliance(battle, SimpleDeserializer.deserializeInt(from));
            if (alliance == null) {
                throw new RegolithException();
            }
            BattleGroup group = BattleHelper.findBattleGroup(alliance, SimpleDeserializer.deserializeInt(from));
            if (group == null) {
                throw new RegolithException();
            }
            byte length = from.get();
            int[] warriorIds = new int[length];
            for (int i = 0; i < length; i++) {
                warriorIds[i] = SimpleDeserializer.deserializeInt(from);
            }
            Warrior[] warriors = ServerDataBaseHelper.getWarriorsByIds(warriorIds);
            if (warriors.length < length) {
                throw new RegolithException();
            }
            if (battleCreationManager.completeGroup(group, warriors)) {
                return ServerConfirmationAnswer.answerSuccess(writeBuffer, Packets.GROUP_COMPLETE);
            } else {
                return ServerConfirmationAnswer.answerFailure(writeBuffer, Packets.GROUP_COMPLETE);
            }
        } else {
            return ServerConfirmationAnswer.answerFailure(writeBuffer, Packets.GROUP_COMPLETE);
        }

    }
}
