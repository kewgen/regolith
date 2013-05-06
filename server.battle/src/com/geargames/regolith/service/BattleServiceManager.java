package com.geargames.regolith.service;

import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.RegolithConfiguration;
import com.geargames.regolith.service.remote.BattleServiceRegister;
import com.geargames.regolith.service.remote.MainServiceFunctions;
import com.geargames.regolith.service.remote.BattleServiceDescriptor;

import java.rmi.Naming;

public class BattleServiceManager {
    private static final String MAIN_NAME = "MAIN";

    public static synchronized SimpleService runBattleService(String battleServiceName, String mainHost, String battleHost, int battleServicePort) throws Exception {
        BattleServiceRegister register = new BattleServiceRegister();
        Naming.rebind(battleServiceName, register);

        BattleServiceConfiguration configuration = BattleServiceConfigurationFactory.getConfiguration();

        SimpleService service = new SimpleService(battleServicePort, configuration.getReader());
        new Thread(service).start();

        BattleServiceDescriptor battleServiceDescriptor = new BattleServiceDescriptor();
        battleServiceDescriptor.setHost(battleHost);
        battleServiceDescriptor.setPort(battleServicePort);
        battleServiceDescriptor.setName(battleServiceName);

        MainServiceFunctions main = (MainServiceFunctions) Naming.lookup("rmi://" + mainHost + "/" + MAIN_NAME);
        RegolithConfiguration regolithConfiguration = main.register(battleServiceDescriptor);
        BattleConfiguration battleConfiguration = regolithConfiguration.getBattleConfiguration();
        battleConfiguration.setObserver(configuration.getObserver());
        battleConfiguration.setRouter(configuration.getRouter());
        configuration.setRegolithConfiguration(regolithConfiguration);

        configuration.getBattleSchedulerService().start();
        return service;
    }

    public static synchronized void unRegisterBattleService(String mainHost, String battleServiceName) throws Exception {
        MainServiceFunctions main = (MainServiceFunctions) Naming.lookup("rmi://" + mainHost + "/" + MAIN_NAME);
        main.unRegister(battleServiceName);
    }

}
