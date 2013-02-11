package com.geargames.regolith.app;


import com.geargames.Debug;
import com.geargames.MIDlet;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: kewgen
 * Date: 13.01.12
 * Time: 13:11
 */
public class TablesLoader extends com.geargames.common.TablesLoader {

    //загружаемые файлы
    private java.lang.String fileTTX = "/da";
    private java.lang.String fileTextes = "/ds";

    public void loadTables(MIDlet midlet) {
    }

    protected void loadTables(DataInputStream dis) {
        try {
            switch (getType()) {
                case TYPE_INT://из одного файла несколько интовых таблиц
                    attractionsTTX = (int[][]) loadTable(dis);
                    servicesTTX = (int[][]) loadTable(dis);
                    roadsTTX = (int[][]) loadTable(dis);
                    decorationsTTX = (int[][]) loadTable(dis);

                    int[][] array = (int[][]) loadTable(dis);
                    scoreToNextLevel = new int[array.length];
                    for (int row = 0; row < array.length; row++) scoreToNextLevel[row] = array[row][0];
                    array = null;

                    array = (int[][]) loadTable(dis);
                    costExpansion = new int[array.length];
                    for (int row = 0; row < array.length; row++) costExpansion[row] = array[row][0];
                    array = null;

                    break;
                case TYPE_STRING://смешанная таблица с байтами и стрингами
                    textes = loadTablesAsObjects(dis);
                    textIndexed();
                    break;
            }
        } catch (IOException ex) {
            Debug.logEx(ex);
        }
    }

    private void textIndexed() {
        Object[][] array = (Object[][]) textes;
        text = new String[array.length];
        int counter = 0;
        textIndexes = new int[STR_COUNT];
        for (int row = 0; row < array.length; row++) {
            int a = (Byte) array[row][0];
            if (a == 1) {
                textIndexes[counter] = row;
                counter++;
            }
            text[row] = (String) array[row][1];
        }
        STR_TUTOR_HELLO = textIndexes[0];
        STR_TUTOR_FIRSTVISITOR = textIndexes[1];
        STR_TUTOR_DESIRE = textIndexes[2];
        STR_TUTOR_ADDATTRACTION = textIndexes[3];
        STR_TUTOR_UPGRADE = textIndexes[4];
        STR_TUTOR_TICKETCOSR = textIndexes[5];
        STR_TUTOR_BENCH = textIndexes[6];
        STR_TUTOR_ADDDECORATION = textIndexes[7];
        STR_TUTOR_KASSA = textIndexes[9];
        STR_TUTOR_CRASH = textIndexes[10];
        STR_TUTOR_TRASH = textIndexes[11];
        STR_TUTOR_FIGHT = textIndexes[12];
        STR_TUTOR_EXPANSION = textIndexes[13];
        STR_TUTOR_HINTS = textIndexes[14];
        STR_PANELPURCHASE = textIndexes[15];
        STR_NAMES_ATTR = textIndexes[16];
        STR_NAMES_SERV = textIndexes[17];
        STR_NAMES_ROAD = textIndexes[18];
        STR_NAMES_DECOR = textIndexes[19];
    }

    public int[] getTTX(byte type, int sid) {//int[]
        int[] array = null;
/*
        switch (type) {
            case Building.TYPE_ATTRACTION:
                array = attractionsTTX[sid];
                break;
            case Building.TYPE_SERVICES:
                array = servicesTTX[sid];
                break;
        }
*/
        return array;
    }

    public int[] getScoreToNextLevel() {
        return scoreToNextLevel;
    }

    public int[] getCostExpansion() {
        return costExpansion;
    }

    public String getText(int ind) {
        return text[ind];
    }

    public int getTextIndex(int ind) {//начало группы текстов
        return textIndexes[ind];
    }

    public int[] getTextIndexes() {
        return textIndexes;
    }

    public void clean() {
        //attractionsTTX = null;
    }

    private int[][] attractionsTTX;
    private int[][] servicesTTX;
    private int[][] roadsTTX;
    private int[][] decorationsTTX;
    private int[] scoreToNextLevel;
    private int[] costExpansion;
    private Object textes;
    private String[] text;

    private int[] textIndexes;//хранит указатели на начала групп строк
    public static int STR_TUTOR_HELLO;
    public static int STR_TUTOR_FIRSTVISITOR;
    public static int STR_TUTOR_DESIRE;
    public static int STR_TUTOR_ADDATTRACTION;
    public static int STR_TUTOR_UPGRADE;
    public static int STR_TUTOR_TICKETCOSR;
    public static int STR_TUTOR_BENCH;
    public static int STR_TUTOR_ADDDECORATION;
    public static int STR_TUTOR_NULL;
    public static int STR_TUTOR_KASSA;
    public static int STR_TUTOR_CRASH;//10
    public static int STR_TUTOR_TRASH;
    public static int STR_TUTOR_FIGHT;
    public static int STR_TUTOR_EXPANSION;
    public static int STR_TUTOR_HINTS;
    public static int STR_PANELPURCHASE;
    public static int STR_NAMES_ATTR;
    public static int STR_NAMES_SERV;
    public static int STR_NAMES_ROAD;
    public static int STR_NAMES_DECOR;
    public static final int STR_COUNT = 20;


    private TablesLoader() {
        /*ObjC uncomment*///return self;
    }

    private static TablesLoader self;

    public synchronized static TablesLoader getInstance() {
        if (self == null) self = new TablesLoader();
        return self;
    }


}
