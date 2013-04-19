package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.*;
import com.geargames.regolith.helpers.BattleHelper;
import com.geargames.regolith.helpers.ServerDataBaseHelper;
import com.geargames.regolith.managers.ServerTrainingBattleCreationManager;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.MainServerRequestUtils;
import com.geargames.regolith.serializers.answers.ServerGroupReadyStateAnswer;
import com.geargames.regolith.service.*;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mkutuzov
 * Date: 13.07.12
 */
public class ServerGroupAddWarriorsRequest extends ServerRequest {
    private BattleManagerContext battleManagerContext;
    private ServerTrainingBattleCreationManager battleCreationManager;

    public ServerGroupAddWarriorsRequest() {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        this.battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        this.battleCreationManager = configuration.getBattleCreationManager();
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        Battle battle = battleManagerContext.getBattlesById().get(SimpleDeserializer.deserializeInt(from));
        List<SocketChannel> recipients;
        SerializedMessage message;

        if (battleManagerContext.getCreatedBattles().get(battle.getAuthor()) != null) {
            BattleAlliance alliance = BattleHelper.findAllianceById(battle, SimpleDeserializer.deserializeInt(from));
            if (alliance != null) {
                BattleGroup group = BattleHelper.findBattleGroupById(alliance, SimpleDeserializer.deserializeInt(from));
                if (group != null) {
                    byte length = from.get();
                    int[] warriorIds = new int[length];
                    for (int i = 0; i < length; i++) {
                        warriorIds[i] = SimpleDeserializer.deserializeInt(from);
                    }
                    Warrior[] warriors = ServerDataBaseHelper.getWarriorsByIds(warriorIds);
                    if (warriors.length == length) {
                        if (battleCreationManager.addWarriors(group, warriors) && battleCreationManager.isReady(group)) {
                            recipients = MainServerRequestUtils.recipientsByCreatedBattle(battle);
                            message = ServerGroupReadyStateAnswer.answerSuccess(to, Packets.GROUP_COMPLETE, group);

                            List<MessageToClient> messages = new ArrayList<MessageToClient>(1);
                            messages.add(new MainMessageToClient(recipients, message.serialize()));
                            return messages;
                        }
                    }
                }
            }
        }
        recipients = MainServerRequestUtils.singleRecipientByClient(client);
        message = ServerGroupReadyStateAnswer.answerFailure(to, Packets.GROUP_COMPLETE);

        List<MessageToClient> messages = new ArrayList<MessageToClient>(1);
        messages.add(new MainMessageToClient(recipients, message.serialize()));
        return messages;
    }
}
