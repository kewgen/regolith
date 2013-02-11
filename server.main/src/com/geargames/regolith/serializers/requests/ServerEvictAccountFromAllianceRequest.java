package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.managers.ServerTrainingBattleCreationManager;
import com.geargames.regolith.serializers.answers.ServerEvictAccountFromAllianceAnswer;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.serializers.MainServerRequestUtils;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleDeserializer;
import com.geargames.regolith.service.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * User: mkutuzov
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
            BattleAlliance alliance = null;
            int alliance_id = SimpleDeserializer.deserializeInt(from);
            Account victim = serverContext.getActiveAccountById(SimpleDeserializer.deserializeInt(from));
            for (BattleAlliance battleAlliance : battle.getAlliances()) {
                if (alliance_id == battleAlliance.getId()) {
                    if (victim == null) {
                        throw new RegolithException();
                    } else if (victim != client.getAccount() && client.getAccount() != battleAlliance.getBattle().getAuthor()) {
                        throw new RegolithException();
                    }
                    alliance = battleAlliance;
                    break;
                }
            }
            if (alliance == null) {
                throw new RegolithException();
            }
            battleCreationManager.evictAccount(alliance, victim);
            recipients = MainServerRequestUtils.recipientsByCreatedBattle(battle);
            message = ServerEvictAccountFromAllianceAnswer.AnswerSuccess(to, victim, alliance);
        } else {
            recipients = MainServerRequestUtils.singleRecipientByClient(client);
            message = ServerEvictAccountFromAllianceAnswer.AnswerFailure(to);
        }
        List<MessageToClient> messages = new LinkedList<MessageToClient>();
        messages.add(new MainMessageToClient(recipients, message.serialize()));
        return messages;
    }
}
