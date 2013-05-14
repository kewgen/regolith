package com.geargames.regolith.serializers.requests;

import com.geargames.common.logging.Debug;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.ServerBattleHelper;
import com.geargames.regolith.service.*;
import com.geargames.regolith.service.states.ClientNotLoggedIn;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * User: abarakov
 * Date: 16.04.13
 */
public class ServerLogoutAtBattleCreationRequest extends ServerRequest {
    private BrowseBattlesSchedulerService schedulerService;

    public ServerLogoutAtBattleCreationRequest() {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        this.schedulerService = configuration.getBrowseBattlesSchedulerService();
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        MainServerConfiguration serverConfiguration = MainServerConfigurationFactory.getConfiguration();
        ServerContext serverContext = serverConfiguration.getServerContext();
        List<MessageToClient> messages = null;

        Account activeAccount = serverConfiguration.getServerContext().getActiveAccountById(client.getAccount().getId());
        if (activeAccount != null) {
            if (client.getAccount() != activeAccount) {
                Debug.error("ServerSimpleLogoutRequest.clientRequest(): client.getAccount() != activeAccount");
            }

            BattleManagerContext battleManagerContext = serverContext.getBattleManagerContext();

            Battle createdBattle = battleManagerContext.getCreatedBattles().get(client.getAccount());
            if (createdBattle != null) {
                // Клиент является автором одной из битв => отменим битву и оповестим об этом всех ее подписчиков

                battleManagerContext.getBattleListeners().get(createdBattle).remove(client.getAccount());
                //todo: при выполнении запроса в список реципиентов попадает сам клиент, его следует от туда исключить
                messages = new ServerCancelBattleRequest().request(from, to, client);
            } else {
                Battle listenedBattle = battleManagerContext.getBattlesByAccount().get(client.getAccount());
                if (listenedBattle != null) {
                    // Клиент подписан на одну из битв
                    BattleGroup battleGroup = ServerBattleHelper.findBattleGroupByAccountId(listenedBattle, client.getAccount().getId());
                    if (battleGroup != null) {
                        // Клиент является участником битвы и занимает одну из боевых групп
//                        messages = new ServerEvictAccountFromAllianceRequest().request(from, to, client);
                        battleManagerContext.getBattleListeners().get(listenedBattle).remove(client.getAccount());
                        messages = ServerEvictAccountFromAllianceRequest.evictAccount(
                                listenedBattle, client.getAccount(), client.getAccount(), battleGroup.getAlliance().getId(), to);
                        //todo: при выполнении запроса в список реципиентов попадает сам клиент, его следует от туда исключить
                    }

                    serverConfiguration.getBattleCreationManager().doNotListenToBattle(listenedBattle, client.getAccount());
//                    messages = new ServerDoNotListenToCreatedBattleRequest().request(from, to, client);
                }
            }

            schedulerService.removeListener(client);

            // Клиент все еще подключен => разлогиним его
            serverConfiguration.getCommonManager().logout(activeAccount);
            client.setState(new ClientNotLoggedIn());
            serverContext.removeChannel(client.getAccount());
            serverContext.removeClient(client.getChannel());
        } else {
            // Клиент послал логаут, будучи не залогиненым
            //todo: как на это реагировать?
        }

        if (messages != null) {
//            messages.remove(client.getAccount());
        } else {
            messages = new ArrayList<MessageToClient>(0);
        }
        return messages;
    }

}
