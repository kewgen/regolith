package com.geargames.demo;

import com.geargames.demo.LWJGL.GLImage;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

import java.nio.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

//import javax.media.opengl.GLCanvas;


/**
 * Collection of functions to init and run an OpenGL app using LWJGL.
 * <P>
 * Includes functions to handle:  <BR>
 *        Setup display mode, keyboard, mouse, handle events<BR>
 *        Run main loop of application <BR>
 *        Buffer allocation -- manage IntBuffer, ByteBuffer calls. <BR>
 *        OpenGL functions  -- convert screen/world coords, set modes, lights, etc. <BR>
 *        Utility functions -- load images, convert pixels, getTimeInMillis, etc. <BR>
 * <P>
 * Has a main() function to run as an application, though this class has only
 * minimal placeholder functionality.  It is meant to be subclassed,
 * and the subclass will define setup() draw() mouseMove() functions, etc.
 * <P>
 * GLApp initializes the LWJGL environment for OpenGL rendering,
 * ie. creates a window, sets the display mode, inits mouse and keyboard,
 * then runs a loop.
 * <P>
 * Uses GLImage to load and hold image pixels.
 * <P>
 * napier -at- potatoland -dot- org
 *
 * @see com.geargames.demo.LWJGL.GLImage
 */
public class GLApplication {

    // Byte size of data types: Used when allocating native buffers
    public static final int SIZE_DOUBLE = 8;
    public static final int SIZE_FLOAT = 4;
    public static final int SIZE_INT = 4;
    public static final int SIZE_BYTE = 1;

    // Application settings
    // These can be tweaked in main() before calling app.run()
    // to customize app behavior.
    public static int finishedKey = Keyboard.KEY_ESCAPE; // App will exit when this key is hit (set to 0 for no key exit)
    public static String window_title = "OpenGL Window"; // window title, set in initDisplay()
    public static boolean VSyncEnabled = true;           // if true, synchronize screen updates with video refresh rate
    public static boolean useCurrentDisplay = false;     // when initing display, use the settings of the desktop (whatever the PC was using before app was started)
    public static boolean fullScreen = false;            // full screen or floating window
	public static boolean showMessages = true;           // if true, show debug messages, if false show only error messages (see msg() err())
    public static float aspectRatio = 0;                 // aspect ratio of OpenGL context (if 0, default to displayWidth/displayHeight)

    // Display settings (settings in glapp.cfg will override these)
    // initDisplay() will pick a Display that best matches displayWidth,
    // displayHeight, displayColorBits, displayFrequency.  If these values
    // are -1, initDisplay() will use the desktop screen settings.
    public static int displayWidth = -1;
    public static int displayHeight = -1;
    public static int displayColorBits = -1;
    public static int displayFrequency = -1;
    public static int depthBufferBits = 24;             // bits per pixel in the depth buffer
    public static int viewportX, viewportY;             // viewport position (will default to 0,0)
    public static int viewportW, viewportH;             // viewport size (will default to screen width, height)
    //private static int orthoWidth = 0;
    //private static int orthoHeight = 0;

    // DisplayMode chosen by initDisplay()
    // DM and displayMode are the same thing.
    public static DisplayMode DM, origDM;               // hold display mode we set, and the display mode when app first executes
    public static DisplayMode displayMode;              // hold display mode we set (same as DM)

    // Application variables
    // These are set internally but can be read by the
    // subclass application.
    public static boolean finished;                     // App will exit when finished is true (when finishedKey is hit: see run())
    public static double lastFrameTime = 0;             // ticks since last frame was drawn (see run() and updateTimer())
    public static double secondsSinceLastFrame = 0;     // seconds elapsed since last frame was drawn (see updateTimer())
    public static long ticksPerSecond = 0;              // used to calc time in millis
    public static int frameCount = 0;                   // count frames per sec (just to track performance)

    // For copying screen image to a texture
    public static int screenTextureSize = 1024;         // how large should texture be to hold screen (see makeTextureForScreen())

//    // NIO Buffers to retrieve OpenGL settings.
//    // For memory efficiency and performance, instantiate these once, and reuse.
//    // see getSetingInt(), getModelviewMatrix(), project(), unProject()
//    public static IntBuffer     bufferViewport = allocInts(16);
//    public static FloatBuffer   bufferModelviewMatrix = allocFloats(16);
//    public static FloatBuffer   bufferProjectionMatrix = allocFloats(16);
//    public static FloatBuffer   tmpResult = allocFloats(16);         // temp var to hold project/unproject results
//    public static FloatBuffer   tmpFloats = allocFloats(4);          // temp var used by setLightPos(), setFog()
//    public static ByteBuffer    tmpFloat = allocBytes(SIZE_FLOAT);   // temp var used by getZDepth()
//    public static IntBuffer     tmpInts = allocInts(16);             // temp var used by getSettingInt()
//    public static ByteBuffer 	tmpByte = allocBytes(SIZE_BYTE);     // temp var used by getStencilValue()
//    public static ByteBuffer    tmpInt = allocBytes(GLApplication.SIZE_INT); // temp var used by getPixelColor()

//    // Material colors (see setMaterial())
//    public static FloatBuffer mtldiffuse = allocFloats(4);     // color of the lit surface
//    public static FloatBuffer mtlambient = allocFloats(4);     // color of the shadowed surface
//    public static FloatBuffer mtlspecular = allocFloats(4);    // reflection color (typically this is a shade of gray)
//    public static FloatBuffer mtlemissive = allocFloats(4);    // glow color
//    public static FloatBuffer mtlshininess = allocFloats(4);   // size of the reflection highlight

//    // Misc.
//    public static float rotation = 0f;       // to rotate cubes (just to put something on screen)
//    public static final float PIOVER180 = 0.0174532925f;   // A constant used in navigation: PI/180
//	public static final float PIUNDER180 = 57.2957795130f;   // A constant used in navigation: 180/PI;
	static Hashtable OpenGLextensions;       // will be populated by extensionExists()
//	static double avgSecsPerFrame=.01;       // to smooth out motion, keep a moving average of frame render times

    //========================================================================
    // Run main loop of application.  Handle mouse and keyboard input.
    //
    // The functions main(), run() and init() start and run the application.
    // The run() function starts a loop that handles mouse and keyboard events
    // and calls draw() repeatedly.
    //
    //========================================================================

    public static void main(String args[]) {
        GLApplication demo = new GLApplication();
        demo.run();
    }

    /**
     * Runs the application.  Calls init() function to setup OpenGL,
     * input and display.  Runs the main loop of the application, which handles
     * mouse and keyboard input.
     * <P>
     * Calls init(), handleEvents(), update() and draw(). <BR>
     * handleEvents() calls:  mouseMove(), mouseDown(), mouseUp(), keyDown(), keyUp()
     */
    public void run() {
        try {
            // Init Display, Keyboard, Mouse, OpenGL, load config file
            init();
            // Main loop
            while (!finished) {
                if (!Display.isVisible()) {  // window is minimized
                    Thread.sleep(200L);
                }
                else if (Display.isCloseRequested()) {  // window X button clicked
                    finished = true;
                }
                else {   // yield a little so other threads can work
                    Thread.sleep(1);
                }
//                updateTimer();      // track when frame was drawn (see secondsSinceLastFrame)
                handleEvents();     // call key...() and mouse...() functions based on input events
                update();           // do program logic here (subclass may override this)
                draw();             // redraw the screen (subclass overrides this)
                Display.update();
            }
        }
        catch (Exception e) {
            err("GLApp.run(): " + e);
            e.printStackTrace(System.out);
        }
        // prepare to exit
        cleanup();
        System.exit(0);
    }

    /**
     * Called only once when app is first started, init() prepares the display,
     * mouse and OpenGL context for use. Override init() only if you want to
     * substantially alter the app startup behavior.  Otherwise just override
     * initGL() to tweak the OpenGL context and setup() to load textures,
     * models, etc..
     */
    public void init()
    {
        initDisplay();
        initInput();
        initGL();
        setup();        // subclass usually overrides this
//    	updateTimer();  // Do this once to init time values to something sane, otherwise the first game loop will report a huge secondsElapsedPerFrame
    }

    /**
     * Called by the run() loop.  Handles animation and input for each frame.
     */
    public void handleEvents() {
        // Handle key hits
        while ( Keyboard.next() )  {
        	// check for exit key
            if (Keyboard.getEventKey() == finishedKey) {
                finished = true;
            }
            // pass key event to handler
            if (Keyboard.getEventKeyState()) {    // key was just pressed, trigger keyDown()
                keyDown(Keyboard.getEventKey());
            }
            else {
                keyUp(Keyboard.getEventKey());    // key was released
            }
        }

        // Count frames
        frameCount++;
        if ((Sys.getTime()-lastFrameTime) > ticksPerSecond) {
            //msg("==============> FramesPerSec=" + (frameCount/1) + " timeinsecs=" + getTimeInSeconds() + " timeinmillis=" + getTimeInMillis());
            frameCount = 0;
        }
    }

    //========================================================================
    // Setup display mode
    //
    // Initialize Display, Mouse, Keyboard.
    //
    //========================================================================

    /**
     * Initialize the Display mode, viewport size, and open a Window.
     * By default the window is fullscreen, the viewport is the same dimensions
     * as the window.
     */
    public boolean initDisplay() {
        origDM = Display.getDisplayMode();  // current display settings
        msg("GLApp.initDisplay(): Current display mode is " + origDM);
    	// for display properties that have not been specified, default to current display value
    	if (displayHeight == -1) displayHeight = origDM.getHeight();
    	if (displayWidth == -1) displayWidth = origDM.getWidth();
    	if (displayColorBits == -1) displayColorBits = origDM.getBitsPerPixel();
    	if (displayFrequency == -1) displayFrequency = origDM.getFrequency();
        // Set display mode
        try {
            if (useCurrentDisplay) {
            	// use current display settings (ignore properties file)
                DM = origDM;
            }
            else {
            	// find a display mode that matches the specified settings (or use a sane alternative)
                if ( (DM = getDisplayMode(displayWidth, displayHeight, displayColorBits, displayFrequency)) == null) {
                    if ( (DM = getDisplayMode(1024, 768, 32, 60)) == null) {
                        if ( (DM = getDisplayMode(1024, 768, 16, 60)) == null) {
                            if ( (DM = getDisplayMode(origDM.getWidth(), origDM.getHeight(), origDM.getBitsPerPixel(), origDM.getFrequency())) == null) {
                                err("GLApp.initDisplay(): Can't find a compatible Display Mode!!!");
                            }
                        }
                    }
                }
            }
            msg("GLApp.initDisplay(): Setting display mode to " + DM + " with pixel depth = " + depthBufferBits);
            Display.setDisplayMode(DM);
            displayMode = DM;
            displayWidth = DM.getWidth();
            displayHeight = DM.getHeight();
            displayColorBits = DM.getBitsPerPixel();
            displayFrequency = DM.getFrequency();
        }
        catch (Exception exception) {
            System.err.println("GLApp.initDisplay(): Failed to create display: " + exception);
            System.exit(1);  //!!!!new
        }
        // Initialize the Window
        try {
            Display.create(new PixelFormat(0, depthBufferBits, 8));  // set bits per buffer: alpha, depth, stencil
            Display.setTitle(window_title);
            Display.setFullscreen(fullScreen);
            Display.setVSyncEnabled(VSyncEnabled);
            //msg("GLApp.initDisplay(): Created OpenGL window.");
        }
        catch (Exception exception1) {
            System.err.println("GLApp.initDisplay(): Failed to create OpenGL window: " + exception1);
            System.exit(1);
        }
        // Set viewport width/height and Aspect ratio.
        if (aspectRatio == 0f) {
            // no aspect ratio was set in GLApp.cfg: default to full screen.
            aspectRatio = (float)DM.getWidth() / (float)DM.getHeight(); // calc aspect ratio of display
        }
        // viewport may not match shape of screen.  Adjust to fit.
        viewportH = DM.getHeight();                        // viewport Height
        viewportW = (int) (DM.getHeight() * aspectRatio);  // Width
        if (viewportW > DM.getWidth()) {
            viewportW = DM.getWidth();
            viewportH = (int) (DM.getWidth() * (1 / aspectRatio));
        }
        // center viewport in screen
        viewportX = (int) ((DM.getWidth()-viewportW) / 2);
        viewportY = (int) ((DM.getHeight()-viewportH) / 2);
        return true;
    }

