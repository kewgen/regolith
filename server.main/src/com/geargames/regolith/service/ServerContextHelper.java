package com.geargames.regolith.service;

import com.geargames.regolith.service.remote.BattleServiceDescriptor;
import com.geargames.regolith.units.Account;

import java.util.Map;

/**
 * User: mikhail v. kutuzov
 * Date: 13.08.12
 * Time: 16:14
 */
public class ServerContextHelper {
    public static synchronized BattleServiceDescriptor getWastingBattleServiceAddress(ServerContext context){
        Map<String, Integer> powers = context.getBattleServicePowers();
        String minBattle = "";
        int min = 0;
        for(String battleServiceName : powers.keySet()){
            int value = powers.get(battleServiceName);
            if(min >= value){
                min = value;
                minBattle = battleServiceName;
            }
        }

        return context.getBattleServiceSocketPairs().get(minBattle);
    }

    public static synchronized BattleServiceDescriptor tookWastingBattleServiceAddress(ServerContext context){
        BattleServiceDescriptor battleServiceDescriptor = getWastingBattleServiceAddress(context);
        increaseBattleServicePower(context, battleServiceDescriptor.getName());
        return battleServiceDescriptor;
    }

    public static synchronized void addBattleService(ServerContext context, String battleServiceName, BattleServiceDescriptor battleServiceDescriptor){
        context.getBattleServicePowers().put(battleServiceName, 0);
        context.getBattleServiceSocketPairs().put(battleServiceName, battleServiceDescriptor);
    }

    public static synchronized void removeBattleService(ServerContext context, String battleServiceName){
        context.getBattleServiceSocketPairs().remove(battleServiceName);
        context.getBattleServicePowers().remove(battleServiceName);
    }

    public static synchronized void increaseBattleServicePower(ServerContext context, String battleServiceName){
        context.getBattleServicePowers().put(battleServiceName , context.getBattleServicePowers().get(battleServiceName) + 1);
    }

    public static synchronized void decreaseBattleServicePower(ServerContext context, String battleServiceName){
        context.getBattleServicePowers().put(battleServiceName , context.getBattleServicePowers().get(battleServiceName) - 1);
    }

    public static boolean isLoggedIn(Account account, ServerContext context){
        return context.getActiveAccountById(account.getId()) != null;
    }

}
