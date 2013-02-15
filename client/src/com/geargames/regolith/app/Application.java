package com.geargames.regolith.app;


import com.geargames.Debug;
import com.geargames.Recorder;
import com.geargames.common.String;
import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PFontComposite;
import com.geargames.common.packer.PFontManager;
import com.geargames.common.util.ArrayByte;
import com.geargames.common.util.ArrayIntegerDual;
import com.geargames.packer.Graphics;
import com.geargames.packer.Image;
import com.geargames.regolith.Port;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Enumeration;
import java.util.Vector;


/*ObjC uncomment*///#import "Map.h"
/*ObjC uncomment*///#import "Image.h"
/*ObjC uncomment*///#import "Recorder.h"
/*ObjC uncomment*///#import "SoundPlayer.h"
/*ObjC uncomment*///#import "Loader.h"
/*ObjC uncomment*///#import "Util.h"
/*ObjC uncomment*///#import "Event.h"
/*ObjC uncomment*///#import "Etimer.h"
/*ObjC uncomment*///#import "Exception.h"
/*ObjC uncomment*///#import "Canvas.h"
/*ObjC uncomment*///#import "Manager.h"
/*ObjC uncomment*///#import "Ticker.h"
/*ObjC uncomment*///#import "pfp2ViewController.h"
/*ObjC uncomment*///#import "GLView.h"


public final class Application {
    public static final int mult_fps = /*@MULT_FPS@*/2/*END*/;//1 2 4 = 6 12 24

    // Состояние регистрации
    public Loader loader;
    public PFontManager fontManager;

    private boolean vibrationEnabled;
    private boolean soundEnabled;

    public int userId, userMidletId;
    public int clientId;

    private boolean isLoading = true;//данные загружаются
    public Render render;
    private static int globalTick;

    private java.lang.String loadLog = "";
    private Image splash;

    private PFont font5;
    private PFont font6;
    private PFont font7;
    private PFont font8;
    private PFont font9;
    private PFont font10;
    private PFont font11;
    private PFont baseFont;

    private PRegolithPanelManager panels;

    public PFont getFont5() {
        return font5;
    }

    public PFont getFont6() {
        return font6;
    }

    public PFont getFont7() {
        return font7;
    }

    public PFont getFont8() {
        return font8;
    }

    public PFont getFont9() {
        return font9;
    }

    public PFont getFont10() {
        return font10;
    }

    public PFont getFont11() {
        return font11;
    }

    public PFont getBaseFont() {
        return baseFont;
    }

    private static Application instance;

    // Конструктор
    private Application() {
        drawSplash();
        /*ObjC uncomment*///return self;
    }

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    public void drawSplash(String str) {
        loadLog = str.toString();
        drawSplash();
    }

    private void drawSplash() {
        try {
            if (graphicsBuffer == null) {
                createScreenBuffer(Port.getW(), Port.getH());
            }

            if (splash == null) {
                splash = Image.createImage(String.valueOfC("/s1.png"), Manager.getInstance());
            }
            graphicsBuffer.drawImage(splash, splash.getWidth() / 2, splash.getHeight() / 2, Graphics.HCENTER | Graphics.VCENTER);
            graphicsBuffer.setColor(0);
            //todo установить фонт!
            graphicsBuffer.drawString(String.valueOfC(loadLog), 5, Port.getH() - 20, 0);
            Manager.getInstance().repaintStart();
            Thread.yield();
            Manager.paused(10);
        } catch (Exception ex) {
            Debug.logEx(ex);
        }
    }

    public void createScreenBuffer(int w, int h) {
        try {
            i_buf = Image.createImage(w, h);
            graphicsBuffer = i_buf.getGraphics();
            if (render != null) {
                getGraphics().setRender(render);
            }
        } catch (Exception ex) {
            Debug.logEx(ex);
        }
    }