    /**
     * Retrieve a DisplayMode object with the given params
     */
    public static DisplayMode getDisplayMode(int w, int h, int colorBits, int freq) {
         try {
             DisplayMode allDisplayModes[] = Display.getAvailableDisplayModes();
             DisplayMode dm = null;
             for (int j = 0; j < allDisplayModes.length; j++) {
                 dm = allDisplayModes[j];
                 if (dm.getWidth() == w && dm.getHeight() == h && dm.getBitsPerPixel() == colorBits &&
                     dm.getFrequency() == freq) {
                     return dm;
                 }
             }
         }
         catch (LWJGLException lwjgle) {
             err("GLApp.getDisplayMode() error:" + lwjgle);
         }
        return null;
    }

    /**
     * Initialize the Keyboard and Mouse.
     * <P>
     * Disable or hide the native cursor.  Set the initial cursor position.  Get
     * the timer resolution (ticks per second).
     *
//     * @see handleEvents()
     */
    public void initInput() {
        try {
            // init keyboard
            Keyboard.create();

            // Init hi-res timer (see time functions)
            ticksPerSecond = Sys.getTimerResolution();
        }
        catch (Exception e) {
            err("GLApp.initInput(): " + e);
        }
    }


    //========================================================================
    // Custom Application functionality: can be overriden by subclass.
    //
    // Functions to initialize OpenGL, set the viewing mode, render the scene,
    // respond to mouse actions, and initialize the app.  These functions
    // are overridden in the subclass to create custom behavior.
    //
    //========================================================================

    /**
     * Initialize the OpenGL context.  The subclass can override this function
     * to customize the OpenGL settings.  This function is called by init()
     * once when app starts, but may be called again to restore settings to a
     * default state, or to initialize a PBuffer object to the exact same
     * state as the display.
     */
    public void initGL() {
        try {
            // Depth test setup
            GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
            GL11.glDepthFunc(GL11.GL_LEQUAL);  // The Type Of Depth Testing To Do

            // Some basic settings
            GL11.glClearColor(0f, 0f, 0f, 1f); // Black Background
            GL11.glEnable(GL11.GL_NORMALIZE);  // force normal lengths to 1
            GL11.glEnable(GL11.GL_CULL_FACE);  // don't render hidden faces
        	GL11.glEnable(GL11.GL_TEXTURE_2D); // use textures
            GL11.glEnable(GL11.GL_BLEND);      // enable transparency

            // How to handle transparency: average colors together
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            // Enable alpha test so the transparent backgrounds in texture images don't draw.
            // This prevents transparent areas from affecting the depth or stencil buffer.
            // alpha func will accept only fragments with alpha greater than 0
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0f);

            // Draw specular highlghts on top of textures
            GL11.glLightModeli(GL12.GL_LIGHT_MODEL_COLOR_CONTROL, GL12.GL_SEPARATE_SPECULAR_COLOR );

            // Perspective quality
            GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);

            // Set the size and shape of the screen area
            GL11.glViewport(viewportX, viewportY, viewportW, viewportH);

            // setup perspective (see setOrtho() for 2D)
            setPerspective();

            // select model view for subsequent transformations
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
        }
        catch (Exception e) {
            err("GLApp.initOpenGL(): " + e);
        }
    }

    /**
     *  Setup can be overridden by the subclass to initialize the application
     *  ie load textures, models, and create data structures used by the app.
     *
     *  This function is called only once, when application starts.  It is
     *  called after initDisplay and initOpenGL(), so the OpenGL context is
     *  already created.
     *
//     *  @see init()
     */
    public void setup() {
    }

    /**
     *  Update can be overridden by the subclass
     *
//     *  @see run()
     */
    public void update() {
    }

    /**
     * Called by run() to draw one frame.  Subclass will override this.
     * This is an alias function just to be follow Processing and OpenFrameworks
     * function naming conventions.
     */
    public void draw() {
        render();
    }

    /**
     * Same as draw().  Subclass can override render() instead of draw().
     * Same thing, just a matter of taste.
     */
    public void render() {
        // rotate 90 degrees per second
//        rotation += secondsSinceLastFrame * 90f;
        float rotation = 1.0f;

        // clear depth buffer and color
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // select model view for subsequent transforms
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        // set viewpoint 10 units from origin, looking at origin
        GLU.gluLookAt(0,0,10, 0,0,0, 0,1,0);

        // rotate, scale and draw cube
        GL11.glPushMatrix();
        {
        	GL11.glRotatef(rotation, 0, 1, 0);
        	GL11.glColor4f(0f, .5f, 1f, 1f);
        	renderCube();
        }
        GL11.glPopMatrix();

        // draw another overlapping cube
        GL11.glPushMatrix();
        {
        	GL11.glRotatef(rotation, 1, 1, 1);
        	GL11.glColor4f(.7f, .5f, 0f, 1f);
        	renderCube();
        }
        GL11.glPopMatrix();
    }

    /**
     * Run() calls this right before exit. Free up allocated resources (display lists)
     * and gracefully shut down OpenGL context.
     */
    public void cleanup() {
        destroyFont();
        Keyboard.destroy();
        Display.destroy();  // will call Mouse.destroy()
    }

    /**
     * Shutdown the application.  This will call cleanup() before exiting from the
     * application.
//     * @see cleanup()
     */
    public void exit() {
        finished = true;
    }

    //========================================================================
    // Mouse events, called by handleEvents()
    //========================================================================

    /**
     * Called by handleEvents() when mouse moves
     */
    public void mouseMove(int x, int y) {
    }

    public void mouseDown(int x, int y) {
    }

    public void mouseUp(int x, int y) {
    }

    public void mouseWheel(int wheelMoved) {
    }

    /**
     * Return true if the given mousebutton is down.  Typically mouse buttons
     * are 0=left, 1=right.  This function can be called inside mouse events such
     * as mouseDown() and mouseMove() to see which button is activated.
     * @param whichButton  number of mouse button (0=left button)
     */
    public boolean mouseButtonDown(int whichButton) {
    	return Mouse.isButtonDown(whichButton);
    }

    /**
     * Called when key is pressed.  Keycode will be the key ID value as
     * defined in the LWJGL Keyboard class.
     *
     * @see org.lwjgl.input.Keyboard class in the LWJGL documentation
     * @param keycode
     */
    public void keyDown(int keycode) {
    }

    /**
     * Called when key is released.  Keycode will be the key ID value as
     * defined in the LWJGL Keyboard class.
     *
     * @see org.lwjgl.input.Keyboard class in the LWJGL documentation
     * @param keycode
     */
    public void keyUp(int keycode) {
    }

    /**
     * Return the character associatated with the last key event.  When
     * called inside keyDown() or keyUp() this function will return the
     * character equivalent of the keycode that was passed to keyDown()
     * or keyUp().
     */
    public char keyChar() {
        return Keyboard.getEventCharacter();
    }

    //========================================================================
    // functions to get values from a Properties object.  Properties can be
    // loaded from a text file containing name=value pairs.
    //========================================================================

    /**
     * return the width of the Viewport (the screen area that OpenGL will draw into).
     * By default the viewport is the same size as the Display (see getWidthWindow()),
     * however the setViewport() function can set the viewport to a sub-region of the screen.
     * <P>
     * This function is only valid after app is running and Display has been initialized.
     *
//     * @see setViewport(int,int,int,int)
     */
    public static int getWidth() {
    	return viewportW;
    }

    /**
     * return the height of the Viewport (the screen area that OpenGL will draw into).
     * By default the viewport is the same size as the Display (see getHeightWindow()),
     * however the setViewport() function can set the viewport to a sub-region of the screen.
     * <P>
     * This function is only valid after app is running and Display has been initialized.
     *
//     * @see setViewport(int,int,int,int)
     */
    public static int getHeight() {
    	return viewportH;
    }

    /**
     * return the Display width (the width of the full window).  Only valid after app
     * is running and Display has been initialized.
     */
    public static int getWidthWindow() {
    	return displayWidth;
    }

    /**
     * return the Display height (the height of the full window).  Only valid after
     * app is running and Display has been initialized.
     */
    public static int getHeightWindow() {
    	return displayHeight;
    }

    //========================================================================
    // functions to set basic application behavior
    //========================================================================

    /**
     * Set the background color of the screen.  The red,green,blue
     * color components are floats in the range 0-1.  Black is 0,0,0
     * and white is 1,1,1.  Color will take effect the next time the
     * screen is cleared.
     */
    public static void setBackgroundColor(float R, float G, float B) {
        GL11.glClearColor(R,G,B,1);
    }

    //========================================================================
    // Matrix functions: get settings, get matrices, convert
    // screen to world coords.
    //========================================================================

