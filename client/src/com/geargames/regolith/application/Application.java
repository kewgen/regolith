package com.geargames.regolith.application;

import com.geargames.awt.TextHint;
import com.geargames.common.timers.TimerManager;
import com.geargames.common.env.Environment;
import com.geargames.common.logging.Debug;
import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PFontManager;
import com.geargames.common.util.ArrayIntegerDual;
import com.geargames.common.util.Recorder;
import com.geargames.platform.packer.Graphics;
import com.geargames.platform.packer.Image;
import com.geargames.regolith.Port;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

public final class Application extends com.geargames.common.Application {

    public static final int FPS_MAXIMUM = 30;

    private Loader loader;
    private Render render;
    private PFontManager fontManager;
    private boolean vibrationEnabled;
    private boolean soundEnabled;

    private int userId, userMidletId;
    private int clientId;
    private boolean isLoading = true; // true, если данные загружаются

    protected Graphics graphicsBuffer;
    protected Image i_buf;
    private boolean isDrawing;

    private java.lang.String loadLog = "";
    private Image splash;

    private PRegolithPanelManager panels;
    private static Application instance;

    private Application() {
        drawSplash();
    }

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    // ----- Property management ---------------------------------------------------------------------------------------

    public PFontManager getFontManager() {
        return fontManager;
    }


    private void drawSplash() {
        try {
            if (graphicsBuffer == null) {
                createScreenBuffer(Port.getW(), Port.getH());
            }

            if (splash == null) {
                splash = Image.createImage("/s1.png", Manager.getInstance());
            }
            graphicsBuffer.drawImage(splash, splash.getWidth() / 2, splash.getHeight() / 2);
            graphicsBuffer.setColor(0);
            //todo установить фонт!
            graphicsBuffer.drawString(loadLog, 5, Port.getH() - 20, 0);
            Manager.getInstance().repaintStart();
            Thread.yield();
            Manager.pause(10);
        } catch (Exception ex) {
            Debug.error("A splash drawing problems", ex);
        }
    }

    public void createScreenBuffer(int w, int h) {
        try {
            i_buf = Image.createImage(w, h);
            graphicsBuffer = new Graphics(i_buf.getImage());
        } catch (Exception ex) {
            Debug.critical("Exception during the creation of screen buffer", ex);
        }
    }

    public Graphics getGraphics() {
        return graphicsBuffer;
    }

    // ----- Application start, init, stop -----------------------------------------------------------------------------

    public void loading() {
        tSleep = Environment.currentTimeMillis();

        Debug.config("Memory total, free: "+Environment.totalMemory()+", "+Environment.freeMemory());

        loader = new Loader(Manager.getInstance());
        render = new Render();

        render.setCreator(new PRegolithUnitCreator());
        render.create();
        loader.loadPacker(graphicsBuffer, render);

        Environment.setRender(render);
        fontManager = new PFontManager();

        ArrayIntegerDual fontIndexes = new ArrayIntegerDual(1, 2);

        fontIndexes.set(0, 0, Graph.SPR_FONT_SYMB);
        fontIndexes.set(0, 1, Graph.SPR_FONT_RU);

        fontManager.init(render, fontIndexes);

        PFont baseFont = fontManager.getFont(0);
        PFontCollection.initiate(fontManager, baseFont);

        drawSplash("Init network...");

        initPreferenceOnStart();
        isLoading = false;

        Manager.getInstance().setLSK(Manager.SK_OK);
        Manager.getInstance().setRSK(Manager.SK_EXIT);

        TextHint textHint = TextHint.getInstance();
        textHint.setSkinObject(render.getObject(Graph.OBJ_HINT));
//        textHint.setDefaultFont(PFontCollection.getFontHint());

        panels = PRegolithPanelManager.getInstance();

        panels.initiate(render);
        panels.changeScreen(panels.getMainScreen());
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
        Debug.debug("onStop.disconnect");
        if (correct) {
            saveOptionsRMS();
        }
        Debug.debug("Application.onStop");
    }

