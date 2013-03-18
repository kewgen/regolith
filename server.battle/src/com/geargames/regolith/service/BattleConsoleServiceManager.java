package com.geargames.regolith.service;

/**
 * User: mikhail v. kutuzov
 * Date: 13.08.12
 * Time: 10:33
 */
public class BattleConsoleServiceManager {
    public static void main(String[] args) throws Exception {
        if (args[0].equals("START")) {
            BattleServiceManager.runBattleService(args[1], args[2], args[3], Integer.valueOf(args[4]));
        } else if(args[0].equals("UNREGISTER")) {
            BattleServiceManager.unRegisterBattleService(args[1], args[2]);
        }
    }
}
