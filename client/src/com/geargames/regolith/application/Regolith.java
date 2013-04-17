package com.geargames.regolith.application;

import com.geargames.common.logging.Debug;
import com.geargames.platform.MIDlet;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;

public final class Regolith extends MIDlet {

    public Regolith() {
        super();
        setLocationRelativeTo(null);
    }

    @Override
    public void startApp() {
        Manager manager = (Manager)getManager();
        try {
            if (manager == null) {
                manager = Manager.getInstance(this);
            }
            manager.startApp();
        } catch (Exception e) {
            Debug.error("Start application problem", e);
        }
    }

    @Override
    protected com.geargames.platform.Manager getManager() {
        return Manager.getInstance();
    }

    @Override
    protected void formWindowClosing(java.awt.event.WindowEvent evt) {
        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();

        Debug.debug("The client going to logout");
        clientConfiguration.getCommonManager().logout();
//        clientConfiguration.setBaseConfiguration(null);
//        clientConfiguration.setBaseWarriors(null);
//        clientConfiguration.setAccount(null);
        Debug.debug("Disconnecting from the server...");
        clientConfiguration.getNetwork().disconnect();

        super.formWindowClosing(evt);
    }

}