package com.geargames.regolith;

import com.geargames.platform.PortPlatform;

/**
 * Порт проекта
 */
public class Port extends com.geargames.common.Port {

    public static void init() {
        setWH(800, 480);
        PortPlatform.init();
    }

}