    public void destroy(boolean correct) {
        Debug.debug("destroy "+(correct ? "correct" : "uncorrect"));
        Manager.getInstance().destroy(correct);
    }

    public boolean saveOptionsRMS() {
       boolean result = true;
       try {
            Recorder.storeIntegerProperty("version",1);
            Recorder.storeBooleanProperty("vibration", vibrationEnabled);
            Recorder.storeBooleanProperty("sound", vibrationEnabled);
            Recorder.storeIntegerProperty("user_id", userId);
            Recorder.storeIntegerProperty("client_id", clientId);
            Recorder.storeIntegerProperty("midlet", userMidletId);

            Debug.debug("Rms.Prefs saved: vibra:" + (vibrationEnabled ? "On" : "Off")+" sound:"+(soundEnabled ? "On" : "Off")+" userId:"+userId+" clientId:"+clientId);
        } catch (Exception e) {
            Debug.error("Save prefs ", e);
            result = false;
        }
        return result;

    }

    public boolean loadOptionsRMS() {
        boolean result = false;
        try {
                try {
                    int version = Recorder.loadIntegerProperty("version");
                    if (version == 1) {
                        vibrationEnabled = Recorder.loadBooleanProperty("vibration");
                        soundEnabled = Recorder.loadBooleanProperty("sound");
                        userId = Recorder.loadIntegerProperty("user_id");
                        clientId = Recorder.loadIntegerProperty("client_id");
                        userMidletId = Recorder.loadIntegerProperty("midlet");
                        result = true;

                        Debug.debug("Rms.Prefs load:");
                        Debug.debug(" id:"+userId);
                    }
                } catch (Exception e) {
                    Debug.error("RMSLoad prefs ", e);
                    return false;
                }

            return result;
        } catch (Exception e) {
            Debug.error("RMSLoad stream ", e);
            return false;
        }
    }

    // --------------- Main loop ---------------------------------------------------------------------------------------

    public void mainLoop() {
        try {
            if (Manager.getInstance().isSuspended() || isLoading) {
                Manager.pause(10);
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
                    Manager.pause(5);
                }
            }
            manageFPS(FPS_MAXIMUM);
        } catch (Exception e) {
            Debug.error("Main loop exception.", e);
        }
    }

    private int arr_tick = 0;
    private boolean arr_side = true;
    private long tSleep;
    private long fps_cur;

    public void manageFPS(int fps) {
        if (fps != 0) {
            int timeFPS = (1000 / fps);//задержка для установленного фпс
            long timeElapsed = Environment.currentTimeMillis() - tSleep;//реальная задержка
            long paused = timeFPS - timeElapsed;//делаем затержку для выдерживания фпс
            if (timeElapsed <= 0) {
                timeElapsed = 1;
            }
            if (paused > 0) {
                Manager.pause(paused);
            }
            fps_cur = (fps_cur + 1000 / (timeElapsed/* - (paused > 0 ? paused : 0)*/)) / 2;
            tSleep = Environment.currentTimeMillis();
        } else {
            Manager.pause(1);
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

    public Image getBuffer() {
        return i_buf;
    }

    public boolean isDrawing() {
        return isDrawing;
    }

    public void setIsDrawing(boolean isDrawing) {
        this.isDrawing = isDrawing;
    }

    public void drawSplash(String str) {
        loadLog = str.toString();
        drawSplash();
    }

    private void draw(Graphics graphics) {
        graphics.setColor(0xffffff);
        graphics.fillRect(0, 0, i_buf.getWidth(), i_buf.getHeight());
        panels.draw(graphics);
    }

    // ----- Event handlers --------------------------------------------------------------------------------------------

    /**
     * Выполнение всех манипуляций на один игровой тик
     */
    protected void onEvent(com.geargames.common.Event event) {
        panels.onEvent(event.getUid(), event.getParam(), event.getX(), event.getY());
    }

}
