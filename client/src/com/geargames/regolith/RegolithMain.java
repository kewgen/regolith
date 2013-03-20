package com.geargames.regolith;

import com.geargames.common.logging.Logger;
import com.geargames.common.util.Recorder;
import com.geargames.regolith.application.Regolith;

import java.io.IOException;

/**
 * Users: mikhail v. kutuzov, abarakov
 */
public class RegolithMain extends com.geargames.platform.Main {

    public static void main(String[] args) throws IOException {
        Logger.logFileName         = "regolith";
        Recorder.storageFolder     = "data.storage";
        Recorder.storageProperties = "property.storage";

        RegolithMain main = new RegolithMain();
        main.commonMain(args);
        Port.init();
        Regolith project = new Regolith();
        project.startApp();
    }

}
