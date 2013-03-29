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

}
