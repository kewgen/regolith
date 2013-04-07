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
        List<MessageToClient> messages = new LinkedList<MessageToClient>();
        if (battleManagerContext.getCreatedBattles().get(battle.getAuthor()) != null) {
            int allianceId = SimpleDeserializer.deserializeInt(from);
            int victimId = SimpleDeserializer.deserializeInt(from);
            Account victimAccount = serverContext.getActiveAccountById(victimId);
            if (victimAccount != null) {
                if (client.getAccount() == battle.getAuthor() || client.getAccount() == victimAccount) {
                    for (BattleAlliance alliance : battle.getAlliances()) {
                        if (allianceId == alliance.getId()) {
                            if (battleCreationManager.evictAccount(alliance, victimAccount)) {
                                recipients = MainServerRequestUtils.recipientsByCreatedBattle(battle);
                                schedulerService.updateBattle(battle);

                                message = ServerEvictAccountFromAllianceAnswer.AnswerSuccess(to, victimAccount, alliance);
                                messages.add(new MainMessageToClient(recipients, message.serialize()));
                                return messages;
                            }
                            break;
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
