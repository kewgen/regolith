package com.geargames.regolith.application;

import com.geargames.MIDlet;
import com.geargames.common.env.SystemEnvironment;

public final class Regolith extends MIDlet {

    public void startApp() {
        Manager manager = (Manager)getManager();
        try {
            if (manager == null) {
                manager = Manager.getInstance(this);
            }
            manager.startApp();
        } catch (Exception e) {
            SystemEnvironment.getInstance().getDebug().exception(com.geargames.common.String.valueOfC("Start application problem"),e);
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

    public void facebookShare() {
    }

    public void sendPay(String str) {
    }

    @Override
    protected com.geargames.Manager getManager() {
        return Manager.getInstance();
    }
}