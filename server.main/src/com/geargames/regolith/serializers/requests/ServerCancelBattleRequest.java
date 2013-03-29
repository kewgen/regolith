package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.managers.ServerTrainingBattleCreationManager;
import com.geargames.regolith.serializers.MainServerRequestUtils;
import com.geargames.regolith.serializers.answers.ServerCancelBattleAnswer;
import com.geargames.regolith.service.*;
import com.geargames.regolith.service.states.ClientAtBase;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Users: mkutuzov, abarakov
 * Date: 13.07.12
 */
public class ServerCancelBattleRequest extends ServerRequest {
    private ServerTrainingBattleCreationManager battleCreationManager;
    protected BattleManagerContext battleManagerContext;
    protected ServerContext serverContext;
    protected Battle battle;
    private  BrowseBattlesSchedulerService schedulerService;

    public ServerCancelBattleRequest(Account account) {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        this.battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        this.serverContext = configuration.getServerContext();
        this.battle = configuration.getServerContext().getBattleManagerContext().getCreatedBattles().get(account);
        this.battleCreationManager = configuration.getBattleCreationManager();
        this.schedulerService = configuration.getBrowseBattlesSchedulerService();
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        List<MessageToClient> messages = new ArrayList<MessageToClient>(1);
        List<SocketChannel> recipients = MainServerRequestUtils.recipientsByCreatedBattle(battle);

        //todo: Удалить все сопутствующие с битвой данные:
        // саму битву, информацию о готовности боевых отрядов, высвободить нанятых бойцов, сменить стейт клиентам и т.д.
        SerializedMessage message = new ServerCancelBattleAnswer(to);
        if (battleManagerContext.getCreatedBattles().get(client.getAccount()) != null) {
            battleCreationManager.cancelBattle(client.getAccount());

            for (SocketChannel recipient : recipients) {
                Client recipientClient = serverContext.getClient(recipient);
                recipientClient.setState(new ClientAtBase());
            }
        } else {
            throw new RegolithException();
        }

        schedulerService.deleteBattle(battle);
        messages.add(new MainMessageToClient(recipients, message.serialize()));
        return messages;
    }

}
