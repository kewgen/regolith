package com.geargames.regolith.application;

import com.geargames.Debug;
import com.geargames.Recorder;
import com.geargames.awt.TextHint;
import com.geargames.awt.timers.TimerManager;
import com.geargames.common.String;
import com.geargames.common.env.Environment;
import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PFontManager;
import com.geargames.common.util.ArrayByte;
import com.geargames.common.util.ArrayIntegerDual;
import com.geargames.env.ConsoleEnvironment;
import com.geargames.packer.Graphics;
import com.geargames.packer.Image;
import com.geargames.regolith.Port;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

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


public final class Application extends com.geargames.awt.Application {

    @Deprecated
    public static final int mult_fps = /*@MULT_FPS@*/2/*END*/;//1 2 4 = 6 12 24

    private final java.lang.String RMS_SETTINGS = "pfp";

    private Loader loader;
    private Render render;
    private PFontManager fontManager;
    private Environment environment;
    private PRegolithPanelManager panels;

    protected Graphics graphicsBuffer;
    protected Image i_buf;
    private boolean isDrawing;

    private java.lang.String loadLog = "";
    private Image splash;

    private boolean isLoading = true; // true, если данные загружаются
    private static int globalTick;

    private boolean vibrationEnabled;
    private boolean soundEnabled;

    private int userId, userMidletId;
    private int clientId;

    // ----- Instance management ---------------------------------------------------------------------------------------

    private static Application instance;

    // Конструктор
    private Application() {
        drawSplash();
        /*ObjC uncomment*///return self;
    }

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
            instance.setEnvironment(ConsoleEnvironment.getInstance());
        }
        return instance;
    }

    // ----- Property management ---------------------------------------------------------------------------------------

    public Render getRender() {
        return render;
    }

    public PFontManager getFontManager() {
        return fontManager;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Graphics getGraphics() {
        return graphicsBuffer;
    }

    // ----- Application start, init, stop -----------------------------------------------------------------------------

    public void loading() {
        tSleep = environment.currentTimeMillis();

        Debug.log(String.valueOfC("Memory total, free: ").
                concatL(/*Debug.formatSize*/(environment.totalMemory())).concatC(", ").
                concatL(/*Debug.formatSize*/(environment.freeMemory())));

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

        PFont baseFont = fontManager.getFont(0);
        PFontCollection.initiate(fontManager, baseFont);

        drawSplash(String.valueOfC("Init network..."));

        initPreferenceOnStart();
        isLoading = false;

        Manager.getInstance().setLSK(Manager.SK_OK);
        Manager.getInstance().setRSK(Manager.SK_EXIT);

        TextHint textHint = TextHint.getInstance();
        textHint.setSkinObject(render.getFrame(Graph.OBJ_HINT), render, 16, 24, 16, 24); //todo: Установить правильный скин и размеры
//        textHint.setDefaultFont(PFontCollection.getFontHint());

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

    // --------------- Main loop ---------------------------------------------------------------------------------------

    public void mainLoop() {
        try {
            if (Manager.getInstance().isSuspended() || isLoading) {
                Manager.paused(10);
                return;
            }
            eventProcess();
            TimerManager.update();

            draw(graphicsBuffer);

            if (true/* || this.equals(manager.getDisplay())*/) {
                isDrawing = true;
                /*ObjC uncomment*///is_drawing = false;
                Manager.getInstance().repaintStart();
                Thread.yield();
                while (isDrawing) {
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
            long timeElapsed = environment.currentTimeMillis() - tSleep;//реальная задержка
            long paused = timeFPS - timeElapsed;//делаем затержку для выдерживания фпс
            if (timeElapsed <= 0) {
                timeElapsed = 1;
            }
            if (paused > 0) {
                Manager.paused(paused);
            }
            fps_cur = (fps_cur + 1000 / (timeElapsed/* - (paused > 0 ? paused : 0)*/)) / 2;
            tSleep = environment.currentTimeMillis();
        } else {
            Manager.paused(1);
        }

        if (arr_side) {
            arr_tick++;
        } else {
            arr_tick--;
        }
        if (arr_tick > 2) {
            arr_side = false;
        } else if (arr_tick == 0) {
            arr_side = true;
        }
    }

//    public static boolean isTick() {
//        return (globalTick % mult_fps == 0) ? true : false;
//    }
//
//    public static int getTick() {
//        return globalTick;
//    }

    // --------------- Drawing -----------------------------------------------------------------------------------------

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

    public Image getBuffer() {
        return i_buf;
    }

    public boolean isDrawing() {
        return isDrawing;
    }

    public void setIsDrawing(boolean isDrawing) {
        this.isDrawing = isDrawing;
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

    public void drawSplash(String str) {
        loadLog = str.toString();
        drawSplash();
    }

    private void draw(Graphics graphics) {
        graphics.setColor(0xffffff);
        graphics.fillRect(0, 0, i_buf.getWidth(), i_buf.getHeight());
        panels.draw(graphics);
        /*ObjC uncomment*///if ([Port isOpenGL]) [gles_view paintEnd];
    }

    // ----- Event handlers --------------------------------------------------------------------------------------------

    /**
     * Выполнение всех манипуляций на один игровой тик
     */
    protected void onEvent(com.geargames.common.Event event) {
        panels.onEvent(event.getUid(), event.getParam(), event.getX(), event.getY());
    }

}
