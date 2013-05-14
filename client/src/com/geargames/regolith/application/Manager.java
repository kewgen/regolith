package com.geargames.regolith.application;

import com.geargames.common.logging.Debug;
import com.geargames.platform.MIDlet;
import com.geargames.platform.packer.Canvas;
import com.geargames.platform.packer.Display;
import com.geargames.common.Graphics;
import com.geargames.regolith.Port;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

public final class Manager extends com.geargames.platform.Manager implements Runnable {

    // ****************************** Settings ******************************
    private Application app;
    private MIDlet midlet;
    private volatile boolean running;
    private boolean isSuspended;//поток приостановлен
    private Thread thread;

    private byte okLabelN;
    private byte cancelLabelN;
    private boolean enabledOkKey = true;
    private boolean enabledCancelKey = true;

    // ------------------ Manage ------------------------

    private static Manager self_manger;
    private Canvas canvas;

    private Manager(MIDlet midlet_) {
        initCanvas(midlet_);
        self_manger = this;
        midlet = midlet_;
        isSuspended = false;
    }

    public static Manager getInstance(MIDlet midlet_) {
        if (self_manger == null) self_manger = new Manager(midlet_);
        return self_manger;
    }

    public static Manager getInstance() {
        return self_manger;
    }

    public static Manager getManager() {//для андроида после выхода из спячки
        return self_manger;
    }


    public void create() {
        isSuspended = false;

        canvas.detectKeys();
        canvas.setFullScreenMode(true);
//        Recorder.setContext(midlet);
        app = Application.getInstance();
        loading();
    }

    public void loading() {
        app.loading();
    }


    // ------------------ Screen ------------------------
    public void paint(Graphics g) {
        if (Port.IS_CONSOLE) {
            g.onCache(5000);
        }
        try {
            if (app == null) {
                return;
            } else {
                g.drawImage(app.getBuffer(), Port.SCREEN_DX, Port.SCREEN_DY);
            }
        } catch (Exception e) {
            Debug.error("Could not draw an image", e);
        }
        app.setIsDrawing(false);
    }


    public void repaintStart() {
        canvas.repaintStart();
        canvas.repaint();
        midlet.repaint();
    }

    public void resizeScreenBuffer(int w, int h) {//изменение размеров экранного буфера
        if (app == null) return;
        Port.setWH(w, h);
        app.createScreenBuffer(w, h);
        PRegolithPanelManager.getInstance().onScreenResize();
    }


    // ------------MIDLET EVENTS HANDLERS--------
    public void startApp() {// Обработчик события startApp мидлета
        isSuspended = false;
        create();
        if (Port.OPEN_GL && Port.IS_ANDROID) {//вызов потока аи и рендера делает OpenGL
            run_();
            //onResume();//оживляем GLSurface
        } else {
            startMainThread();
        }
    }

    public void pauseApp() {// Обработчик события pauseApp мидлета
        isSuspended = true;
        //onPause();
        Display.getDisplay(midlet).setCurrent(null);
    }

    @Override
    public void onStop() {
        destroy(true);
    }


    public void destroy(boolean correct) {
        stopMainThread();
        if (Port.OPEN_GL && Port.IS_ANDROID) runStop();
        midlet.notifyDestroyed();
    }


    // ------------THREAD CONTROL----------------
    public void startMainThread() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stopMainThread() {
        running = false;
        thread = null;
        Debug.debug("Manager.stopMainThread");
    }

    public void run_() {
        Debug.debug("Manager.run_");
        running = true;
        runStart();
    }

    public void run() {
        Debug.debug("Manager.run - Main thread running");
        running = true;
        try {
            runStart();
            while (getRunning()) {
                mainLoop();
            }
            runStop();
        } catch (Exception e) {
            stopMainThread();
            Debug.error("Main loop problems", e);
        }
    }

    public boolean getRunning() {
        return running;
    }

    public void loadTextures() {//OpenGL загрузка текстур
    }

    public void mainLoop() {//Вызов основного игрового цикла. Он должен вызывать рендер нужных фреймов
        app.mainLoop();
    }

    protected final void runStart() {
        try {
            isSuspended = false;
            Display.getDisplay(midlet).setCurrent(this);
        } catch (Exception e) {
            e.printStackTrace();
            Debug.debug("onStart error [FILELINE]");
        }
    }

