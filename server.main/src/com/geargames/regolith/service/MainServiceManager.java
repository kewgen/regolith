package com.geargames.regolith.service;

import com.geargames.regolith.service.remote.MainServiceRegister;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Naming;

/**
 * User: mikhail v. kutuzov
 * Date: 29.08.12
 * Time: 17:36
 */
public class MainServiceManager {
    private static Logger logger = LoggerFactory.getLogger(MainServiceManager.class);
    private static SimpleService service;
    private static final String MAIN_NAME = "MAIN";

    public static synchronized SimpleService runMainService() throws Exception {
        MainServiceRegister register = new MainServiceRegister();
        Naming.rebind(MAIN_NAME, register);

        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        service = new SimpleService(configuration.getPort(), configuration.getReader());
        new Thread(service).start();
        configuration.getBrowseBattlesSchedulerService().start();
        logger.debug("Main service was started port {}", configuration.getPort() );
        return service;
    }

}
