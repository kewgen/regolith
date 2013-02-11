package com.geargames.regolith.app;


public class Etimer {
    private int eid;
    private long etime;//время отсчета
    private long ewait;
    private long edata;//время окончания

    public Etimer(int id_, long wait_, long data_, boolean periodic_) {
        eid = id_;
        etime = System.currentTimeMillis();
        ewait = wait_;
        long p = periodic_ ? 0x8000000000000000L : 0L;
        edata = (data_ & 0x7FFFFFFFFFFFFFFFL) | p;
        /*ObjC uncomment*///return self;
    }

    public int getId() {
        return eid;
    }

    public long getTime() {
        return etime;
    }

    public void setTime(long time) {
        this.etime = time;
    }

    public long getWait() {
        return ewait;
    }

    public void setWait(long new_wait) {//для ticker
        ewait = new_wait;
    }

    public long getData() {
        return edata;
    }

}