    protected final void runStop() {
        try {
            Debug.debug("runStop");
            app.onStop(true);
            //midlet.notifyDestroyed();
        } catch (Exception e) {

            Debug.error("Error during stop [FILELINE]", e);
        }
    }


    // ------------KEYS CONTROL----------------

    private int pressedKey = 0;

    final public static byte SK_OK = 0;
    final public static byte SK_EXIT = 3;
    final public static byte SK_NONE = 7;


    public final void setLSK(byte label) {
        okLabelN = label;
        enabledOkKey = okLabelN != SK_NONE;
    }

    public final void setRSK(byte label) {
        cancelLabelN = label;
        enabledCancelKey = cancelLabelN != SK_NONE;
    }

    public void keyPressed(int key) {
        switch (key) {
            case Event.EVENT_KEY_UP:
                app.eventAdd(Event.EVENT_KEY_UP, 0, null);
                break;
            case Event.EVENT_KEY_DOWN:
                app.eventAdd(Event.EVENT_KEY_DOWN, 0, null);
                break;
        }
    }

    protected void keyRepeated(int key) {
    }

    protected void keyReleased(int key) {
        if (!enabledOkKey && key == Canvas.KEY_OK || !enabledCancelKey && key == Canvas.KEY_CANCEL)
            return;
        try {
            if (canvas.isKeyValid(key) || canvas.isTouchSupport) {
                app.eventAdd(Event.EVENT_KEY_RELEASED, key, null);
                pressedKey = 0;
            }
        } catch (Exception e) {
            Debug.error("keyReleased [FILELINE]", e);
        }
    }

    public boolean onTouchEvent(int action, int x, int y) {
        x -= Port.SCREEN_DX;
        y -= Port.SCREEN_DY;
        switch (action) {
            case Canvas.ACTION_DOWN:
                app.eventAdd(Event.EVENT_TOUCH_PRESSED, 0, null, x, y);
                touch_down_last = System.currentTimeMillis();
                break;
            case Canvas.ACTION_UP:
                if (isDoubleClick()) {
                    app.eventAdd(Event.EVENT_TOUCH_DOUBLE_CLICK, 0, null, x, y);
                } else if (isLongClick()) {
                    app.eventAdd(Event.EVENT_TOUCH_LONG_CLICK, 0, null, x, y);
                } else {
                    app.eventAdd(Event.EVENT_TOUCH_RELEASED, 0, null, x, y);
                }
                touch_up_last = System.currentTimeMillis();
                break;
            case Canvas.ACTION_MOVE:
                app.eventAdd(Event.EVENT_TOUCH_MOVED, 0, null, x, y);
                break;
        }
        return true;
    }

    private boolean isDoubleClick() {
        return System.currentTimeMillis() - touch_up_last < 250;//mls
    }

    private boolean isLongClick() {
        return System.currentTimeMillis() - touch_down_last > 1000;//mls
    }

    private long touch_up_last;//время последнего отжатия
    private long touch_down_last;//время последнего нажатия
    private final long TOUCH_DOUBLE_CLICK_MLS = 100;//интервал между нажатиями засчитываемый как двойной клик


    public void backPressed() {
        destroy(true);
    }

    public void menuPressed() {
        Debug.debug("Manager.menuPressed");
    }

    // ------------------ SERVICES ------------------------
    public boolean isSuspended() {
        return isSuspended;
    }

    protected Display getDisplay() {
        return Display.getDisplay(midlet);
    }

    public void vibrate() {
        Display.getDisplay(midlet).vibrate(300);
    }

    public static void pause(long pause) {
        try {
            Thread.sleep(1);
            Thread.sleep(pause);
            Thread.yield();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public MIDlet getMidlet() {
        return midlet;
    }

    public void setMidlet(MIDlet midlet) {
        this.midlet = midlet;
    }

    public Application getApplication() {
        return app;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onLowMemory() {
    }

    public void draw() {
    }

    public void loadSplash() {
    }

    public int getFrameTotalCount() {
        return 0;
    }

    public void setOpenGLDrawCount(int drawCount) {
    }

    public int getSizeExpectedResources() {
        return 0;
    }

    public void initCanvas(MIDlet midlet) {
        canvas = new Canvas(this, 1, 1);
        if (app != null) {
            canvas.setRenderer(app.getGraphics());
        }
    }
}