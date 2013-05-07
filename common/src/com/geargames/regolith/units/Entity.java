package com.geargames.regolith.units;

import com.geargames.regolith.serializers.SerializeHelper;

import java.io.Serializable;

/**
 * User: mkutuzov
 * Date: 07.02.12
 */
public class Entity implements Serializable {
    private int id;

    public Entity() {
        id = SerializeHelper.NULL_REFERENCE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id=" + id;
    }

}
