package com.geargames.regolith.application;

import com.geargames.Debug;

import java.util.Enumeration;
import java.util.Vector;


//for ObjC


//прототип Timer только работающий на тиках
@Deprecated
public class Ticker {

    public static final int TICK_LAST_BUILDING = 10;
    public static final int TICK_LAST_BUILDING_LEN = 100;
    public static final int TICK_LAST_SAVE = 11;
    public static final int TICK_LAST_SAVE_LEN = 100;
    public static final int TICK_BUTTONS_SHOW = 12;
    public static final int TICK_BUTTONS_SHOW_LEN = 100;

    private static boolean DEBUG_T = false;

    public static void processTickers() {//ежесекундный проход
        if (tickers == null) return;
        for (Enumeration e = tickers.elements(); e.hasMoreElements();) {
            Ticker ticker = (Ticker) e.nextElement();

            if (ticker.getTickCount() < ticker.getTickCur() + ticker.getTickHold()) {
                if (ticker.isPeriodic()) {
                    ticker.setTickCur(0);
                } else {
                    if (DEBUG_T) {
                        Debug.log(com.geargames.common.String.valueOfC("Ticker.Remove.").concatC(toString(ticker)));
                    }
                    tickers.removeElement(ticker);
                }
            } else {
                ticker.nextTick();
            }
        }
    }

    public static void clearTickers() {
        tickers.removeAllElements();
    }

    public static void setTicker(int id, int len) {
        setTicker(id, len, 0, false);
    }

    public static void setTicker(int id, int len, int hold, boolean periodic) {

        Ticker ticker = Ticker.findTicker(id);

        if (ticker == null) {
            ticker = new Ticker(id, len, hold, periodic);
            tickers.addElement(ticker);
        } else {
            Ticker.resetTicker(id);
        }
        if (DEBUG_T) {
            Debug.log(com.geargames.common.String.valueOfC("Ticker.Set.").concatC(toString(ticker)));
        }
    }

    public static void resetTicker(int id) {
        Ticker ticker = findTicker(id);
        if (ticker != null) ticker.setTickCur(0);
    }

    public static boolean isTicker(int id) {
        return findTicker(id) != null;
    }

    public static void killTicker(int id) {
        Ticker ticker = findTicker(id);
        if (ticker != null) tickers.removeElement(ticker);
    }

    public static int getTickerElapsedTick(int id) {
        Ticker ticker = findTicker(id);
        return ticker == null ? 0 : ticker.getTickCount() - ticker.getTickCur() + ticker.getTickHold();
    }

    public static int getTickerTick(int id) {
        Ticker ticker = findTicker(id);
        return ticker == null ? 0 : ticker.getTickCur() - ticker.getTickHold();
    }

    private static Ticker findTicker(int id) {
        if (tickers == null) tickers = new Vector(16);
        for (Enumeration e = tickers.elements(); e.hasMoreElements();) {
            Ticker ticker = (Ticker) e.nextElement();
            if (ticker.getId() == id)
                return ticker;
        }
        return null;
    }

    private static java.lang.String toString(Ticker ticker) {
        return com.geargames.common.String.valueOfC(" id:").concatI(ticker.getId()).concatC(", len:").concatI(ticker.getTickCount()).concatC(", cur:").concatI(ticker.getTickCur()).toString();
    }

    private static Vector tickers;// = new Vector();



    private int tick_id;
    private int tick_cur;//текущий тик
    private int tick_count;
    private int tick_hold;//задержка запуска
    private boolean isPeriodic;

    private Ticker(int id_, int count_, int hold_, boolean periodic_) {
        tick_id = id_;
        tick_cur = 0;
        tick_count = count_;
        isPeriodic = periodic_;
        tick_hold = hold_;
        /*ObjC uncomment*///return self;
    }

    private int getId() {
        return tick_id;
    }

    private int getTickCur() {
        return tick_cur;
    }

    private void setTickCur(int tick) {
        tick_cur = tick;
    }

    private void nextTick() {
        tick_cur++;
    }

    private int getTickCount() {
        return tick_count;
    }

    private int getTickHold() {
        return tick_hold;
    }

    private boolean isPeriodic() {
        return isPeriodic;
    }


}
