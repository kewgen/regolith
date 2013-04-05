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
        int battleId = SimpleDeserializer.deserializeInt(from);
        Battle battle = battleManagerContext.getBattlesById().get(battleId);
        SerializedMessage message;
        List<SocketChannel> recipients;
        if (battleManagerContext.getCreatedBattles().get(battle.getAuthor()) != null) {
            int allianceId = SimpleDeserializer.deserializeInt(from);
            int victimAccountId = SimpleDeserializer.deserializeInt(from);
            Account victimAccount = serverContext.getActiveAccountById(victimAccountId);
            if (victimAccount == null) {
                throw new RegolithException();
            }
            if (client.getAccount() == battle.getAuthor() && victimAccount == client.getAccount()) {
                // Автор битвы пытается исключить из битвы сам себя
                recipients = MainServerRequestUtils.singleRecipientByClient(client);
                message = ServerEvictAccountFromAllianceAnswer.AnswerFailure(to);
            } else {
                if (client.getAccount() != battle.getAuthor() && client.getAccount() != victimAccount) {
                    throw new RegolithException();
                }
                BattleAlliance alliance = null;
                for (BattleAlliance battleAlliance : battle.getAlliances()) {
                    if (allianceId == battleAlliance.getId()) {
                        alliance = battleAlliance;
                        break;
                    }
                }
                if (alliance == null) {
                    throw new RegolithException();
                }
                if (battleCreationManager.evictAccount(alliance, victimAccount)) {
                    SocketChannel victimChannel = serverContext.getChannel(victimAccount);
                    Client victimClient = serverContext.getClient(victimChannel);
                    victimClient.setState(new ClientAtBattleMarket());

                    recipients = MainServerRequestUtils.recipientsByCreatedBattle(battle);
                    recipients.add(serverContext.getChannel(victimAccount));
					schedulerService.updateBattle(battle);
                    message = ServerEvictAccountFromAllianceAnswer.AnswerSuccess(to, victimAccount, alliance);
                } else {
                    recipients = MainServerRequestUtils.singleRecipientByClient(client);
                    message = ServerEvictAccountFromAllianceAnswer.AnswerFailure(to);
                }
            }
        } else {
            recipients = MainServerRequestUtils.singleRecipientByClient(client);
            message = ServerEvictAccountFromAllianceAnswer.AnswerFailure(to);
        }
        List<MessageToClient> messages = new LinkedList<MessageToClient>();
        messages.add(new MainMessageToClient(recipients, message.serialize()));
        return messages;
    }

}
