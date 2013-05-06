package com.geargames.regolith.units.map;

/**
 * Серверная реализация класса ящика с аммуницией.
 * User: abarakov
 * Date: 04.05.13
 */
public class ServerBox extends Box {
    private int frameId;

    @Override
    public int getFrameId() {
        return frameId;
    }

    @Override
    public void setFrameId(int frameId) {
        this.frameId = frameId;
    }

}
