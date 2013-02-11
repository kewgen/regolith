package com.geargames.regolith;

import com.geargames.regolith.app.Regolith;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: kewgen
 * Date: 02.02.12
 * Time: 14:23
 * To change this template use File | Settings | File Templates.
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
