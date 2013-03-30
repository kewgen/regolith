package com.geargames.regolith.application;

import com.geargames.common.logging.Debug;
import com.geargames.platform.MIDlet;

public final class Regolith extends MIDlet {

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
}