package com.geargames.regolith;

import java.util.Enumeration;
import java.util.Vector;


public class Util {

    //Encryption ******************************************************
    private static final boolean SET_ENCRIPTION = true;

    private static final String chashstring = "hSKs73lDFJd92jJdf92jSL0dk22e";
    private static final int ckeycode = 648292674;

    private static byte[] csetXor(int ckeycode, String chashstring) {
        byte[] cxor = new byte[chashstring.length()];
        for (int i = 0; i < cxor.length; i++) {
            cxor[i] = (byte) ((byte) (chashstring.charAt(i)) ^ (ckeycode >> (8 * (i % 4))));
        }
        return cxor;
    }

    private static void cdecoder(byte[] data, int start_pos) {
        coder(data, start_pos, data.length, 0, chashstring);
    }

    private static void cdecoder(byte[] data) {
        coder(data, 0, data.length, 0, chashstring);
    }

    public static void coder(byte[] data, int ckeycode, String chashstring) {
        coder(data, 0, data.length, ckeycode, chashstring);
    }

    public static void coder(byte[] data, int start_pos, int len, int ckeycode, String chashstring) {
        if (!SET_ENCRIPTION) return;
        //ckeycode &= 0xffffffff;
        boolean DEBUG = false;
        if (data.length == 42661 && false) DEBUG = true;
        if (len > 1000) len = 1000;//шифруем только начало пакета, экономим процессор
        if (DEBUG) clog("1 " + ckeycode, data, start_pos, len);
//        System.out.println("---ckeycode" + ckeycode);
        byte[] cxor = csetXor(ckeycode, chashstring);
        int cxor_pos = 0;
        for (int i = start_pos; i < start_pos + len; i++) {
            data[i] = (byte) (data[i] ^ cxor[cxor_pos]);
            cxor_pos = (cxor_pos + 1) % cxor.length;
        }
        if (DEBUG) clog("2 " + ckeycode, data, start_pos, len);
    }

    private static int[] ixor = {5, 12, 3, 5, 7, 1, 2, 9, 6, 4, 5, 2, 11, 8, 4};

    public static String coder(String str) {
        StringBuffer cstr = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            cstr.append(str.charAt(i));
        }
        return cstr.toString();
    }

    private static void clog(String str, byte[] data, int start_pos, int len) {
        String code = "";
        for (int i = start_pos; i < start_pos + len; i++) {
            code = code.concat(String.valueOf(data[i] & 0xff)).concat(" ");
        }
        System.out.println(str + " " + code);
    }

    //System ***********************************************************
    public static void paused(long pause) {
        try {
            if (pause < 0) pause = 0;
            Thread.yield();
            Thread.sleep(pause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static java.util.Random rnd = new java.util.Random(1);

    public static int getRandom() {
        return rnd.nextInt();
    }

    public static int getRandom(int max) {
        return getRandom(0, max);
    }

    public static int getRandom(int min, int max) {
        if (max == 0) return 0;
        int average = max - min;
        int r = rnd.nextInt();
        int res = min + (Math.abs(r) % average);
        return res;
    }

    //Constants
    public final static byte NUM_0 = 0;
    public final static byte NUM_1 = 1;

    //Ticker ********************************************************
    private static Vector tickers = new Vector();
    private static final byte TIMER_ID = 0;
    private static final byte TIMER_CUR = 1;
    private static final byte TIMER_LEN = 2;
    private static final byte TIMER_HOLD = 3;
    private static final byte TIMER_CICLE = 4;
    private static final byte TIMER_SIZE = 5;

    public static boolean isTicker(int id) {
        int[] ticker = findTicker(id);
        boolean is_ticker = ticker != null && ticker[TIMER_HOLD] == 0;
//        if (is_ticker) Debug.trace("Util.isTicker, id:" + id + ", len:" + ticker[TIMER_LEN] + ", hold:" + ticker[TIMER_HOLD] + ", tick:" + Application.global_tick);
        return is_ticker;
    }

    private static int[] findTicker(int id) {
        for (int i = 0; i < tickers.size(); i++) {
            int[] ticker = (int[]) tickers.elementAt(i);
            if (ticker[TIMER_ID] == id)
                return ticker;
        }
        return null;
    }

    public static void setTicker(int id, int len, int hold) {
        setTicker(id, len, hold, false);
    }
    public static void setTicker(int id, int len, int hold, boolean cicle) {

        int[] ticker = findTicker(id);

        if (ticker == null) {
            ticker = new int[TIMER_SIZE];
            ticker[TIMER_ID] = id;
            tickers.addElement(ticker);
        }
        ticker[TIMER_CUR] = 0;
        ticker[TIMER_LEN] = len;
        ticker[TIMER_HOLD] = hold;
        ticker[TIMER_CICLE] = cicle ? 1 : 0;
//        Debug.trace("Util.setTicker, id:" + id + ", len:" + len + ", hold:" + hold + ", tick:" + Application.global_tick);
//        long p = periodic ? 0x8000000000000000L : 0L;
//        ticker[3] = (data & 0x7FFFFFFFFFFFFFFFL) | p;
    }

    public static void resetTicker(int id) {
        int[] ticker = findTicker(id);
        if (ticker != null) ticker[TIMER_CUR] = 0;
    }

    public static void killTicker(int id) {
        for (int i = 0; i < tickers.size(); i++) {
            int[] ticker = (int[]) tickers.elementAt(i);
            if (ticker[TIMER_ID] == id) {
                tickers.removeElementAt(i);
                return;
            }
        }
    }

    public static int getTickerElapsedTick(int id) {
        int ticker[] = findTicker(id);
        return ticker[TIMER_LEN] + ticker[TIMER_HOLD] - ticker[TIMER_CUR];
    }

    public static int getTickerTick(int id) {
        int ticker[] = findTicker(id);
        if (ticker == null) return 0;
        return ticker[TIMER_CUR];
    }

    public static boolean isTickerExpired(int id) {
        int ticker[] = findTicker(id);
        return ticker[TIMER_CUR] >= ticker[TIMER_LEN];
    }

    public static void clearTickers() {
        tickers.removeAllElements();
    }

    public static void processTickers() {
        for (Enumeration e = tickers.elements(); e.hasMoreElements();) {
            int[] ticker = (int[]) e.nextElement();

            if (ticker[TIMER_HOLD] > 0) {
                ticker[TIMER_HOLD]--;
                continue;
            }
            ticker[TIMER_CUR]++;
            if (ticker[TIMER_CUR] >= ticker[TIMER_LEN]) {
//                Debug.trace("Util.removeTicker, id:" + ticker[TIMER_ID] + ", len:" + ticker[TIMER_LEN] + ", tick:" + Application.global_tick);
//                postEvent(Event.TIMER, (int) ticker[0], ticker);
                if (ticker[TIMER_CICLE] == 1) {
                    ticker[TIMER_CUR] = 0;
                } else {
                    tickers.removeElement(ticker);
                }
            }
        }
    }


}