//    /**
//     * Return the Z depth of the single pixel at the given screen position.
//     */
//    public static float getZDepth(int x, int y)
//    {
////        tmpFloat.clear();
////        GL11.glReadPixels(x, y, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, tmpFloat);
////        return ( (float) (tmpFloat.getFloat(0)));
//    }
//
//    /**
//     * Find the Z depth of the origin in the projected world view. Used by getWorldCoordsAtScreen()
//     * Projection matrix  must be active for this to return correct results (GL.glMatrixMode(GL.GL_PROJECTION)).
//     * For some reason I have to chop this to four decimals or I get bizarre
//     * results when I use the value in project().
//     */
//    public static float getZDepthAtOrigin()
//    {
//        float[] resultf = new float[3];
//        project( 0, 0, 0, resultf);
//        return ((int)(resultf[2] * 10000F)) / 10000f;  // truncate to 4 decimals
//    }
//
//    /**
//     * Return screen coordinates for a given point in world space.  The world
//     * point xyz is 'projected' into screen coordinates using the current model
//     * and projection matrices, and the current viewport settings.
//     *
//     * @param x         world coordinates
//     * @param y
//     * @param z
//     * @param resultf    the screen coordinate as an array of 3 floats
//     */
//    public static void project(float x, float y, float z, float[] resultf)
//    {
//        // lwjgl 2.0 altered params for GLU funcs
//        GLU.gluProject( x, y, z, getModelviewMatrix(), getProjectionMatrix(), getViewport(), tmpResult);
//        resultf[0] = tmpResult.get(0);
//        resultf[1] = tmpResult.get(1);
//        resultf[2] = tmpResult.get(2);
//    }
//
//    /**
//     * Return world coordinates for a given point on the screen.  The screen
//     * point xyz is 'un-projected' back into world coordinates using the
//     * current model and projection matrices, and the current viewport settings.
//     *
//     * @param x         screen x position
//     * @param y         screen y position
//     * @param z         z-buffer depth position
//     * @param resultf   the world coordinate as an array of 3 floats
////     * @see             getWorldCoordsAtScreen()
//     */
//    public static void unProject(float x, float y, float z, float[] resultf)
//    {
//        GLU.gluUnProject( x, y, z, getModelviewMatrix(), getProjectionMatrix(), getViewport(), tmpResult);
//        resultf[0] = tmpResult.get(0);
//        resultf[1] = tmpResult.get(1);
//        resultf[2] = tmpResult.get(2);
//    }
//
//    /**
//     * For given screen xy, return the world xyz coords in a float array.  Assume
//     * Z position is 0.
//     */
//    public static float[] getWorldCoordsAtScreen(int x, int y) {
//        float z = getZDepthAtOrigin();
//        float[] resultf = new float[3];
//        unProject( (float)x, (float)y, (float)z, resultf);
//        return resultf;
//    }
//
//    /**
//     * For given screen xy and z depth, return the world xyz coords in a float array.
//     */
//    public static float[] getWorldCoordsAtScreen(int x, int y, float z) {
//        float[] resultf = new float[3];
//        unProject( (float)x, (float)y, (float)z, resultf);
//        return resultf;
//    }

    //========================================================================
    // Texture functions
    //========================================================================

    /**
     * Allocate a texture (glGenTextures) and return the handle to it.
     */
    public static int allocateTexture()
    {
        IntBuffer textureHandle = allocInts(1);
        GL11.glGenTextures(textureHandle);
        return textureHandle.get(0);
    }

    /**
     * "Select" the given texture for further texture operations.
     */
    public static void activateTexture(int textureHandle)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,textureHandle);
    }

    /**
     * Create a texture and mipmap from the given image file.
     */
    public static int makeTexture(String textureImagePath)
    {
		int textureHandle = 0;
		GLImage textureImg = loadImage(textureImagePath);
		if (textureImg != null) {
			textureHandle = makeTexture(textureImg);
			makeTextureMipMap(textureHandle,textureImg);
		}
		return textureHandle;
    }

    /**
     * Create a texture and optional mipmap with the given parameters.
     *
     * @param mipmap: if true, create mipmaps for the texture
     * @param anisotropic: if true, enable anisotropic filtering
     */
    public static int makeTexture(String textureImagePath, boolean mipmap, boolean anisotropic)
    {
        int textureHandle = 0;
        GLImage textureImg = loadImage(textureImagePath);
        if (textureImg != null) {
            textureHandle = makeTexture(textureImg.pixelBuffer, textureImg.w, textureImg.h, anisotropic);
            if (mipmap) {
                makeTextureMipMap(textureHandle,textureImg);
            }
        }
        return textureHandle;
    }

    /**
     * Create a texture from the given image.
     */
    public static int makeTexture(GLImage textureImg)
    {
        if ( textureImg != null ) {
            if (isPowerOf2(textureImg.w) && isPowerOf2(textureImg.h)) {
                return makeTexture(textureImg.pixelBuffer, textureImg.w, textureImg.h, false);
            }
            else {
                msg("GLApp.makeTexture(GLImage) Warning: not a power of two: " + textureImg.w + "," + textureImg.h);
                textureImg.convertToPowerOf2();
                return makeTexture(textureImg.pixelBuffer, textureImg.w, textureImg.h, false);
            }
        }
        return 0;
    }

    /**
     * De-allocate the given texture (glDeleteTextures()).
     */
    public static void deleteTexture(int textureHandle)
    {
        IntBuffer bufferTxtr = allocInts(1).put(textureHandle);
        GL11.glDeleteTextures(bufferTxtr);
    }

    /**
     *  Returns true if n is a power of 2.  If n is 0 return zero.
     */
    public static boolean isPowerOf2(int n) {
    	if (n == 0) { return false; }
        return (n & (n - 1)) == 0;
    }

    /**
     * Create a blank square texture with the given size.
     * @return  the texture handle
     */
    public static int makeTexture(int w)
    {
        ByteBuffer pixels = allocBytes(w*w*SIZE_INT);  // allocate 4 bytes per pixel
        return makeTexture(pixels, w, w, false);
	}

    /**
     * Create a texture from the given pixels in the default Java ARGB int format.<BR>
     * Configure the texture to repeat in both directions and use LINEAR for magnification.
     * <P>
     * @return  the texture handle
     */
    public static int makeTexture(int[] pixelsARGB, int w, int h, boolean anisotropic)
    {
    	if (pixelsARGB != null) {
    		ByteBuffer pixelsRGBA = GLImage.convertImagePixelsRGBA(pixelsARGB,w,h,true);
    		return makeTexture(pixelsRGBA, w, h, anisotropic);
    	}
    	return 0;
    }

    /**
     * Create a texture from the given pixels in the default OpenGL RGBA pixel format.
     * Configure the texture to repeat in both directions and use LINEAR for magnification.
     * <P>
     * @return  the texture handle
     */
    public static int makeTexture(ByteBuffer pixels, int w, int h, boolean anisotropic)
    {
        // get a new empty texture
        int textureHandle = allocateTexture();
        // preserve currently bound texture, so glBindTexture() below won't affect anything)
        GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
        // 'select' the new texture by it's handle
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,textureHandle);
        // set texture parameters
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR); //GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); //GL11.GL_NEAREST);

        // make texture "anisotropic" so it will minify more gracefully
    	if (anisotropic && extensionExists("GL_EXT_texture_filter_anisotropic")) {
    		// Due to LWJGL buffer check, you can't use smaller sized buffers (min_size = 16 for glGetFloat()).
    		FloatBuffer max_a = allocFloats(16);
    		// Grab the maximum anisotropic filter.
    		GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, max_a);
    		// Set up the anisotropic filter.
    		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, max_a.get(0));
    	}

        // Create the texture from pixels
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,
        		0, 						// level of detail
        		GL11.GL_RGBA8,			// internal format for texture is RGB with Alpha
        		w, h, 					// size of texture image
        		0,						// no border
        		GL11.GL_RGBA, 			// incoming pixel format: 4 bytes in RGBA order
        		GL11.GL_UNSIGNED_BYTE,	// incoming pixel data type: unsigned bytes
        		pixels);				// incoming pixels

        // restore previous texture settings
        GL11.glPopAttrib();

        return textureHandle;
    }

    /**
     * Create a texture from the given pixels in ARGB format.  The pixels buffer contains
     * 4 bytes per pixel, in ARGB order.  ByteBuffers are created with native hardware byte
     * orders, so the pixels can be in big-endian (ARGB) order, or little-endian (BGRA) order.
     * Set the pixel_byte_order accordingly when creating the texture.
     * <P>
     * Configure the texture to repeat in both directions and use LINEAR for magnification.
     * <P>
     * NOTE: I'm having problems creating mipmaps when image pixel data is in GL_BGRA format.
     * Looks like GLU type param doesn't recognize GL_UNSIGNED_INT_8_8_8_8 and
     * GL_UNSIGNED_INT_8_8_8_8_REV so I can't specify endian byte order.  Mipmaps look
     * right on PC but colors are reversed on Mac.  Have to stick with GL_RGBA
     * byte order for now.
     * <P>
     * @return  the texture handle
     */
    public static int makeTextureARGB(ByteBuffer pixels, int w, int h)
    {
    	// byte buffer has ARGB ints in little endian or big endian byte order
		int pixel_byte_order = (pixels.order() == ByteOrder.BIG_ENDIAN)?
				GL12.GL_UNSIGNED_INT_8_8_8_8 :
				GL12.GL_UNSIGNED_INT_8_8_8_8_REV;
        // get a new empty texture
        int textureHandle = allocateTexture();
        // 'select' the new texture by it's handle
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,textureHandle);
        // set texture parameters
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR); //GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); //GL11.GL_NEAREST);
        // Create the texture from pixels
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,
        		0, 						// level of detail
        		GL11.GL_RGBA8,			// internal format for texture is RGB with Alpha
        		w, h, 					// size of texture image
        		0,						// no border
        		GL12.GL_BGRA, 			// incoming pixel format: 4 bytes in ARGB order
        		pixel_byte_order,		// incoming pixel data type: little or big endian ints
        		pixels);				// incoming pixels
        return textureHandle;
    }

    /**
     * Build Mipmaps for currently bound texture (builds a set of textures at various
     * levels of detail so that texture will scale up and down gracefully)
     *
     * @param textureImg  the texture image
     * @return   error code of buildMipMap call
     */
    public static int makeTextureMipMap(int textureHandle, GLImage textureImg)
    {
    	int ret = 0;
    	if (textureImg != null && textureImg.isLoaded()) {
    		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);
    		ret = GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, GL11.GL_RGBA8,
    				textureImg.w, textureImg.h,
    				GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureImg.getPixelBytes());
    		if (ret != 0) {
    			err("GLApp.makeTextureMipMap(): Error occured while building mip map, ret=" + ret + " error=" + GLU.gluErrorString(ret) );
    		}
    		// Assign the mip map levels and texture info
    		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
    		GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
    	}
        return ret;
    }

    /**
     * Create a texture large enough to hold the screen image.  Use RGBA8 format
     * to insure colors are copied exactly.  Use GL_NEAREST for magnification
     * to prevent slight blurring of image when screen is drawn back.
     *
//     * @see frameCopy()
//     * @see frameDraw()
     */
    public static int makeTextureForScreen(int screenSize)
    {
        // get a texture size big enough to hold screen (512, 1024, 2048 etc.)
        screenTextureSize = getPowerOfTwoBiggerThan(screenSize);
        msg("GLApp.makeTextureForScreen(): made texture for screen with size " + screenTextureSize);
        // get a new empty texture
        int textureHandle = allocateTexture();
        ByteBuffer pixels = allocBytes(screenTextureSize*screenTextureSize*SIZE_INT);
        // 'select' the new texture by it's handle
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,textureHandle);
        // set texture parameters
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        // use GL_NEAREST to prevent blurring during frequent screen restores
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        // Create the texture from pixels: use GL_RGBA8 to insure exact color copy
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, screenTextureSize, screenTextureSize, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
        return textureHandle;
    }

    /**
     * Find a power of two equal to or greater than the given value.
     * Ie. getPowerOfTwoBiggerThan(800) will return 1024.
     * <P>
//     * @see makeTextureForScreen()
//     * @param dimension
     * @return a power of two equal to or bigger than the given dimension
     */
    public static int getPowerOfTwoBiggerThan(int n) {
        if (n < 0)
            return 0;
        --n;
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        return n+1;
    }

	/**
	 * Copy pixels from a ByteBuffer to a texture.  The buffer pixels are integers in ARGB format
	 * (this is the Java default format you get from a BufferedImage) or BGRA format (this is the
	 * native order of Intel systems.
	 *
	 * The glTexSubImage2D() call treats the incoming pixels as integers
	 * in either big-endian (ARGB) or little-endian (BGRA) formats based on the setting
	 * of the bytebuffer (pixel_byte_order).
	 *
	 * @param bb  ByteBuffer of pixels stored as ARGB or BGRA integers
	 * @param w   width of source image
	 * @param h   height of source image
	 * @param textureHandle  texture to copy pixels into
	 */
	public static void copyPixelsToTexture(ByteBuffer bb, int w, int h, int textureHandle) {
		int pixel_byte_order = (bb.order() == ByteOrder.BIG_ENDIAN)?
										GL12.GL_UNSIGNED_INT_8_8_8_8 :
										GL12.GL_UNSIGNED_INT_8_8_8_8_REV;

		// "select" the texture that we'll write into
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);

		// Copy pixels to texture
    	GL11.glTexSubImage2D(
    			GL11.GL_TEXTURE_2D,   		// always GL_TEXTURE_2D
    			0,                  		// texture detail level: always 0
    			0, 0,                		// x,y offset into texture
    			w, h,             			// dimensions of image pixel data
    			GL12.GL_BGRA,   			// format of pixels in texture (little_endian - native for PC)
    			pixel_byte_order,    		// format of pixels in bytebuffer (big or little endian ARGB integers)
    			bb           				// image pixel data
    	);
	}

	/**
	 * Calls glTexSubImage2D() to copy pixels from an image into a texture.
	 */
	public static void copyImageToTexture(GLImage img, int textureHandle) {
		copyPixelsToTexture(img.pixelBuffer, img.w, img.h, textureHandle);
	}

    //========================================================================
    // functions to set projection
    //========================================================================

    /**
     * Set OpenGL to render in 3D perspective.  Set the projection matrix using
     * GLU.gluPerspective().  The projection matrix controls how the scene is
     * "projected" onto the screen.  Think of it as the lens on a camera, which
     * defines how wide the field of vision is, how deep the scene is, and how
     * what the aspect ratio will be.
     */
    public static void setPerspective()
    {
        // select projection matrix (controls perspective)
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(40f, aspectRatio, 1f, 1000f);
        // return to modelview matrix
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    /**
     * Set OpenGL to render in flat 2D (no perspective) with a one-to-one
     * relation between screen pixels and world coordinates, ie. if
     * you draw a 10x10 quad at 100,100 it will appear as a 10x10 pixel
     * square on screen at pixel position 100,100.
     * <P>
     * <B>ABOUT Ortho and Viewport:</B><br>
     * Let's say we're drawing in 2D and want to have a cinema proportioned
     * viewport (16x9), and want to bound our 2D rendering into that area ie.
     * <PRE>
          ___________1024,576
         |           |
         |  Scene    |      Set the bounds on the scene geometry
         |___________|      to the viewport size and shape
      0,0

          ___________1024,576
         |           |
         |  Ortho    |      Set the projection to cover the same
         |___________|      area as the scene
      0,0

          ___________ 1024,768
         |___________|
         |           |1024,672
         |  Viewport |      Set the viewport to the same shape
     0,96|___________|      as scene and ortho, but centered on
         |___________|      screen.
      0,0
     *</PRE>
     */
    public static void setOrtho()
    {
        // select projection matrix (controls view on screen)
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        // set ortho to same size as viewport, positioned at 0,0
        GL11.glOrtho(
        		0,viewportW,  // left,right
        		0,viewportH,  // bottom,top
        		-500,500);    // Zfar, Znear
        // return to modelview matrix
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    public static void setOrtho(int width, int height)
    {
        // select projection matrix (controls view on screen)
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        // set ortho to same size as viewport, positioned at 0,0
        GL11.glOrtho(
        		0,width,  // left,right
        		0,height,  // bottom,top
        		-500,500);    // Zfar, Znear
        // return to modelview matrix
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    /**
     * Set OpenGL to render in flat 2D (no perspective) on top of current scene.
     * Preserve current projection and model views, and disable depth testing.
     * Ortho world size will be same as viewport size, so any ortho drawing
     * (drawQuad(), drawImageFullscreen(), etc.) will be scaled to fit viewport.
     * <P>
     * NOTE: if the viewport is the same size as the window (by default it is),
     * then setOrtho() will make the world coordinates exactly match the screen
     * pixel positions.  This is convenient for mouse interaction, but be warned:
     * if you setViewport() to something other than fullscreen, then you need
     * to use getWorldCoordsAtScreen() to convert screen xy to world xy.
     * <P>
     * Once Ortho is on, glTranslate() will take pixel coords as arguments,
     * with the lower left corner 0,0 and the upper right corner 1024,768 (or
     * whatever your screen size is).  !!!
     *
//     * @see setOrthoOff()
//     * @see setViewport(int,int,int,int)
     */
    public static void setOrthoOn()
    {
		// prepare projection matrix to render in 2D
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();                   // preserve perspective view
        GL11.glLoadIdentity();                 // clear the perspective matrix
        GL11.glOrtho(                          // turn on 2D mode
        		////viewportX,viewportX+viewportW,    // left, right
        		////viewportY,viewportY+viewportH,    // bottom, top    !!!
        		0,viewportW,    // left, right
        		0,viewportH,    // bottom, top
        		-500,500);                        // Zfar, Znear
        // clear the modelview matrix
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();				   // Preserve the Modelview Matrix
        GL11.glLoadIdentity();				   // clear the Modelview Matrix
		// disable depth test so further drawing will go over the current scene
		GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Set the width and height of the Ortho scene.  By default GLApp will use
     * viewportWidth and viewportHeight for the width and height of ortho mode.
     * You can override this behavior by setting a specific size with setOrthoDimensions().
     * <P>
     * By default ortho mode (setOrtho(), setOrthoOn()) will set the world width
     * and height to exactly match the screen pixel width and height (it uses
     * the viewport size, which is typically the exact same size as the opengl window).
     * This makes it easy to map screen positions to the world space.  Text positions,
     * images and mouse coordinates map pixel-for-pixel to the screen.
     * <P>
     * The drawback is that the ortho world space will change size on different
     * resolution screens. In low-res screens the ortho world may be 800x600 while
     * in a hi-res screen it could be 1920x1200, which means that anything drawn
     * in ortho mode may shift on different screens.
     * <P>
     * setOrthoDimensions() assigns a fixed size for ortho scenes, independent of
     * the viewport size.  OpenGL will project the ortho world into the
     * viewport just as it does with a perspective projection.  Things drawn in
     * ortho will stay in the right place on any resolution screen.
     * <P>
     * The drawback with this method is that screen coordinates don't necessarily
     * map exactly to the ortho world.  To translate a screen xy to the ortho
     * world coordinates, use getWorldCoordsAtScreen(x,y) just as with perspective
     * worlds.
     *
     * @param width    width of ortho scene
     * @param height   height of ortho scene
     *
     * @see setOrtho()
     * @see setOrthoOn()
     * @see setPerspective()
     *//*
    public static void setOrthoDimensions(int width, int height)
    {
    	orthoWidth = width;
    	orthoHeight = height;
    }
    */

    /**
     * Turn 2D mode off.  Return the projection and model views to their
     * preserved state that was saved when setOrthoOn() was called, and
     * re-enable depth testing.
     *
//     * @see setOrthoOn()
     */
    public static void setOrthoOff()
    {
        // restore the original positions and views
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        // turn Depth Testing back on
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Define the position and size of the screen area in which the
     * OpenGL context will draw. Position and size of the area are given
     * in exact pixel sizes.  By default the viewport is the same size as
     * the window (displayWidth,displayHeight).
     * <P>
     * NOTE: by default the window size, viewport size and setOrtho() size are all
     * the same, so in ortho mode screen pixel positions exactly match to world
     * coordinates.  THIS IS NO LONGER TRUE if you setViewport() to some other
     * size.  With a custom viewport you need to use getWorldCoordsAtScreen()
     * to convert screen xy to world xy.
     * <P>
     * @param x       position of the lower left of viewport area, in pixels
     * @param y
     * @param width   size of the viewport area, in pixels
     * @param height
     *
//     * @see setPerspective()
//     * @see setOrtho()
//     * @see setOrthoDimensions(int,int)
     */
    public static void setViewport(int x, int y, int width, int height)
    {
    	viewportX = x;
    	viewportY = y;
    	viewportW = width;
    	viewportH = height;
     	aspectRatio = (float)width / (float)height;
       	GL11.glViewport(x,y,width,height);
   }

    /**
     * Reset the viewport to full screen (displayWidth x displayHeight).
     *
//     * @see setViewport(int,int,int,int)
     */
    public static void resetViewport()
    {
    	setViewport(0,0,displayWidth,displayHeight);
    }

    /**
     * A simple way to set eye position.  Calls gluLookat() to place
     * the viewpoint <distance> units up the Z axis from the given target position,
     * looking at the target position. The camera is oriented vertically (Y axis is up).
     * away.
     */
    public static void lookAt(float lookatX, float lookatY, float lookatZ, float distance)
    {
        // set viewpoint
        GLU.gluLookAt(
        		lookatX,lookatY,lookatZ+distance,  // eye is at the same XY as the target, <distance> units up the Z axis
        		lookatX,lookatY,lookatZ,           // look at the target position
        		0,1,0);                            // the Y axis is up
    }

    //========================================================================
    // Functions to push/pop OpenGL settings
    //========================================================================

	/**
	 * preserve all OpenGL settings that can be preserved.  Use this
	 * function to isolate settings changes.  Call pushAttrib() before
	 * calling glEnable(), glDisable(), glMatrixMode() etc. After
	 * your code executes, call popAttrib() to return to the
	 * previous settings.
	 *
	 * For better performance, call pushAttrib() with specific settings
	 * flags to preserve only specific settings.
	 *
//	 * @see popAttrib()
	 */
	public static void pushAttrib()
	{
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
	}

	/**
	 * preserve the specified OpenGL setting.  Call popAttrib() to return to
	 * the preserved state.
	 *
//	 * @see popAttrib()
	 */
	public static void pushAttrib(int attribute_bits)
	{
        GL11.glPushAttrib(attribute_bits);
	}

	/**
	 * preserve the OpenGL settings that will be affected when we draw in ortho
	 * mode over the scene.  For example if we're drawing an interface layer,
	 * buttons, popup menus, cursor, text, etc. we need to turn off lighting,
	 * turn on blending, set color to white and turn off depth test.
	 * <P>
	 * call pushAttribOverlay(), enable settings that you need, when done call popAttrib()
	 *
//	 * @see popAttrib()
	 */
	public static void pushAttribOrtho()
	{
        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_TEXTURE_BIT | GL11.GL_LIGHTING_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * preserve the OpenGL viewport settings.
	 * <pre>
	 *       pushAttribViewport();
	 *           setViewport(0,0,displaymode.getWidth(),displaymode.getHeight());
	 *           ... do some drawing outside of previous viewport area
	 *       popAttrib();
	 * </pre>
	 *
//	 * @see popAttrib()
	 */
	public static void pushAttribViewport()
	{
        GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT);
	}

	/**
	 * return to the OpenGL settings that were preserved by the previous pushAttrib() call.
	 *
//	 * @see pushAttrib()
	 */
	public static void popAttrib()
	{
        GL11.glPopAttrib();
	}

    //========================================================================
    // Time functions
    //========================================================================

//    /**
//     *  Calculate time since we last called updateTimer().  Updates secondsSinceLastFrame and
//     *  sets lastFrameTime to the current Sys.getTime().
//     *  <P>
//     *  Called by run() at the beginning of each loop.
//     *
////     *  @see run()
////     *  @see getSecondsPerFrame()
//     */
//    public static void updateTimer()
//    {
//    	// number of frames to average (about one second)
//    	double numToAvg = 50;
//    	// calc time elapsed since we last rendered
//    	secondsSinceLastFrame = (double)(Sys.getTime() - lastFrameTime) / (double)ticksPerSecond;
//    	lastFrameTime = Sys.getTime();
//    	// keep a moving average of frame elapsed times
//    	if (secondsSinceLastFrame < 1) {
//    		avgSecsPerFrame = (double) ((avgSecsPerFrame*numToAvg)+secondsSinceLastFrame) / (numToAvg+1D);
//    	}
//    }
//
//	/**
//     * Return the moving average of the seconds per frame for the last 50 frames.
//     * Useful when animating in real time.  Will provide smoother time deltas
//     * than the secondsSinceLastFrame variable, which holds the exact time elapsed
//     * during the last frame (but may jump or lag as processor load varies).
//     *
////     * @see updateTimer()
//     */
//	public static double getSecondsPerFrame() {
//    	return avgSecsPerFrame;
//    }
//
//	/**
//     * Return the moving average of the frames per second for the last 50 frames.
//     *
////     * @see updateTimer()
//     */
//	public static double getFramesPerSecond() {
//    	return 1d / avgSecsPerFrame;
//    }

    //========================================================================
    // Load images
    //========================================================================

//    /**
//     * Make a blank image of the given size.
//     * @return  the new GLImage
//     */
//    public static GLImage makeImage(int w, int h) {
//        ByteBuffer pixels = allocBytes(w*h*SIZE_INT);
//        return new GLImage(pixels,w,h);
//    }

    /**
     * Load an image from the given file and return a GLImage object.
//     * @param image filename
     * @return the loaded GLImage
     */
    public static GLImage loadImage(String imgFilename) {
        GLImage img = new GLImage(imgFilename);
        if (img.isLoaded()) {
            return img;
        }
        return null;
    }

    /**
     * Load an image from the given file and return a ByteBuffer containing ARGB pixels.<BR>
     * Can be used to create textures. <BR>
     * @param imgFilename
     * @return
     */
    public static ByteBuffer loadImagePixels(String imgFilename) {
        GLImage img = new GLImage(imgFilename);
        return img.pixelBuffer;
    }

    /**
     * Draw a cursor image textured onto a quad at cursorX,cursorY.  The cursor
     * image must be loaded into a 32x32 texture. This function can be called
     * after scene is drawn to place a cursor on top of scene.
     * <P>
     * NOTE: the cursor is drawn in screen space, at an absolute screen pixel location
     * without regard for viewport (temporarily zets viewport to entire screen).
     * <P>
     * See handleEvents() for cursorX cursorY and mouse motion handling.
     * <P>
     * Example:
     * <PRE>
     *    int cursorTxtr;
     *
     *    public void setup() {
     *        cursorTxtr = makeTexture("images/cursorCrosshair32.gif"); // image must be 32x32
     *    }
     *
     *    public void draw() {
     *        // render scene
     *        ...
     *        drawCursor(cursorTxtr);
     *    }
     * </PRE>
     *
     * @param cursorTextureHandle  handle to texture containing 32x32 cursor image
     */
//    public static void drawCursor(int cursorTextureHandle) {
//		// set projection matrix to 2D fullscreen
//        GL11.glMatrixMode(GL11.GL_PROJECTION);
//        GL11.glPushMatrix();                   // preserve perspective view
//        GL11.glLoadIdentity();                 // clear the perspective matrix
//        GL11.glOrtho(                          // set ortho to exactly match screen size
//        		0,displayWidth,     // left, right
//        		0,displayHeight,    // bottom, top
//        		-1,1);              // Zfar, Znear
//        // clear the modelview matrix
//        GL11.glMatrixMode(GL11.GL_MODELVIEW);
//        GL11.glPushMatrix();				   // preserve current modelview matrix
//        GL11.glLoadIdentity();				   // clear the modelview matrix
//
//        // preserve current settings then draw cursor
//        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_TEXTURE_BIT | GL11.GL_LIGHTING_BIT | GL11.GL_VIEWPORT_BIT);
//        {
//        	// set viewport to full screen
//        	GL11.glViewport(0,0,displayWidth,displayHeight);
//    	    // tweak settings
//            GL11.glEnable(GL11.GL_TEXTURE_2D);   // be sure textures are on
//            GL11.glColor4f(1,1,1,1);             // no color
//            GL11.glDisable(GL11.GL_LIGHTING);    // no lighting
//            GL11.glDisable(GL11.GL_DEPTH_TEST);  // no depth test
//            GL11.glEnable(GL11.GL_BLEND);        // enable transparency
//            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//            // draw 32x32 cursor image
//            drawQuadZ(cursorTextureHandle, cursorX-15, cursorY-15, 0, 32, 32);
//        }
//        GL11.glPopAttrib();
//
//        // restore the previous matrix settings
//        GL11.glMatrixMode(GL11.GL_PROJECTION);
//        GL11.glPopMatrix();
//        GL11.glMatrixMode(GL11.GL_MODELVIEW);
//        GL11.glPopMatrix();
//    }

    /**
     * Draw an image in ortho mode (2D) over the entire viewport area.
     * Converts the image to a texture and maps onto a viewport-sized quad.
     * Depth test is turned off, lighting is off, color is set to white.
     * Alpha blending is on, so transparent areas will be respected.
     * <P>
     * NOTE: By default the viewport is the same size as the window so this function
     * will draw the image over the entire window.  If you setViewport() to a
     * custom size the image will be drawn into the custom viewport area. To
     * insure that the image is drawn truly full screen, call resetViewport()
     * before drawImageFullScreen().
     * <P>
//     * @see loadImage(String)
//     * @see setViewport(int,int,int,int)
//     * @see resetViewport()
     */
    public static void drawImageFullScreen(GLImage img) {
    	if (img == null || img.isLoaded() == false) {
    		return;
    	}
    	// if image has no texture, convert the image to a texture
    	if (img.textureHandle <= 0) {
    		img.textureHandle = makeTexture(img);
    	}
    	// Calculate the UV dimensions of the image in the texture
    	float maxU = (float)img.w / (float)img.textureW;
    	float maxV = (float)img.h / (float)img.textureH;
    	// preserve settings
    	pushAttribOrtho();
    	// switch to 2D projection
    	setOrthoOn();
    	// tweak settings
    	GL11.glEnable(GL11.GL_TEXTURE_2D);   // be sure textures are on
    	GL11.glColor4f(1,1,1,1);             // no color
    	GL11.glDisable(GL11.GL_LIGHTING);    // no lighting
    	GL11.glDisable(GL11.GL_DEPTH_TEST);  // no depth test
    	GL11.glEnable(GL11.GL_BLEND);        // enable transparency
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	// activate the image texture
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D,img.textureHandle);
    	// draw a textured quad
    	GL11.glBegin(GL11.GL_QUADS);
    	{
    		GL11.glTexCoord2f(0, 0);
    		GL11.glVertex3f(0f, 0f, 0f);         // Bottom Left

    		GL11.glTexCoord2f(maxU, 0);
    		GL11.glVertex3f(getWidth(), 0f, 0f);         // Bottom Right

    		GL11.glTexCoord2f(maxU, maxV);
    		GL11.glVertex3f(getWidth(), getHeight(), 0f);   // Top Right

    		GL11.glTexCoord2f(0, maxV);
    		GL11.glVertex3f(0f, getHeight(), 0f);       // Top left
    	}
    	GL11.glEnd();
    	// return to previous projection mode
    	setOrthoOff();
    	// return to previous settings
    	popAttrib();
    }

    /**
     * Draw an image in whichever projection mode is current (does not switch to ortho mode).
     * Convert the image to a texture and draw onto quad.  Will draw with current settings
     * (light, material, depth, blend, etc.)
     * <BR>
//     * @see loadImage()
//     * @see drawQuad()
//     * @see drawImageFullScreen()
     */
    public static void drawImage(GLImage img, int x, int y, float w, float h) {
    	// if image has no texture, convert the image to a texture
    	if (img.textureHandle <= 0) {
    		img.textureHandle = makeTexture(img);
    	}
    	// preserve settings
    	pushAttribOrtho();
    	// set color to white
    	//GL11.glColor4f(1,1,1,1);   // don't force color to white (may want to tint image)
        // activate the image texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,img.textureHandle);
        // draw a textured quad
        GL11.glNormal3f(0.0f, 0.0f, 1.0f); // normal faces positive Z
        GL11.glBegin(GL11.GL_QUADS);
        {
	        GL11.glTexCoord2f(0f, 0f);
	        GL11.glVertex3f( (float)x, (float)y, (float)0);
	        GL11.glTexCoord2f(1f, 0f);
	        GL11.glVertex3f( (float)x+w, (float)y, (float)0);
	        GL11.glTexCoord2f(1f, 1f);
	        GL11.glVertex3f( (float)x+w, (float)y+h, (float)0);
	        GL11.glTexCoord2f(0f, 1f);
	        GL11.glVertex3f( (float)x, (float)y+h, (float)0);
        }
        GL11.glEnd();
        // return to previous settings
        popAttrib();
    }

    /**
     * Draw a textured quad in Ortho mode (2D) at the given xy, scaled to
     * the given width and height.  Depth test is turned off so quad will
     * be drawn on top of the current scene.  Quad will be drawn
     * with current light and material if any are active.
     * <BR>
     * @ee loadImage()
     */
    public static void drawQuad(int textureHandle, int x, int y, float w, float h) {
        // activate the specified texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,textureHandle);
        // prepare to render in 2D
        setOrthoOn();
        // draw the textured quad
        GL11.glNormal3f(0.0f, 0.0f, 1.0f); // normal faces positive Z
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(0f, 0f);
            GL11.glVertex3f( (float)x, (float)y, (float)0);
            GL11.glTexCoord2f(1f, 0f);
            GL11.glVertex3f( (float)x+w, (float)y, (float)0);
            GL11.glTexCoord2f(1f, 1f);
            GL11.glVertex3f( (float)x+w, (float)y+h, (float)0);
            GL11.glTexCoord2f(0f, 1f);
            GL11.glVertex3f( (float)x, (float)y+h, (float)0);
        }
        GL11.glEnd();
        // restore the previous perspective and model views
        setOrthoOff();
    }

    /**
     * Draw a textured quad at the given xyz position in 3D space.  Quad will be drawn
     * with current settings (ie. light, material, depth test, projection, etc.)
     */
    public static void drawQuadZ(int textureHandle, float x, float y, float z, float w, float h) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,textureHandle);
        // draw  textured quad
        GL11.glNormal3f(0.0f, 0.0f, 1.0f); // normal faces positive Z
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(0f, 0f);
            GL11.glVertex3f( x, y, z);
            GL11.glTexCoord2f(1f, 0f);
            GL11.glVertex3f( x+w, y, z);
            GL11.glTexCoord2f(1f, 1f);
            GL11.glVertex3f( x+w, y+h, z);
            GL11.glTexCoord2f(0f, 1f);
            GL11.glVertex3f( x, y+h, z);
        }
        GL11.glEnd();
    }

    //========================================================================
    // Functions to get and set framebuffer pixels
    //========================================================================

    /**
     * Return a ByteBuffer containing ARGB pixels of the entire screen area.
     */
    public static ByteBuffer framePixels() {
		return framePixels(0,0,displayMode.getWidth(),displayMode.getHeight());
    }

    /**
     * Return a ByteBuffer containing ARGB pixels from the given screen area.
     */
    public static ByteBuffer framePixels(int x, int y, int w, int h) {
		// allocate 4 bytes per pixel
        ByteBuffer pixels = allocBytes(w*h*4);
        // Get pixels from frame buffer in ARGB format.
        GL11.glReadPixels(x,y,w,h, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixels);
        return pixels;
    }

    /**
     * Return an int array containing ARGB pixels from the given screen area.
     */
