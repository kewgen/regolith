package com.geargames.regolith;

import com.geargames.regolith.service.MainServiceManager;
//import com.geargames.regolith.service.SimpleService;

/**
 * User: abarakov
 * Date: 02.04.13
 */
public class RunMainService {

    public static void main(String[] args) {
        try {
            /*SimpleService service = */MainServiceManager.runMainService();
            System.out.println("Server was started");
            // Вечный цикл
            while (true) {
                Thread.sleep(50);
            }
        } catch (Exception e) {
            e.printStackTrace();
//        } finally {
//            service.stopService();
        }
    }

}