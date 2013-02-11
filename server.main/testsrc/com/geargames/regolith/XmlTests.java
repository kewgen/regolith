package com.geargames.regolith;

import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import junit.framework.Assert;
import org.junit.Test;

import java.io.File;

/**
 * User: mkutuzov
 * Date: 14.06.12
 */
public class XmlTests {
    @Test
    public void write() throws Exception {
        MainServerConfiguration configuration = new MainServerConfiguration();
        configuration.setBattleRevision((short) 1);
        configuration.setBaseRevision((short) 1);
        configuration.setMessageOutputDataProcessorsAmount(3);
        File file = new File("testdata" + File.separator + "configuration.xml");

        XmlTransmitter.getTransmitter().marshal(configuration, file);
        MainServerConfiguration sc = XmlTransmitter.getTransmitter().unmarshal(MainServerConfiguration.class, file);
        Assert.assertEquals(sc.getMessageBufferSize(), configuration.getMessageBufferSize());
    }

    @Test
    public void readFullServerConfiguration() throws Exception {
        MainServerConfiguration serverConfiguration = MainServerConfigurationFactory.getConfiguration();
        Assert.assertNotNull(serverConfiguration.getSessionFactory());
        Assert.assertNotNull(serverConfiguration.getRegolithConfiguration().getBaseConfiguration().getProjectiles());
    }
}
