package com.geargames.regolith.app;

import java.util.Random;
import java.lang.Exception;


public class myUtil {

    //Encryption ******************************************************
    private static final boolean SET_ENCRIPTION = true;

    private static final String chashstring_ = "hSKs73lDFJd92jJdf92jSL0dk22e";
    private static final int ckeycode_ = 648292674;

    public static void paused(long pause) {
        try {
            if (pause < 0) pause = 0;
            Thread.yield();
            Thread.sleep(pause);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Random rnd;

    public static int getRandom() {
        if (rnd == null) rnd = new Random(1);
        return rnd.nextInt();
    }

    public static int getRandom(int max) {
        return getRandom(0, max);
    }

    public static int getRandom(int min, int max) {//генерация числа от min до max-1
        if (max == 0) return 0;
        int average = max - min;
        int r = getRandom();
        int res = min + (Math.abs(r) % average);
        return res;
    }

    public static void copy(byte[] source, byte[] destination){
        int size = source.length > destination.length ? destination.length : source.length;
        for(int i = 0; i < size; i++){
            destination[i] = source[i];
        }
    }

}