//    public static int[] framePixelsInt(int x, int y, int w, int h) {
//        int[] pixels = new int[w * h];
//        ByteBuffer pixelsBB = framePixels(x, y, w, h);
//        get(pixelsBB,pixels);
//        return pixels;
//    }
//
//    /**
//     * Return the color buffer RGB value at the given screen position as byte[3].
//     *
//     * @param x    screen position
//     * @param y
//     * @return rgb byte array
//     */
//    public static byte[] getPixelColor(int x, int y)
//    {
//    	// color value will be stored in an integer
//    	tmpInt.clear();
//    	// read the framebuffer color value at the given position, as bytes
//    	GL11.glReadPixels(x, y, 1, 1, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, tmpInt);
//    	byte[] rgb = new byte[] {tmpInt.get(0), tmpInt.get(1), tmpInt.get(2)};
//    	return rgb;
//    }
//
//    /**
//     * Return the depth buffer value at the given screen position.
//     *
//     * @param x    screen position
//     * @param y
//     * @return float Z depth value
//     */
//    public static float getPixelDepth(int x, int y)
//    {
//    	return getZDepth(x,y);
//    }

//    /**
//     * Return the stencil buffer value at the given screen position. Stencil values are
//     * typically bytes (0-255).  The value will be returned as an integer.
//     *
//     * @param x    screen position
//     * @param y
//     * @return int stencil value
//     */
//    public static int getPixelStencil(int x, int y)
//    {
//    	return getMaskValue(x,y);
//    }

    /**
     * Save entire screen image to a texture.  Will copy entire screen even
     * if a viewport is in use.  Texture param must be large enough to hold
     * screen image (see makeTextureForScreen()).
     *
     * @param txtrHandle   texture where screen image will be stored
//     * @see frameDraw()
//     * @see makeTextureForScreen()
     */
    public static void frameCopy(int txtrHandle)
    {
        frameCopy(txtrHandle, 0, 0, DM.getWidth(), DM.getHeight());   // entire screen
    }

    /**
     * Save a region of the screen to a texture.  Texture must be large enough to hold
     * screen image.
     *
     * @param txtrHandle   texture where screen region will be stored
//     * @see frameDraw()
//     * @see makeTextureForScreen()
     */
    public static void frameCopy(int txtrHandle, int x, int y, int w, int h)
    {
        GL11.glColor4f(1,1,1,1);                // turn off alpha and color tints
        GL11.glReadBuffer(GL11.GL_BACK);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,txtrHandle);
        // Copy screen to texture
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0,0, x,y,w,h);
    }

    /**
     * Draw the screen-sized image over entire screen area.  The screen image
     * is stored in the given texture at 0,0 (see frameCopy()) and has the
     * same dimensions as the current display mode (DM.getWidth(), DM.getHeight()).
     * <P>
     * Reset the viewport and ortho mode to full screen (viewport may be
     * different proportion than screen if custom aspectRatio is set).  Draw the
     * quad the same size as texture so no stretching or compression of image.
     *
     * @param txtrHandle
     */
    public static void frameDraw(int txtrHandle)
    {
        // keep it opaque
        GL11.glDisable(GL11.GL_BLEND);
        // set viewport to full screen
        GL11.glViewport(0, 0, DM.getWidth(), DM.getHeight());
        // draw square quad that covers entire screen
        drawQuad(txtrHandle, 0, 0, screenTextureSize, screenTextureSize); // draw the full screen image
        // restore viewport to custom aspect ratio
        GL11.glViewport(viewportX, viewportY, viewportW, viewportH);
    }

    /**
     * Save the current frame buffer to a PNG image. Exactly the same as screenShot().
//     * @see screenShot()
     */
    public static void frameSave() {
    	screenShot();
    }

    //========================================================================
    // Functions to render shapes.
    //========================================================================

    /**
     * Draw a rectangle outline in ortho mode (draws in 2D over the scene).
     * <BR>
//     * @see setLineWidth()
//     * @see drawRectZ()
     */
    public static void drawRect(int x, int y, float w, float h) {
        // switch projection to 2D mode
        setOrthoOn();
        // draw rectangle at Z=0
        drawRectZ(x,y,0,w,h);
        // restore the previous perspective and model views
        setOrthoOff();
    }

    /**
     * Draw a rectangle outline in world space.  Uses opengl line_strip to make
     * the rectangle.
     * <BR>
//     * @see setLineWidth()
//     * @see drawRect()
     */
    public static void drawRectZ(int x, int y, int z, float w, float h) {
    	// preserve current settings
    	GL11.glPushAttrib(GL11.GL_TEXTURE_BIT | GL11.GL_LIGHTING_BIT);
        // de-activate texture and light
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        // draw the rectangle
        GL11.glBegin(GL11.GL_LINE_STRIP);
        {
            GL11.glVertex3f( (float)x, (float)y, (float)z);
            GL11.glVertex3f( (float)x+w, (float)y, (float)z);
            GL11.glVertex3f( (float)x+w, (float)y+h, (float)z);
            GL11.glVertex3f( (float)x, (float)y+h, (float)z);
            GL11.glVertex3f( (float)x, (float)y, (float)z);
        }
        GL11.glEnd();
        // draw points at the corners
        GL11.glBegin(GL11.GL_POINTS);
        {
            GL11.glVertex3f( (float)x, (float)y, (float)z);
            GL11.glVertex3f( (float)x+w, (float)y, (float)z);
            GL11.glVertex3f( (float)x+w, (float)y+h, (float)z);
            GL11.glVertex3f( (float)x, (float)y+h, (float)z);
        }
        GL11.glEnd();
        // re-enable settings
        popAttrib();
    }

    /**
     * Draws a circle with the given radius centered at the given world position.
     */
    public static void drawCircle(int x, int y, int radius, int linewidth) {
        // switch projection to 2D mode
        setOrthoOn();
        // draw circle at x,y with z=0
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(x,y,0);
            drawCircle(radius-linewidth, radius, 180);
        }
        GL11.glPopMatrix();
        // restore the previous perspective and model views
        setOrthoOff();
    }

    /**
     * Draws a circle with the given radius centered at the given world position.
     */
    public static void drawCircleZ(int x, int y, int z, int radius, int linewidth) {
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(x,y,z);
            drawCircle(radius-linewidth, radius, 180);
        }
        GL11.glPopMatrix();
    }

    /**
     * Draws a circle centered at 0,0,0.  Use translate() to place circle at desired coords.
     * Inner and outer radius specify width, stepsize is number of degrees for each segment.
     */
    public static void drawCircle(float innerRadius, float outerRadius, int numSegments) {
        int s = 0;     // start
        int e = 360;   // end
        int stepSize = 360/numSegments;   // degrees per segment
        GL11.glBegin(GL11.GL_QUAD_STRIP);
        {
            // add first 2 vertices
            float ts = (float) Math.sin(Math.toRadians(s));
            float tc = (float) Math.cos(Math.toRadians(s));
            GL11.glVertex2f(tc * innerRadius, ts * innerRadius);
            GL11.glVertex2f(tc * outerRadius, ts * outerRadius);
            // add intermediate vertices, snap to {step} degrees
            while ( (s = ( (s + stepSize) / stepSize) * stepSize) < e) {
                ts = (float) Math.sin(Math.toRadians(s));
                tc = (float) Math.cos(Math.toRadians(s));
                GL11.glVertex2f(tc * innerRadius, ts * innerRadius);
                GL11.glVertex2f(tc * outerRadius, ts * outerRadius);
            }
            // add last 2 vertices at end angle
            ts = (float) Math.sin(Math.toRadians(e));
            tc = (float) Math.cos(Math.toRadians(e));
            GL11.glVertex2f(tc * innerRadius, ts * innerRadius);
            GL11.glVertex2f(tc * outerRadius, ts * outerRadius);
        }
        GL11.glEnd();
    }

    /**
     * Render a 2 unit cube centered at origin.  Includes texture coordinates
     * and normals.
     */
    public static void renderCube()
    {
        GL11.glBegin(GL11.GL_QUADS);
        // Front Face
        GL11.glNormal3f( 0.0f, 0.0f, 1.0f);
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Left
        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Right
        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f,  1.0f);	// Top Right
        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f,  1.0f);	// Top Left
        // Back Face
        GL11.glNormal3f( 0.0f, 0.0f, -1.0f);
        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f, -1.0f);	// Bottom Right
        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Right
        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Left
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f, -1.0f);	// Bottom Left
        // Top Face
        GL11.glNormal3f( 0.0f, 1.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Left
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-1.0f,  1.0f,  1.0f);	// Bottom Left
        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f( 1.0f,  1.0f,  1.0f);	// Bottom Right
        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Right
        // Bottom Face
        GL11.glNormal3f( 0.0f, -1.0f, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f(-1.0f, -1.0f, -1.0f);	// Top Right
        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f( 1.0f, -1.0f, -1.0f);	// Top Left
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Left
        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Right
        // Right face
        GL11.glNormal3f( 1.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f, -1.0f);	// Bottom Right
        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Right
        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f,  1.0f);	// Top Left
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Left
        // Left Face
        GL11.glNormal3f( -1.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f, -1.0f);	// Bottom Left
        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Right
        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f,  1.0f);	// Top Right
        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Left
        GL11.glEnd();
    }

    /**
     * draw a cube with the given size, centered at origin.  Include texture coordinates.
     * @param size       length of each side
     * @param segments   # segments to divide each side into
     */
    public static void renderCube(float size, int segments) {
    	float halfsize = size/2f;
    	GL11.glPushMatrix();
    	{
    		GL11.glPushMatrix();
    		{
    			GL11.glTranslatef(0,0,halfsize);
    			renderPlane(size,segments);// front
    		}
    		GL11.glPopMatrix();
    		GL11.glPushMatrix();
    		{
    			GL11.glRotatef(90,0,1,0);
    			GL11.glTranslatef(0,0,halfsize);
    			renderPlane(size,segments);// right
    		}
    		GL11.glPopMatrix();
    		GL11.glPushMatrix();
    		{
    			GL11.glRotatef(180,0,1,0);
    			GL11.glTranslatef(0,0,halfsize);
    			renderPlane(size,segments);// back
    		}
    		GL11.glPopMatrix();
    		GL11.glPushMatrix();
    		{
    			GL11.glRotatef(270,0,1,0);
    			GL11.glTranslatef(0,0,halfsize);
    			renderPlane(size,segments);// left
    		}
    		GL11.glPopMatrix();
    		GL11.glPushMatrix();
    		{
    			GL11.glRotatef(90,1,0,0);
    			GL11.glTranslatef(0,0,halfsize);
    			renderPlane(size,segments);// bottom
    		}
    		GL11.glPopMatrix();
    		GL11.glPushMatrix();
    		{
    			GL11.glRotatef(-90,1,0,0);
    			GL11.glTranslatef(0,0,halfsize);
    			renderPlane(size,segments);// top
    		}
    		GL11.glPopMatrix();
    	}
    	GL11.glPopMatrix();
    }

    /**
     * draw a square plane in the X,Y axis, centered at origin.  Include texture coordinates.
     * @param size       length of each side
     * @param segments   number of segments to divide each side into
     */
    public static void renderPlane(float size, int segments) {
        renderPlane(size,size,segments,segments);
    }

    /**
     * draw a rectangular plane in the X,Y axis, centered at origin, with the specified size and
     * number of divisions.  Texture will cover entire rectangle without repeating.
     * @param length     length of X axis side
     * @param height     length of Y axis side
//     * @param segments   number of segments to divide each side into
     */
    public static void renderPlane(float length, float height, int length_segments, int height_segments) {
        renderPlane(length, height, length_segments, height_segments, 1, 1);
    }

    /**
     * draw a rectangular plane in the X,Y axis, centered at origin.  Include texture coordinates.
     * Scale the UV coordinates to same proportion as plane dimensions. Texture will repeat as
     * specified by the tilefactorU and tilefactorV values.  If tilefactor values are 1, the texture
     * will cover the rectangle without tiling.
     * @param length    length on X axis
//     * @param depth     length on Y axis
//     * @param segments  number of segments to divide each side into
     */
    public static void renderPlane(float length, float height, int length_segments, int height_segments, float tilefactorU, float tilefactorV) {
    	float xpos = - length/2f;
    	float ypos = - height/2f;
    	float segsizeL = length/(float)length_segments;
    	float segsizeH = height/(float)height_segments;
        float maxDimension = (length > height)? length : height;
    	float uvsegsizeL = (length/maxDimension) / (float)length_segments;
    	float uvsegsizeH = (height/maxDimension) / (float)height_segments;
    	GL11.glBegin(GL11.GL_QUADS); {
    		GL11.glNormal3f(0f, 0f, 1f);   // plane is facing up the Z axis
    		for (int x=0; x < length_segments; x++, xpos+=segsizeL) {
    			for (int y=0; y < height_segments; y++, ypos+=segsizeH) {
    				// bottom left
    				GL11.glTexCoord2f((x*uvsegsizeL)*tilefactorU, (y*uvsegsizeH)*tilefactorV);
    				GL11.glVertex3f( xpos, ypos, 0f);
    				// bottom rite
    				GL11.glTexCoord2f(((x*uvsegsizeL)+uvsegsizeL)*tilefactorU, (y*uvsegsizeH)*tilefactorV);
    				GL11.glVertex3f( xpos+segsizeL, ypos,  0f);
    				// top rite
    				GL11.glTexCoord2f(((x*uvsegsizeL)+uvsegsizeL)*tilefactorU, ((y*uvsegsizeH)+uvsegsizeH)*tilefactorV);
    				GL11.glVertex3f( xpos+segsizeL,  ypos+segsizeH, 0f);
    				// top left
    				GL11.glTexCoord2f((x*uvsegsizeL)*tilefactorU, ((y*uvsegsizeH)+uvsegsizeH)*tilefactorV);
    				GL11.glVertex3f( xpos,  ypos+segsizeH, 0f);
    			}
    			ypos = - height/2f; // reset column position
    		}
    	}
    	GL11.glEnd();
    }

    /**
     * draw a rectangular plane in the X,Y axis, centered at origin.  Include texture coordinates.
     * Scale the UV coordinates to same proportion as plane dimensions.
     * @param length    length on X axis
//     * @param depth     length on Y axis
//     * @param segments  number of segments to divide each side into
     */
    public static void renderPlaneORIG(float length, float height, int length_segments, int height_segments) {
    	float xpos = - length/2f;
    	float ypos = - height/2f;
    	float segsizeL = length/(float)length_segments;
    	float segsizeH = height/(float)height_segments;
        float maxDimension = (length > height)? length : height;
    	float uvsegsizeL = (length/maxDimension) / (float)length_segments;
    	float uvsegsizeH = (height/maxDimension) / (float)height_segments;
    	GL11.glBegin(GL11.GL_QUADS); {
    		GL11.glNormal3f(0f, 0f, 1f);   // plane is facing up the Z axis
    		for (int x=0; x < length_segments; x++, xpos+=segsizeL) {
    			for (int y=0; y < height_segments; y++, ypos+=segsizeH) {
    				// bottom left
    				GL11.glTexCoord2f(x*uvsegsizeL, y*uvsegsizeH);
    				GL11.glVertex3f( xpos, ypos, 0f);
    				// bottom rite
    				GL11.glTexCoord2f((x*uvsegsizeL)+uvsegsizeL, y*uvsegsizeH);
    				GL11.glVertex3f( xpos+segsizeL, ypos,  0f);
    				// top rite
    				GL11.glTexCoord2f((x*uvsegsizeL)+uvsegsizeL, (y*uvsegsizeH)+uvsegsizeH);
    				GL11.glVertex3f( xpos+segsizeL,  ypos+segsizeH, 0f);
    				// top left
    				GL11.glTexCoord2f(x*uvsegsizeL, (y*uvsegsizeH)+uvsegsizeH);
    				GL11.glVertex3f( xpos,  ypos+segsizeH, 0f);
    			}
    			ypos = - height/2f; // reset column position
    		}
    	}
    	GL11.glEnd();
    }

    /**
     * Sets glLineWidth() and glPointSize() to the given width.  This will
     * affect geometry drawn using glBegin(GL_LINES), GL_LINE_STRIP, and GL_POINTS.
     * May only work with widths up to 10 (depends on hardware).
     */
    public static void setLineWidth(int width)
    {
    	GL11.glLineWidth(width);
    	GL11.glPointSize(width);
    	//GL11.glEnable(GL11.GL_POINT_SMOOTH);
    	//GL11.glEnable(GL11.GL_LINE_SMOOTH);
    }

    /**
     * Set the current color with RGBA floats in range 0-1.  The current color
     * is disabled when lighting is enabled.  When lighting is enabled (glEnable(GL_LIGHTING))
     * then material colors are in effect and the current color is ignored.
     */
    public static void setColor(float R, float G, float B, float A)
    {
    	GL11.glColor4f(R,G,B,A);
    }

    /**
     * Set the current color with RGBA bytes in range 0-255. The current color
     * is disabled when lighting is enabled.  When lighting is enabled (glEnable(GL_LIGHTING))
     * then material colors are in effect and the current color is ignored.
     */
    public static void setColorB(int R, int G, int B, int A)
    {
    	GL11.glColor4ub((byte)R,(byte)G,(byte)B,(byte)A);
    }

    /**
     * Set the current color to the given RGB or RGBA float array.  Floats are
     * in range 0-1. The current color is disabled when lighting is enabled.
     * When lighting is enabled (glEnable(GL_LIGHTING)) then
     * material colors are in effect and the current color is ignored.
     */
    public static void setColor(float[] rgba)
    {
    	if (rgba != null) {
    		if (rgba.length == 4) {
    			GL11.glColor4f(rgba[0],rgba[1],rgba[2],rgba[3]);
    		}
    		else if (rgba.length == 3) {
    			GL11.glColor4f(rgba[0],rgba[1],rgba[2],1);
    		}
    	}
    }

    //========================================================================
    // Functions to build a character set and draw text strings.
    //
    // Example:
    //           buildFont("Font_tahoma.png");
    //           ...
    //           glPrint(100, 100, 0, "Here's some text");
    //           ...
    //           destroyFont();   // cleanup
    //========================================================================

    static int fontListBase = -1;           // Base Display List For The character set
    static int fontTextureHandle = -1;      // Texture handle for character set image

    /**
     * Build a character set from the given texture image.
     *
     * @param charSetImage   texture image containing 256 characters in a 16x16 grid
     * @param fontWidth      how many pixels to allow per character on screen
     *
//     * @see       destroyFont()
     */
    public static boolean buildFont(String charSetImage, int fontWidth)
    {
    	// make texture from image
    	GLImage textureImg = loadImage(charSetImage);
    	if (textureImg == null) {
    		return false;  // image not found
    	}
    	//pushAttrib();
    	fontTextureHandle = makeTexture(textureImg);
    	// build character set as call list of 256 textured quads
    	buildFont(fontTextureHandle, fontWidth);
    	//popAttrib();
    	return true;
    }

    /**
      * Build the character set display list from the given texture.  Creates
      * one quad for each character, with one letter textured onto each quad.
      * Assumes the texture is a 256x256 image containing every
      * character of the charset arranged in a 16x16 grid.  Each character
      * is 16x16 pixels.  Call destroyFont() to release the display list memory.
      *
      * Should be in ORTHO (2D) mode to render text (see setOrtho()).
      *
      * Special thanks to NeHe and Giuseppe D'Agata for the "2D Texture Font"
      * tutorial (http://nehe.gamedev.net).
      *
//      * @param charSetImage   texture image containing 256 characters in a 16x16 grid
      * @param fontWidth      how many pixels to allow per character on screen
      *
//      * @see       destroyFont()
      */
    public static void buildFont(int fontTxtrHandle, int fontWidth)
    {
        float factor = 1f/16f;
        float cx, cy;
        fontListBase = GL11.glGenLists(256); // Creating 256 Display Lists
        for (int i = 0; i < 256; i++) {
            cx = (float) (i % 16) / 16f;              // X Texture Coord Of Character (0 - 1.0)
            cy = (float) (i / 16) / 16f;              // Y Texture Coord Of Character (0 - 1.0)
            GL11.glNewList(fontListBase + i, GL11.GL_COMPILE); // Start Building A List
            GL11.glBegin(GL11.GL_QUADS);              // Use A 16x16 pixel Quad For Each Character
            GL11.glTexCoord2f(cx, 1 - cy - factor);  // Texture Coord (Bottom Left)
            GL11.glVertex2i(0, 0);
            GL11.glTexCoord2f(cx + factor, 1 - cy - factor); // Texture Coord (Bottom Right)
            GL11.glVertex2i(16, 0);
            GL11.glTexCoord2f(cx + factor, 1 - cy);   // Texture Coord (Top Right)
            GL11.glVertex2i(16, 16);
            GL11.glTexCoord2f(cx, 1 - cy);             // Texture Coord (Top Left)
            GL11.glVertex2i(0, 16);
            GL11.glEnd();                              // Done Building Our Quad (Character)
            GL11.glTranslatef(fontWidth, 0, 0);        // Move To The Right Of The Character
            GL11.glEndList();                          // Done Building The Display List
        } // Loop Until All 256 Are Built
    }

    /**
     * Clean up the allocated display lists for the character set.
     */
    public static void destroyFont()
    {
        if (fontListBase != -1) {
            GL11.glDeleteLists(fontListBase,256);
            fontListBase = -1;
        }
    }

    /**
     * Render a text string in 2D over the scene, using the character set created
     * by buildFont().
     *
     * @param x  screen pixel position of the string
     * @param y
     * @param msg  text string to draw
     */
    public static void print(int x, int y, String msg)
    {
    	print(x,y,msg,0);
    }

    /**
     * Render a text string in 2D over the scene, using the character set created
     * by buildFont().
     *
     * @param x  screen pixel position of the string
     * @param y
     * @param msg  text string to draw
     * @param set  which of the two character sets: 0 or 1
     */
    public static void print(int x, int y, String msg, int set)
    {
    	// if font is not initiallized, try loading default font
    	if (fontListBase == -1 || fontTextureHandle == -1) {
    		if (!buildFont("images/font_tahoma.png", 12)) {
    			err("GLApp.print(): character set has not been created -- see buildFont()");
    			return;
    		}
    	}
    	if (msg != null) {
    		int offset = fontListBase - 32 + (128 * set);
    		// preserve current GL settings
    		pushAttribOrtho();
    		// turn off lighting
    		GL11.glDisable(GL11.GL_LIGHTING);
    		// enable alpha blending, so character background is transparent
    		GL11.glEnable(GL11.GL_BLEND);
    		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    		// enable the charset texture
    		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTextureHandle);
    		// prepare to render in 2D
    		setOrthoOn();
    		// draw the text
    		GL11.glTranslatef(x, y, 0);        // Position The Text (in pixel coords)
    		for(int i=0; i<msg.length(); i++) {
    			GL11.glCallList(offset + msg.charAt(i));
    		}
    		// restore the original positions and views
    		setOrthoOff();
    		// restore previous settings
    		popAttrib();
    	}
    }

    /**
     * Render a text string in model space, using the character set created
     * by buildFont().
     */
    public static void printZ(float x, float y, float z, int set, float scale, String msg)
    {
    	int offset;
    	if (fontListBase == -1 || fontTextureHandle == -1) {
    		// font is not initiallized, try this default
    		if (!buildFont("images/font_tahoma.png", 12)) {
    			err("GLApp.printZ(): character set has not been created -- see buildFont()");
    			return;
    		}
    	}
    	offset = fontListBase - 32 + (128 * set);
    	if (msg != null) {
    		// enable the charset texture
    		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTextureHandle);
    		// draw the text
    		GL11.glPushMatrix();
    		{
    			GL11.glTranslatef(x, y, z); // Position The Text (in pixels coords)
    			GL11.glScalef(scale,scale,scale);  // make it smaller (arbitrary kludge!!!!)
    			for (int i = 0; i < msg.length(); i++) {
    				GL11.glCallList(offset + msg.charAt(i));
    			}
    		}
    		GL11.glPopMatrix();
    	}
    }


    //========================================================================
    // PBuffer functions
    //
    // Pbuffers are offscreen buffers that can be rendered into just like
    // the regular framebuffer.  A pbuffer can be larger than the screen,
    // which allows for the creation of higher resolution images.
    //
    //========================================================================

    /**
     * Create a Pbuffer for use as an offscreen buffer, with the given
     * width and height.  Use selectPbuffer() to make the pbuffer the
     * context for all subsequent opengl commands.  Use selectDisplay() to
     * make the Display the context for opengl commands.
     * <P>
     * @param width
     * @param height
     * @return Pbuffer
//     * @see selectPbuffer(), selectDisplay()
     */
    public static Pbuffer makePbuffer(final int width, final int height) {
    	Pbuffer pbuffer = null;
    	try {
    		pbuffer = new Pbuffer(width, height,
    				new PixelFormat(24, //bitsperpixel
    						8,  //alpha
    						24, //depth
    						8,  //stencil
    						0), //samples
    						null,
    						null);
    	} catch (LWJGLException e) {
    		err("GLApp.makePbuffer(): exception " + e);
    	}
    	return pbuffer;
    }

    /**
     * Make the pbuffer the current context for opengl commands.  All following
     * gl functions will operate on this buffer instead of the display.
     * <P>
     * NOTE: the Pbuffer may be recreated if it was lost since last used.  It's
     * a good idea to use:
     * <PRE>
     *         pbuff = selectPbuffer(pbuff);
     * </PRE>
     * to hold onto the new Pbuffer reference if Pbuffer was recreated.
     *
     * @param pb  pbuffer to make current
     * @return    Pbuffer
//     * @see       selectDisplay(), makePbuffer()
     */
    public static Pbuffer selectPbuffer(Pbuffer pb) {
    	if (pb != null) {
    		try {
    			// re-create the buffer if necessary
    			if (pb.isBufferLost()) {
    				int w = pb.getWidth();
    				int h = pb.getHeight();
    				msg("GLApp.selectPbuffer(): Buffer contents lost - recreating the pbuffer");
    				pb.destroy();
    				pb = makePbuffer(w, h);
    			}
    			// select the pbuffer for rendering
    			pb.makeCurrent();
    		}
    		catch (LWJGLException e) {
    			err("GLApp.selectPbuffer(): exception " + e);
    		}
    	}
    	return pb;
    }

    /**
     * Make the Display the current context for OpenGL commands.  Subsequent
     * gl functions will operate on the Display.
     *
//     * @see selectPbuffer()
     */
    public static void selectDisplay()
    {
    	try {
    		Display.makeCurrent();
    	} catch (LWJGLException e) {
    		err("GLApp.selectDisplay(): exception " + e);
    	}
    }

    /**
     * Copy the pbuffer contents to a texture.  (Should this use glCopyTexSubImage2D()?
     * Is RGB the fastest format?)
     */
    public static void frameCopy(Pbuffer pbuff, int textureHandle) {
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D,textureHandle);
    	GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 0, 0, pbuff.getWidth(), pbuff.getHeight(), 0);
    }

    /**
     * Save the current frame buffer to a PNG image. Same as
     * screenShot(filename) but the screenshot filename will be automatically
     * set to <applicationClassName>-<timestamp>.png
     */
    public static void screenShot() {
    	screenShot(0, 0, displayMode.getWidth(), displayMode.getHeight(), makeTimestamp() + ".png");
    }

    /**
     * Save the current frame buffer to a PNG image. Can also
     * be used with the PBuffer class to copy large images or textures that
     * have been rendered into the offscreen pbuffer.
     */
    public static void screenShot(String imageFilename) {
    	screenShot(0, 0, displayMode.getWidth(), displayMode.getHeight(), imageFilename);
    }

    /**
     * Save the current Pbuffer to a PNG image. Same as screenShot(filename)
     * but the Pbuffer will be saved instead of the framebuffer, and the
     * screenshot filename will be set to <applicationClassName>-<timestamp>.png
     * NOTE: Have to call selectPbuffer() before calling this function.
     */
    public static void screenShot(Pbuffer pb) {
    	screenShot(0, 0, pb.getWidth(), pb.getHeight(), makeTimestamp() + ".png");
    }

    /**
     * Save a region of the current render buffer to a PNG image.  If the current
     * buffer is the framebuffer then this will work as a screen capture.  Can
     * also be used with the PBuffer class to copy large images or textures that
     * have been rendered into the offscreen pbuffer.
     * <P>
     * WARNING: this function hogs memory!  Call java with more memory
     * (java -Xms128m -Xmx128m)
     * <P>
//     * @see   selectPbuffer( org.lwjgl.opengl.Pbuffer)
//     * @see   selectDisplay()
//     * @see   savePixelsToPNG()
     */
    public static void screenShot(int x, int y, int width, int height, String imageFilename) {
    	// allocate space for ARBG pixels
    	ByteBuffer framebytes = allocBytes(width * height * SIZE_INT);
    	int[] pixels = new int[width * height];
    	// grab the current frame contents as ARGB ints (BGRA ints reversed)
    	GL11.glReadPixels(x, y, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, framebytes);
    	// copy ARGB data from ByteBuffer to integer array
    	framebytes.asIntBuffer().get(pixels, 0, pixels.length);
    	// free up this memory
    	framebytes = null;
    	// flip the pixels vertically and save to file
    	GLImage.savePixelsToPNG(pixels, width, height, imageFilename, true);
    }

	/**
	 * Save a ByteBuffer of ARGB pixels to a PNG file.
	 * If flipY is true, flip the pixels on the Y axis before saving.
	 */
	public static void savePixelsToPNG(ByteBuffer framebytes, int width, int height, String imageFilename, boolean flipY) {
		if (framebytes != null && imageFilename != null) {
			// copy ARGB data from ByteBuffer to integer array
			int[] pixels = new int[width * height];
			framebytes.asIntBuffer().get(pixels, 0, pixels.length);
			// save pixels to file
			GLImage.savePixelsToPNG(pixels, width, height, imageFilename, flipY);
		}
	}

	/**
	 * Save the contents of the current render buffer to a PNG image. This is
	 * an older version of screenShot() that used the default OpenGL GL_RGBA
	 * pixel format which had to be swizzled into an ARGB format. I'm
	 * keeping the function here for reference.
	 * <P>
	 * If the current buffer is the framebuffer then this will work as a screen capture.
	 * Can also be used with the PBuffer class to copy large images or textures that
	 * have been rendered into the offscreen pbuffer.
	 * <P>
	 * WARNING: this function hogs memory!  Call java with more memory
	 * (java -Xms128m -Xmx128)
	 * <P>
//	 * @see   selectPbuffer(), selectDisplay()
	 */
	public static void screenShotRGB(int width, int height, String saveFilename) {
		// allocate space for RBG pixels
		ByteBuffer framebytes = GLApplication.allocBytes(width * height * 3);
		int[] pixels = new int[width * height];
		int bindex;
		// grab a copy of the current frame contents as RGB (has to be UNSIGNED_BYTE or colors come out too dark)
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, framebytes);
		// copy RGB data from ByteBuffer to integer array
		for (int i = 0; i < pixels.length; i++) {
			bindex = i * 3;
			pixels[i] =
				0xFF000000                                          // A
				| ((framebytes.get(bindex) & 0x000000FF) << 16)     // R
				| ((framebytes.get(bindex+1) & 0x000000FF) << 8)    // G
				| ((framebytes.get(bindex+2) & 0x000000FF) << 0);   // B
		}
		// free up some memory
		framebytes = null;
		// save to file (flip Y axis before saving)
		GLImage.savePixelsToPNG(pixels, width, height, saveFilename, true);
	}

	//========================================================================
	// Stencil functions
	//========================================================================

	/**
	 * clear the stencil buffer
	 */
	public static void clearMask() {
		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
	}


	/**
	 *  Begin creating a mask.  This function turns off the color and depth buffers
	 *  so all subsequent drawing will go only into the stencil buffer.
	 *  To use:
	 *          beginMask(1);
	 *          renderModel();  // draw some geometry
	 *          endMask();
	 */
	public static void beginMask(int maskvalue) {
		// turn off writing to the color buffer and depth buffer
		GL11.glColorMask(false, false, false, false);
		GL11.glDepthMask(false);

		// enable stencil buffer
		GL11.glEnable(GL11.GL_STENCIL_TEST);

		// set the stencil test to ALWAYS pass
		GL11.glStencilFunc(GL11.GL_ALWAYS, maskvalue, 0xFFFFFFFF);
		// REPLACE the stencil buffer value with maskvalue whereever we draw
		GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
	}

	/**
	 *  End the mask.  Freeze the stencil buffer and activate the color and depth buffers.
	 */
	public static void endMask() {
		// don't let future drawing modify the contents of the stencil buffer
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);

		// turn the color and depth buffers back on
		GL11.glColorMask(true, true, true, true);
		GL11.glDepthMask(true);
	}

	/**
	 *  Restrict rendering to the masked area.
	 *  To use:
	 *          GLStencil.beginMask(1);
	 *          renderModel();
	 *          GLStencil.endMask();
	 */
	public static void activateMask(int maskvalue) {
		// enable stencil buffer
		GL11.glEnable(GL11.GL_STENCIL_TEST);

		// until stencil test is disabled, only write to areas where the
		// stencil buffer equals the mask value
		GL11.glStencilFunc(GL11.GL_EQUAL, maskvalue, 0xFFFFFFFF);
	}

	/**
	 *  turn off the stencil test so stencil has no further affect on rendering.
	 */
	public static void disableMask() {
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}

