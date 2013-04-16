package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
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
    private ServerContext serverContext;

    public ServerEvictAccountFromAllianceRequest() {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        this.battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        this.serverContext = configuration.getServerContext();
    }

    public static List<MessageToClient> evictAccount(Battle battle, Account clientAccount, Account victimAccount, int allianceId, MicroByteBuffer buffer) {
        List<MessageToClient> messages = new LinkedList<MessageToClient>();
        if (clientAccount == battle.getAuthor() || clientAccount == victimAccount) {
            for (BattleAlliance alliance : battle.getAlliances()) {
                if (allianceId == alliance.getId()) {
                    MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
                    if (configuration.getBattleCreationManager().evictAccount(alliance, victimAccount)) {
                        List<SocketChannel> recipients = MainServerRequestUtils.recipientsByCreatedBattle(battle);
                        configuration.getBrowseBattlesSchedulerService().updateBattle(battle);

                        SerializedMessage message = ServerEvictAccountFromAllianceAnswer.AnswerSuccess(buffer, victimAccount, alliance);
                        messages.add(new MainMessageToClient(recipients, message.serialize()));
                        return messages;
                    }
                    break;
                }
            }
        }
        return messages;
    }

    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        int battleId = SimpleDeserializer.deserializeInt(from);
        Battle battle = battleManagerContext.getBattlesById().get(battleId);
        if (battleManagerContext.getCreatedBattles().get(battle.getAuthor()) != null) {
            int allianceId = SimpleDeserializer.deserializeInt(from);
            int victimId = SimpleDeserializer.deserializeInt(from);
            Account victimAccount = serverContext.getActiveAccountById(victimId);
            if (victimAccount != null) {
                return evictAccount(battle, client.getAccount(), victimAccount, allianceId, to);
            }
        }
        List<SocketChannel> recipients = MainServerRequestUtils.singleRecipientByClient(client);
        SerializedMessage message = ServerEvictAccountFromAllianceAnswer.AnswerFailure(to);
        List<MessageToClient> messages = new LinkedList<MessageToClient>();
        messages.add(new MainMessageToClient(recipients, message.serialize()));
        return messages;
    }

}