    public void loading() {
        tSleep = System.currentTimeMillis();

        Debug.log(String.valueOfC("Memory total,free:").concatL(Manager.getTotalMemory()).concatC(",").concatL(Manager.getFreeMemory()));

        loader = new Loader(Manager.getInstance());
        render = new Render();

        render.setCreator(new PRegolithUnitCreator());
        render.create();
        loader.loadPacker(graphicsBuffer, render);
        getGraphics().setRender(render);

        fontManager = new PFontManager();

        ArrayIntegerDual fontIndexes = new ArrayIntegerDual(1, 2);

        fontIndexes.set(0, 0, Graph.SPR_FONT_SYMB);
        fontIndexes.set(0, 1, Graph.SPR_FONT_RU);

        fontManager.init(render, fontIndexes);


        baseFont = fontManager.getFont(0);
        font11 = fontManager.createReSizedFont((PFontComposite) baseFont, 11);
        font10 = fontManager.createReSizedFont((PFontComposite) baseFont, 10);
        font9 = fontManager.createReSizedFont((PFontComposite) baseFont, 9);
        font8 = fontManager.createReSizedFont((PFontComposite) baseFont, 8);
        font7 = fontManager.createReSizedFont((PFontComposite) baseFont, 7);
        font6 = fontManager.createReSizedFont((PFontComposite) baseFont, 6);
        font5 = fontManager.createReSizedFont((PFontComposite) baseFont, 5);

        drawSplash(String.valueOfC("Init network..."));

        initPreferenceOnStart();
        isLoading = false;

        Manager.getInstance().setLSK(Manager.SK_OK);
        Manager.getInstance().setRSK(Manager.SK_EXIT);

        panels = PRegolithPanelManager.getInstance();
        panels.initiate(render);
        panels.show(panels.getMainMenu());
        panels.show(panels.getLeft());
/*
        panels.show(panels.getHeadline());
*/
        panels.show(panels.getRight());

    }

    public void resetPreference() {
        vibrationEnabled = true;
        soundEnabled = true;
    }

    private void initPreferenceOnStart() {
        if (!loadOptionsRMS()) {
            resetPreference();
            saveOptionsRMS();
        }
    }

    protected void onStop(boolean correct) {
        Debug.log(String.valueOfC("onStop.disconnect"));
        if (correct) {
            saveOptionsRMS();
        }
        Debug.trace("Application.onStop");
    }

    public void destroy(boolean correct) {
        Debug.log(String.valueOfC("destroy ").concatC(correct ? "correct" : "uncorrect"));
        Manager.getInstance().destroy(correct);
    }

    private final java.lang.String RMS_SETTINGS = "pfp";

    public boolean saveOptionsRMS() {
        boolean res = false;
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);

            dos.writeByte(1);
            dos.writeBoolean(vibrationEnabled);
            dos.writeBoolean(soundEnabled);
            dos.writeInt(userId);
            dos.writeInt(clientId);
            dos.writeInt(userMidletId);

            res = Recorder.RMSStoreSave(RMS_SETTINGS, baos, false);

