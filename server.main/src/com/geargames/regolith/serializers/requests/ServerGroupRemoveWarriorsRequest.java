package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BattleHelper;
import com.geargames.regolith.managers.ServerTrainingBattleCreationManager;
import com.geargames.regolith.serializers.MainServerRequestUtils;
import com.geargames.regolith.serializers.answers.ServerGroupReadyStateAnswer;
import com.geargames.regolith.service.*;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * User: m.v.kutuzov
 * Date: 27.03.13
 */
public class ServerGroupRemoveWarriorsRequest extends ServerRequest {
    private BattleManagerContext battleManagerContext;
    private ServerTrainingBattleCreationManager battleCreationManager;

    public ServerGroupRemoveWarriorsRequest() {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        this.battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        this.battleCreationManager = configuration.getBattleCreationManager();
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        List<SocketChannel> recipients;
        SerializedMessage message;
        Battle battle = battleManagerContext.getBattlesById().get(SimpleDeserializer.deserializeInt(from));
        BattleAlliance alliance = BattleHelper.findAlliance(battle, SimpleDeserializer.deserializeInt(from));
        if (alliance != null) {
            BattleGroup group = BattleHelper.findBattleGroup(alliance, SimpleDeserializer.deserializeInt(from));
            if (group != null) {
                battleCreationManager.removeWarriors(group);
                if (battleCreationManager.isNotReady(group)) {
                    recipients = MainServerRequestUtils.recipientsByCreatedBattle(battle);
                    message = ServerGroupReadyStateAnswer.answerSuccess(to, Packets.GROUP_INCOMPLETE, group);

                    List<MessageToClient> messages = new ArrayList<MessageToClient>(1);
                    messages.add(new MainMessageToClient(recipients, message.serialize()));
                    return messages;
                }
            }
        }
        recipients = MainServerRequestUtils.singleRecipientByClient(client);
        message = ServerGroupReadyStateAnswer.answerFailure(to, Packets.GROUP_INCOMPLETE);
        List<MessageToClient> messages = new ArrayList<MessageToClient>(1);
        messages.add(new MainMessageToClient(recipients, message.serialize()));
        return messages;
    }
}
