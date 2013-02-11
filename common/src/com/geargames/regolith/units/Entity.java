package com.geargames.regolith.units;

import java.io.Serializable;

/**
 * User: mkutuzov
 * Date: 07.02.12
 */
public class Entity  implements Serializable {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
