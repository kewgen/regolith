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

    public ServerEvictAccountFromAllianceRequest() {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        this.battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        this.battleCreationManager = configuration.getBattleCreationManager();
        this.serverContext = configuration.getServerContext();
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        Battle battle = battleManagerContext.getBattlesById().get(SimpleDeserializer.deserializeInt(from));
        SerializedMessage message;
        List<SocketChannel> recipients;
        if (battleManagerContext.getCreatedBattles().get(battle.getAuthor()) != null) {
            int alliance_id = SimpleDeserializer.deserializeInt(from);
            Account victim = serverContext.getActiveAccountById(SimpleDeserializer.deserializeInt(from));
            if (victim == null) {
                throw new RegolithException();
            }
            if (client.getAccount() == battle.getAuthor() && victim == client.getAccount()) {
                // Автор битвы пытается исключить из битвы сам себя
                recipients = MainServerRequestUtils.singleRecipientByClient(client);
                message = ServerEvictAccountFromAllianceAnswer.AnswerFailure(to);
            } else {
                if (client.getAccount() != battle.getAuthor() && client.getAccount() != victim) {
                    throw new RegolithException();
                }
                BattleAlliance alliance = null;
                for (BattleAlliance battleAlliance : battle.getAlliances()) {
                    if (alliance_id == battleAlliance.getId()) {
                        alliance = battleAlliance;
                        break;
                    }
                }
                if (alliance == null) {
                    throw new RegolithException();
                }
                if (battleCreationManager.evictAccount(alliance, victim)) {
                    recipients = MainServerRequestUtils.recipientsByCreatedBattle(battle);
                    recipients.add(serverContext.getChannel(victim));
                    message = ServerEvictAccountFromAllianceAnswer.AnswerSuccess(to, victim, alliance);
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
