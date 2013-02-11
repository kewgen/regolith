package com.geargames.regolith.service;

/**
 * User: mikhail v. kutuzov
 * Date: 13.08.12
 * Time: 10:33
 */
public class BattleConsoleServiceManager {
    public static void main(String[] args) throws Exception {
        if (args[0].equals("START")) {
            BattleServiceManager.runBattleService(args[2], args[3], args[4], Integer.valueOf(args[5]));
        } else if(args[0].equals("UNREGISTER")) {
            BattleServiceManager.unRegisterBattleService(args[2], args[3]);
        }
    }
}
