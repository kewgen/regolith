package com.geargames.regolith;

import com.geargames.common.logging.Logger;
import com.geargames.regolith.application.Regolith;

import java.io.IOException;

/**
 * User: kewgen
 */
public class RegolithMain extends com.geargames.platform.Main {

    public static void main(String[] args) throws IOException {
        Logger.logFileName = com.geargames.common.String.valueOfC("regolith");
        RegolithMain main = new RegolithMain();
        main.commonMain(args);
        Port.init();
        Regolith project = new Regolith();
        project.startApp();
    }

}
