package com.geargames.regolith.managers;

import com.geargames.regolith.service.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.ServerBattle;

/**
 * User: mvkutuzov
 * Date: 15.04.13
 * Time: 15:38
  */
public class CommonBattleManager {


    public static void cheaterOnBattle(ServerBattle serverBattle, Account cheater){

    }

    /**
     * Закрыть все соединения принадлежащие битве и прекратить жизненный цикл битвы.
     * @param serverBattle
     */
    public static void closeBattle(ServerBattle serverBattle) {
        BattleServiceConfiguration configuration = BattleServiceConfigurationFactory.getConfiguration();
        BattleServiceContext context = configuration.getContext();
        for (Client tmp : serverBattle.getClients()) {
            context.removeClient(tmp.getChannel());
        }
        context.removeServerBattleById(serverBattle.getBattle().getId());
        configuration.getBattleSchedulerService().remove(serverBattle);
    }

}
