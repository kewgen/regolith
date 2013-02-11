package com.geargames.regolith.service;

/**
 * User: mikhail v. kutuzov
 * Date: 29.08.12
 * Time: 17:59
 */
public class ConsoleMainServiceManager {
    public static void main(String[] args) throws Exception {
        if (args[0].equals("START")){
            MainServiceManager.runMainService();
        }
    }
}
