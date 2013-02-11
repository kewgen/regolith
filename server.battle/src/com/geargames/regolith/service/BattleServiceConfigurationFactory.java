package com.geargames.regolith.service;

import com.geargames.regolith.RegolithRuntimeException;
import com.geargames.regolith.XmlTransmitter;

import java.io.InputStream;

public class BattleServiceConfigurationFactory {
    private static BattleServiceConfiguration configuration;

    public static BattleServiceConfiguration getConfiguration() {
        if (configuration == null) {
            synchronized (BattleServiceConfiguration.class) {
                if (configuration == null) {

                    InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("com/geargames/regolith/battleserver.cfg.xml");
                    try {
                        configuration = XmlTransmitter.getTransmitter().unmarshal(BattleServiceConfiguration.class, inputStream);
                    } catch (Exception e) {
                        throw new RegolithRuntimeException(e);
                    }

                    configuration.setContext(new ConcurrentBattleServiceContext());
                    configuration.setReader(new BattleServiceReader());
                    configuration.setWriter(new ClientWriter(configuration.getMessageOutputDataProcessorsAmount()));
                    configuration.setBattleSchedulerService(new BattleSchedulerService());
                }
            }
        }
        return configuration;
    }
}
