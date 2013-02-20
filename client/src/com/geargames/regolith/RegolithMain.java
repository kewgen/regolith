package com.geargames.regolith;

import com.geargames.regolith.application.Regolith;

import java.io.IOException;

/**
 * User: kewgen
 */

public class RegolithMain extends com.geargames.Main {


    public static void main(String[] args) throws IOException {
        LOG_FILE_NAME = "regolith";
        RegolithMain main = new RegolithMain();
        main.commonMain(args);
        Port.init();
        Regolith project = new Regolith();
        project.startApp();
    }


}
