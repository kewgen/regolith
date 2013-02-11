package com.geargames.regolith.service.remote;

import java.io.Serializable;

/**
 * User: mikhail v. kutuzov
 * Date: 10.08.12
 * Time: 13:02
 */
public class BattleServiceDescriptor implements Serializable {
    private String name;
    private String host;
    private int port;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
