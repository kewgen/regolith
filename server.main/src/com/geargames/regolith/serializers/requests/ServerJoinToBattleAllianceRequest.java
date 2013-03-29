package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.managers.ServerTrainingBattleCreationManager;
import com.geargames.regolith.serializers.answers.ServerJoinToBattleAllianceAnswer;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.serializers.MainServerRequestUtils;
import com.geargames.regolith.service.*;
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
public class ServerJoinToBattleAllianceRequest extends ServerRequest {
    private BattleManagerContext battleManagerContext;
    private ServerTrainingBattleCreationManager battleCreationManager;
    private ServerContext serverContext;
    private BrowseBattlesSchedulerService browseBattlesSchedulerService;

    public ServerJoinToBattleAllianceRequest() {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        this.battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        this.battleCreationManager = configuration.getBattleCreationManager();
        this.serverContext = configuration.getServerContext();
        this.browseBattlesSchedulerService = configuration.getBrowseBattlesSchedulerService();
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        Battle battle = battleManagerContext.getBattlesById().get(SimpleDeserializer.deserializeInt(from));
        BattleAlliance alliance = null;
        List<SocketChannel> recipients;
        SerializedMessage message;
        if (battleManagerContext.getCreatedBattles().get(battle.getAuthor()) != null) {
            int alliance_id = SimpleDeserializer.deserializeInt(from);
            for (BattleAlliance battleAlliance : battle.getAlliances()) {
                if (battleAlliance.getId() == alliance_id) {
                    alliance = battleAlliance;
                    break;
                }
            }
            if (alliance != null) {
                BattleGroup group = battleCreationManager.joinToAlliance(alliance, client.getAccount());
                if (group != null) {
                    recipients = MainServerRequestUtils.recipientsByCreatedBattle(battle);
                    browseBattlesSchedulerService.updateBattle(battle);
                    message = ServerJoinToBattleAllianceAnswer.AnswerSuccess(to, group, client.getAccount());
                } else {
                    recipients = MainServerRequestUtils.singleRecipientByClient(client);
                    message = ServerJoinToBattleAllianceAnswer.AnswerFailure(to);
                }
            } else {
                throw new RegolithException();
            }
        } else {
            recipients = new ArrayList<SocketChannel>(1);
            recipients.add(serverContext.getChannel(client.getAccount()));
            message = ServerJoinToBattleAllianceAnswer.AnswerFailure(to);
        }
        List<MessageToClient> messages = new ArrayList<MessageToClient>(1);
        messages.add(new MainMessageToClient(recipients, message.serialize()));
        return messages;
    }
}
