package com.geargames.regolith.managers;

import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.regolith.serializers.answers.BattleClientIsCheating;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.BattleServiceConfigurationFactory;
import com.geargames.regolith.service.BattleServiceContext;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.ServerBattle;

/**
 * Created with IntelliJ IDEA.
 * User: Администратор
 * Date: 15.04.13
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
public class CommonBattleManager {


    public static void cheaterOnBattle(ServerBattle serverBattle, Account cheater){

    }

    public static void closeBattle(ServerBattle serverBattle) {
        BattleServiceContext context = BattleServiceConfigurationFactory.getConfiguration().getContext();
        for (Client tmp : serverBattle.getClients()) {
            context.removeClient(tmp.getChannel());
        }
        context.removeServerBattleById(serverBattle.getBattle().getId());
    }

}
