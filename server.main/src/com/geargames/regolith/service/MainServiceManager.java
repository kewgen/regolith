package com.geargames.regolith.service;

import com.geargames.regolith.service.remote.MainServiceRegister;

import java.rmi.Naming;

/**
 * User: mikhail v. kutuzov
 * Date: 29.08.12
 * Time: 17:36
 */
public class MainServiceManager {
    private static SimpleService service;
    private static final String MAIN_NAME = "MAIN";

    public static synchronized SimpleService runMainService() throws Exception {
        MainServiceRegister register = new MainServiceRegister();
        Naming.rebind(MAIN_NAME, register);

        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        service = new SimpleService(configuration.getPort(), configuration.getReader());
        new Thread(service).start();

        return service;
    }

}