//	/**
//	 * Return the stencil buffer value at the given screen position.
//	 */
//	public static int getMaskValue(int x, int y)
//	{
//		tmpByte.clear();
//		// read the stencil value at the given position, as an unsigned byte, store it in tmpByte
//		GL11.glReadPixels(x, y, 1, 1, GL11.GL_STENCIL_INDEX, GL11.GL_UNSIGNED_BYTE, tmpByte);
//		return (int) tmpByte.get(0);
//	}

    //========================================================================
    // Native IO Buffer allocation functions
    //
    // These functions create and populate the native buffers used by LWJGL.
    //========================================================================

    public static ByteBuffer allocBytes(int howmany) {
        return ByteBuffer.allocateDirect(howmany * SIZE_BYTE).order(ByteOrder.nativeOrder());
    }

    public static IntBuffer allocInts(int howmany) {
        return ByteBuffer.allocateDirect(howmany * SIZE_INT).order(ByteOrder.nativeOrder()).asIntBuffer();
    }

    public static FloatBuffer allocFloats(int howmany) {
        return ByteBuffer.allocateDirect(howmany * SIZE_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    /**
     * make a time stamp for filename
     * @return a string with format "YYYYMMDD-hhmmss"
     */
    public static String makeTimestamp()
    {
    	Calendar now = Calendar.getInstance();
    	int year = now.get(Calendar.YEAR);
    	int month = now.get(Calendar.MONTH) + 1;
    	int day = now.get(Calendar.DAY_OF_MONTH);
    	int hours = now.get(Calendar.HOUR_OF_DAY);
    	int minutes = now.get(Calendar.MINUTE);
    	int seconds = now.get(Calendar.SECOND);
    	String datetime =  ""
    		+ year
    		+ (month < 10 ? "0" : "") + month
    		+ (day < 10 ? "0" : "") + day
    		+ "-"
    		+ (hours < 10 ? "0" : "") + hours
    		+ (minutes < 10 ? "0" : "") + minutes
    		+ (seconds < 10 ? "0" : "")  + seconds;
    	return datetime;
    }

    /**
     * Return true if the OpenGL context supports the given OpenGL extension.
     */
    public static boolean extensionExists(String extensionName) {
    	if (OpenGLextensions == null) {
    		String[] GLExtensions = GL11.glGetString(GL11.GL_EXTENSIONS).split(" ");
    		OpenGLextensions = new Hashtable();
    		for (int i=0; i < GLExtensions.length; i++) {
    			OpenGLextensions.put(GLExtensions[i].toUpperCase(),"");
    		}
    	}
    	return (OpenGLextensions.get(extensionName.toUpperCase()) != null);
    }

    /**
     * Show a debug message on the system console (calls System.out.println()).  If
     * showMessages flag is false, does nothing.
     * @param text
     */
    public static void msg(String text) {
    	if (showMessages) {
    		System.out.println(text);
    	}
    }

    /**
     * Show an error message on the system console (calls System.out.println()).
     * Does not check showMessages flag.
     * @param text
     */
    public static void err(String text) {
    	System.out.println(text);
    }

}