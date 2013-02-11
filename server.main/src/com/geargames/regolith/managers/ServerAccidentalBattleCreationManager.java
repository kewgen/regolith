package com.geargames.regolith.managers;

import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;

/**
 * User: mikhail v. kutuzov
 * Date: 10.10.12
 * Time: 12:34
 */
public class ServerAccidentalBattleCreationManager {
    private MainServerConfiguration configuration;

    public ServerAccidentalBattleCreationManager() {
        configuration = MainServerConfigurationFactory.getConfiguration();
    }


}
