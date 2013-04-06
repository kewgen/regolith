package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.managers.ServerTrainingBattleCreationManager;
import com.geargames.regolith.serializers.answers.ServerEvictAccountFromAllianceAnswer;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.serializers.MainServerRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.service.*;
import com.geargames.regolith.service.states.ClientAtBattleMarket;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * Users: mkutuzov, abarakov
 * Date: 13.07.12
 */
public class ServerEvictAccountFromAllianceRequest extends ServerRequest {
    private BattleManagerContext battleManagerContext;
    private ServerTrainingBattleCreationManager battleCreationManager;
    private ServerContext serverContext;
    private BrowseBattlesSchedulerService schedulerService;

    public ServerEvictAccountFromAllianceRequest() {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        this.battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        this.battleCreationManager = configuration.getBattleCreationManager();
        this.serverContext = configuration.getServerContext();
        this.schedulerService = configuration.getBrowseBattlesSchedulerService();
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        Battle battle = battleManagerContext.getBattlesById().get(SimpleDeserializer.deserializeInt(from));
        SerializedMessage message;
        List<SocketChannel> recipients;
        List<MessageToClient> messages = new LinkedList<MessageToClient>();
        if (battleManagerContext.getCreatedBattles().get(battle.getAuthor()) != null) {
            int alliance_id = SimpleDeserializer.deserializeInt(from);
            Account victim = serverContext.getActiveAccountById(SimpleDeserializer.deserializeInt(from));
            if (victim != null) {
                if (client.getAccount() == battle.getAuthor() || client.getAccount() == victim) {
                    for (BattleAlliance alliance : battle.getAlliances()) {
                        if (alliance_id == alliance.getId()) {
                            if (battleCreationManager.evictAccount(alliance, victim)) {
                                recipients = MainServerRequestUtils.recipientsByCreatedBattle(battle);
                                recipients.add(serverContext.getChannel(victim));
                                schedulerService.updateBattle(battle);

                                message = ServerEvictAccountFromAllianceAnswer.AnswerSuccess(to, victim, alliance);
                                messages.add(new MainMessageToClient(recipients, message.serialize()));
                                return messages;
                            }
                        }
                    }
                }
            }
        }
        recipients = MainServerRequestUtils.singleRecipientByClient(client);
        message = ServerEvictAccountFromAllianceAnswer.AnswerFailure(to);
        messages.add(new MainMessageToClient(recipients, message.serialize()));
        return messages;
    }

}
