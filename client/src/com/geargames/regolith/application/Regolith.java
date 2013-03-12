package com.geargames.regolith.application;

import com.geargames.ConsoleDebug;
import com.geargames.MIDlet;
import com.geargames.common.env.SystemEnvironment;

/*ObjC interface*///#import "MIDlet.h"
/*ObjC interface*///#import "Debug.h"
/*ObjC interface*///@class MIDlet;

/*ObjC uncomment*///#import "Manager.h"

public final class Regolith extends MIDlet {

    public void startApp() {
        Manager manager = (Manager)getManager();
        try {
            if (manager == null) {
                manager = Manager.getInstance(this);
            }
            manager.startApp();
        } catch (Exception e) {
            ((ConsoleDebug) SystemEnvironment.getInstance().getDebug()).logEx(e);
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
            ((ConsoleDebug) SystemEnvironment.getInstance().getDebug()).logEx(e);
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