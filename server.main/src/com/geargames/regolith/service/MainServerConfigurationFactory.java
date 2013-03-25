package com.geargames.regolith.service;

import com.geargames.regolith.*;
import com.geargames.regolith.managers.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: mikhail.kutuzov
 * Date: 09.06.12
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */
public class MainServerConfigurationFactory {
    private static MainServerConfiguration configuration;

    public static MainServerConfiguration getConfiguration() {
        return getXmlServerConfiguration();
    }

    protected static MainServerConfiguration getXmlServerConfiguration()  {
        if (configuration == null) {
            synchronized (MainServerConfiguration.class) {
                if (configuration == null) {
                    InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("com/geargames/regolith/mainserver.cfg.xml");
                    try{
                        configuration = XmlTransmitter.getTransmitter().unmarshal(MainServerConfiguration.class, inputStream);
                    }catch (Exception e){
                        throw new RegolithRuntimeException(e);
                    }

                    org.hibernate.cfg.Configuration hConfiguration = new org.hibernate.cfg.Configuration();
                    hConfiguration = hConfiguration.configure("hibernate.cfg.xml");
                    configuration.setSessionFactory(hConfiguration.buildSessionFactory());

                    ServerCommonManager commonManager =new ServerCommonManager();
                    commonManager.setServerConfiguration(configuration);
                    configuration.setCommonManager(commonManager);
                    configuration.setBattleMarketManager(new ServerBattleMarketManager());
                    configuration.setBattleCreationManager(new ServerTrainingBattleCreationManager());
                    configuration.setBaseWarriorMarketManager(new ServerBaseWarriorMarketManager());

                    SessionFactory sessionFactory = configuration.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    BattleConfiguration battleConfiguration = (BattleConfiguration) session.get(BattleConfiguration.class, configuration.getBattleRevision());
                    BaseConfiguration baseConfiguration = (BaseConfiguration) session.get(BaseConfiguration.class, configuration.getBaseRevision());
                    session.close();
                    RegolithConfiguration regolithConfiguration = new RegolithConfiguration();
                    regolithConfiguration.setBaseConfiguration(baseConfiguration);
                    regolithConfiguration.setBattleConfiguration(battleConfiguration);
                    configuration.setRegolithConfiguration(regolithConfiguration);

                    MainServerContext serverContext = new MainServerContext(1);
                    serverContext.setBattleManagerContext(new ConcurrentBattleManagerContext());
                    configuration.setServerContext(serverContext);

                    configuration.setReader(new MainClientReader());
                    configuration.setWriter(new ClientWriter(configuration.getMessageOutputDataProcessorAmount()));

                    configuration.setBrowseBattlesSchedulerService(new BrowseBattlesSchedulerService());
                }
            }
        }
        return configuration;
    }
}
