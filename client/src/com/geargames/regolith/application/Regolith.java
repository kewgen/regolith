package com.geargames.regolith.application;

import com.geargames.common.logging.Debug;
import com.geargames.common.String;
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
            Debug.error(String.valueOfC(""), e);
        }
    }

    protected void onResume() {
    }

    public void onPause() {
        Manager manager = (Manager)getManager();
        if (manager != null) {
            manager.pauseApp();
        }
    }

    public void destroyApp(boolean b) {
        try {

        } catch (Exception e) {
            Debug.error(String.valueOfC(""), e);
        }

    }

    public void facebookShare() {
    }

    public void sendPay(String str) {
    }

    @Override
    protected com.geargames.platform.Manager getManager() {
        return Manager.getInstance();
    }
}