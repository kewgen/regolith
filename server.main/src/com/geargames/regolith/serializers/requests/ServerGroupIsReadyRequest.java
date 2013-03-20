package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.managers.ServerTrainingBattleCreationManager;
import com.geargames.regolith.serializers.answer.ServerConfirmationAnswer;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.serializers.MainServerRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.service.*;
import com.geargames.regolith.units.BattleHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mkutuzov
 * Date: 13.07.12
 */
public class ServerGroupIsReadyRequest extends ServerRequest {
    private BattleManagerContext battleManagerContext;
    private ServerTrainingBattleCreationManager battleCreationManager;
    private ServerContext serverContext;

    public ServerGroupIsReadyRequest() {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        this.battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        this.battleCreationManager = configuration.getBattleCreationManager();
        this.serverContext = configuration.getServerContext();
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        List<SocketChannel> recipients;
        SerializedMessage message;
        Battle battle = battleManagerContext.getBattlesById().get(SimpleDeserializer.deserializeInt(from));
        BattleAlliance alliance = BattleHelper.findAlliance(battle, SimpleDeserializer.deserializeInt(from));
        if (alliance == null) {
            throw new RegolithException();
        }
        BattleGroup group = BattleHelper.findBattleGroup(alliance, SimpleDeserializer.deserializeInt(from));
        if (battleCreationManager.isReady(group)) {
            recipients = MainServerRequestUtils.recipientsByCreatedBattle(battle);
            message = ServerConfirmationAnswer.answerSuccess(to, Packets.GROUP_IS_READY);
        } else {
            recipients = MainServerRequestUtils.singleRecipientByClient(client);
            message = ServerConfirmationAnswer.answerFailure(to , Packets.GROUP_IS_READY);
        }
        List<MessageToClient> messages = new ArrayList<MessageToClient>(1);
        messages.add(new MainMessageToClient(recipients, message.serialize()));
        return messages;
    }

}
