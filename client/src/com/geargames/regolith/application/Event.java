package com.geargames.regolith.application;

public class Event extends com.geargames.common.Event {

    public static final byte EVENT_TICK_10 = 1;//равен EVENT_TICK
    public static final byte EVENT_TICK_20 = 2;

    public Event(int uid_, int param_, Object data_, int x_, int y_) {
        super(uid_, param_, data_, x_, y_);
    }
}