            Debug.log(String.valueOfC("Rms.Prefs saved: vibra:").concatC(vibrationEnabled ? "On" : "Off").concatC(" sound:").concatC(soundEnabled ? "On" : "Off").concatC(" userId:").concatI(userId).concatC(" clientId:").concatI(clientId));
        } catch (Exception e) {
            Debug.trace(String.valueOfC("Save prefs "), e);
            res = false;
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                Debug.logEx(e);
            }
            return res;
        }
    }

    public boolean loadOptionsRMS() {
        boolean res = false;

        try {
            ArrayByte bData = Recorder.RMSStoreRead(RMS_SETTINGS, false);
            if (bData != null) {
                ByteArrayInputStream bais = new ByteArrayInputStream(bData.getArray());
                DataInputStream dis = new DataInputStream(bais);
                try {
                    byte version = dis.readByte();
                    if (version == 1) {
                        vibrationEnabled = dis.readBoolean();
                        soundEnabled = dis.readBoolean();
                        userId = dis.readInt();
                        clientId = dis.readInt();
                        userMidletId = dis.readInt();
                        res = true;

                        Debug.log(String.valueOfC("Rms.Prefs load:"));
                        Debug.log(String.valueOfC(" id:").concatI(userId));
                    }
                } catch (Exception e) {
                    Debug.trace(String.valueOfC("RMSLoad prefs "), e);
                    Recorder.RMSStoreClean(RMS_SETTINGS);
                    return false;
                }
                if (dis != null) {
                    dis.close();
                }
                /*ObjC uncomment*///[bais release];
                /*ObjC uncomment*///[dis release];
            }
            return res;
        } catch (Exception e) {
            Debug.trace(String.valueOfC("RMSLoad stream "), e);
            return false;
        }
    }

    public PFontManager getFontManager() {
        return fontManager;
    }

    // ------------ EVENTS CONTROL -------------
    // Очередь сообщений, дублёр предназначен для исключения добавления нового события в момент обработки списка событий
    private Vector msgQueue;// = new Vector(64);

    public void eventAdd(int eventid, int param, Object data) {
        eventAdd(eventid, param, data, 0, 0);
    }

    public void eventAdd(int eventid, int param, Object data, int x, int y) {
        if (msgQueue == null) {
            msgQueue = new Vector(64);
        }
        boolean normal = msgQueue.size() < 64;
        if (!normal) {
            Debug.warning(String.valueOfC("queue length exceed 64 events"));
        }
        Event event = new Event(eventid, param, data, x, y);
        msgQueue.addElement(event);
    }

    protected void eventProcess() {
        if (msgQueue == null) {
            return;
        }
        try {
            while (!msgQueue.isEmpty()) {
                Event event = (Event) msgQueue.firstElement();
                msgQueue.removeElement(event);//перед вызовом события нужно его убрать из очереди на случай эксепта
                onEvent(event);
                event = null;//ObjC
            }
            if (Application.isTimer(Manager.TIMERID_KEYDELAY) && !Application.isTimer(Manager.TIMERID_KEYREPEAT))//TODO сделать один интервал на все фпс
                eventAdd(Event.EVENT_KEY_REPEATED, Manager.getInstance().getPressedKey(), null);
        } catch (Exception e) {
            Debug.logEx(e);
        }
    }


    // ------------ TIMERS CONTROL -------------
    private static Vector timers;

    //Timers
    public final static int TIMER_CONTROLPACKET = 0x000001FF;//отправка контрольного пакета
    public final static int TIMER_CONTROLPACKET_MS = 30 * 1000;

    public static boolean isTimer(int timerId) {
        return Application.findTimer(timerId) != null;
    }

    private static Etimer findTimer(int timerId) {
        if (timers == null) {
            timers = new Vector(16);
        }
        for (Enumeration e = timers.elements(); e.hasMoreElements(); ) {
            Etimer timer = (Etimer) e.nextElement();
            if (timer.getId() == timerId)
                return timer;
        }
        Debug.trace("getTimerElapsedTime. Timer " + timerId + " not found");
        return null;
    }

    public final void setTimer(int timerId, long interval) {
        Application.setTimer(timerId, interval, 0, false);
    }

    public static final void setTimer(int timerId, long interval, long data, boolean periodic) {
        Etimer timer = Application.findTimer(timerId);
        if (timer == null) {
            timer = new Etimer(timerId, interval, data, periodic);
            timers.addElement(timer);
        }
    }

    public final void resetTimer(int timerId) {
        Etimer timer = Application.findTimer(timerId);
        if (timer != null) timer.setTime(System.currentTimeMillis());
    }

    public final void killTimer(int timerId) {
        for (Enumeration e = timers.elements(); e.hasMoreElements(); ) {
            Etimer timer = (Etimer) e.nextElement();
            if (timer.getId() == timerId) {
                timers.removeElement(timer);
                return;
            }
        }
    }

    public final long getTimerElapsedTime(int timerId) {
        Etimer timer = Application.findTimer(timerId);
        return System.currentTimeMillis() - timer.getTime();
    }

    public final boolean isTimerExpired(int timerId) {
        Etimer timer = Application.findTimer(timerId);
        long timedelta = (System.currentTimeMillis() - timer.getTime());
        return timedelta >= timer.getWait();
    }

    public final void clearTimers() {
        timers.removeAllElements();
    }

    public void processTimers() {
        if (timers == null) return;
        for (Enumeration e = timers.elements(); e.hasMoreElements(); ) {
            Etimer timer = (Etimer) e.nextElement();

            long timedelta = (System.currentTimeMillis() - timer.getTime());
            if (timedelta >= timer.getWait()) {
                int timer_ = timer.getId();
                eventAdd(Event.EVENT_TIMER_END, timer_, timer);
                if ((timer.getData() & 0x8000000000000000L) != 0) {
                    timer.setTime(System.currentTimeMillis());
                } else {
                    timers.removeElement(timer);
                }
            }
        }
    }

    // ---------------MAIN LOOP------------------

    protected Graphics graphicsBuffer;
    protected Image i_buf;
    private boolean is_drawing;
    private int time_delay_ai;
    private int time_delay_render;

    public void mainLoop() {
        try {

            if (Manager.getInstance().isSuspended() || isLoading) {
                Manager.paused(10);
                return;
            }
            long time_delay_ai_start = System.currentTimeMillis();
            processTimers();
            Ticker.processTickers();
            eventProcess();
            panels.event(Event.EVENT_TICK, 0, 0, 0);
            time_delay_ai = (int) (System.currentTimeMillis() - time_delay_ai_start);

            long time_delay_render_start = System.currentTimeMillis();
            draw(graphicsBuffer);
            time_delay_render = (int) (System.currentTimeMillis() - time_delay_render_start);

            if (true/* || this.equals(manager.getDisplay())*/) {
                is_drawing = true;
                /*ObjC uncomment*///is_drawing = false;
                Manager.getInstance().repaintStart();
                Thread.yield();
                while (is_drawing) {
                    Thread.yield();
                    Manager.paused(5);
                }
            }
            int fps = 8 * mult_fps;
            manageFPS(fps);
            globalTick++;
        } catch (Exception e) {
            Debug.logEx(e);
        }
    }

    private int arr_tick = 0;
    private boolean arr_side = true;
    private long tSleep;
    private long fps_cur;

    public void manageFPS(int fps) {
        if (fps != 0) {
            int timeFPS = (1000 / fps);//задержка для установленного фпс
            long timeElapsed = System.currentTimeMillis() - tSleep;//реальная задержка
            long paused = timeFPS - timeElapsed;//делаем затержку для выдерживания фпс
            if (timeElapsed <= 0) timeElapsed = 1;
            if (paused > 0) {
                Manager.paused(paused);
            }
            fps_cur = (fps_cur + 1000 / (timeElapsed/* - (paused > 0 ? paused : 0)*/)) / 2;
            tSleep = System.currentTimeMillis();
        } else {
            Manager.paused(1);
        }

        if (arr_side) arr_tick++;
        else arr_tick--;
        if (arr_tick > 2) arr_side = false;
        else if (arr_tick == 0) arr_side = true;
    }

    public static boolean isTick() {
        return (globalTick % mult_fps == 0) ? true : false;
    }

    public static int getTick() {
        return globalTick;
    }


    private void draw(Graphics g) {
        g.setColor(0xffffff);
        g.fillRect(0, 0, i_buf.getWidth(), i_buf.getHeight());
        panels.draw(g);
        /*ObjC uncomment*///if ([Port isOpenGL]) [gles_view paintEnd];
    }

    public Image getBuffer() {
        return i_buf;
    }

    public boolean isDrawing() {
        return is_drawing;
    }

    public void setIsDrawing(boolean is_drawing_) {
        this.is_drawing = is_drawing_;
    }

    protected void onEvent(Event event) {
        int code = event.getUid();
        int param = event.getParam();

        if (code == Event.EVENT_TIMER_END) {
            switch (param) {
                case TIMER_CONTROLPACKET:
                    return;
            }
        }
        panels.event(code, param, event.getX(), event.getY());
    }

    public Graphics getGraphics() {
        return graphicsBuffer;
    }
}
