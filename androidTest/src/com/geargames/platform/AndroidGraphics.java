package com.geargames.platform;

//import com.geargames.common.packer.PAffine;
//import com.geargames.common.packer.PFont;
//import com.geargames.common.packer.PFrame;
//import com.geargames.common.util.ArrayChar;
//import com.geargames.common.util.Region;

import android.opengl.GLU;

import javax.microedition.khronos.opengles.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * User: abarakov
 * Date: 16.03.13
 */
//todo: Переименовать в AndroidCanvas?
public class AndroidGraphics {
//    //todo: перетащить константы в TextLayout
//    int HCENTER  = 1 << 0;
//    int VCENTER  = 1 << 1;
//    int LEFT     = 1 << 2;
//    int RIGHT    = 1 << 3;
//    int TOP      = 1 << 4;
//    int BOTTOM   = 1 << 5;
//    int BASELINE = 6;
//    int SOLID    = 7;
//    int DOTTED   = 8;

    private GL10 gl10;
    private GL11 gl11;

    private boolean perspectiveModeEnabled = false;
    //todo: для координат viewport завести поле типа Rect или Region
    int viewportLeft;
    int viewportRight;
    int viewportTop;
    int viewportBottom;

    private int color;
    private int backgroundColor; // ClearColor
//    private byte red;
//    private byte green;
//    private byte blue;
//    // transparency
//    // Было:  0..100; 100 - полная прозрачность
//    // Стало: 0..255;   0 - полная прозрачность
//    private byte alpha;

    private ShortBuffer vertexBuffer;  // буфер вершинных координат
    private FloatBuffer colorBuffer;   // буфер цвета вершин
    private FloatBuffer textureBuffer; // буфер текстурных координат

    private static final int VERTEX_TYPE  = GL10.GL_SHORT;
    private static final int COLOR_TYPE   = GL10.GL_FLOAT; //todo: GL_FLOAT -> GL_UNSIGNED_BYTE ?
    private static final int TEXTURE_TYPE = GL10.GL_FLOAT;

    // Буфер глубины
    private boolean depthTestEnabled = false;
//    private byte depthBits = ;

    // Буфер трафарета
    private boolean stencilTestEnabled = false;

//    private byte    colorBits    = ;
//    private byte    stencilBits  = ;
//    private boolean doubleBuffer = true;
//    private boolean vertSyncMode = true;
//    private byte    maximumFPS   = 30;

    //todo: реализовать следующие свойства
    private short pointSize;
    private boolean pointSmoothEnabled;
    private short lineWidth;
    private boolean lineSmoothEnabled = false;
    private short linePattern;

    public AndroidGraphics(GL10 gl) {
        if (gl == null) {
            throw new IllegalArgumentException("gl = null");
        }
        this.gl10 = gl;
        if (gl instanceof GL11) {
            this.gl11 = (GL11) gl;
        } else {
            this.gl11 = null;
        }

        // По умолчанию в OpenGL определен белый непрозрачный цвет для рендеринга примитивов и черный как фоновый.
        this.color           = 0xffffffff;
        this.backgroundColor = 0xff000000;
//        this.red   = (byte)255;
//        this.green = (byte)255;
//        this.blue  = (byte)255;
//        this.alpha = (byte)255;

        final int MAX_VERTICES = 16;

        ByteBuffer bb; //todo: Нужен автоматически расширяющийся буфер

        bb = ByteBuffer.allocateDirect(MAX_VERTICES * 2 * 2); // на каждую вершину по 2 координаты (x,y) типа short (2 байта)
        bb.order(ByteOrder.nativeOrder());
        bb.position(0);
        vertexBuffer = bb.asShortBuffer();

        bb = ByteBuffer.allocateDirect(MAX_VERTICES * 4 * 4); // на каждую вершину по 4 цветовых компонента (ARGB) типа float (4 байта)
        bb.order(ByteOrder.nativeOrder());
        bb.position(0);
        colorBuffer = bb.asFloatBuffer();

        bb = ByteBuffer.allocateDirect(MAX_VERTICES * 2 * 4); // на каждую вершину по 2 координаты (s,t) типа float (4 байта)
        bb.order(ByteOrder.nativeOrder());
        bb.position(0);
        textureBuffer = bb.asFloatBuffer();
        configureGraphics();
    }

    /**
     * Настроить графический контекст.
     */
    public void configureGraphics() {
        //todo: цвет должен задаваться опционально
        gl10.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);                   checkGLError("glClearColor");
        // Настройка альфа-теста
//        gl10.glEnable(GL10.GL_ALPHA_TEST);
//        gl10.glAlphaFunc();
        // Тип альфа-теста, здесь он настраивается, но по умолчанию выключен
        //todo: GL_ONE -> GL_SRC_ALPHA
        gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);  checkGLError("glBlendFunc");

        // Тест глубины (выключен)
        gl10.glClearDepthf(1.0f);                                    checkGLError("glClearDepthf");
        gl10.glDisable(GL10.GL_DEPTH_TEST);                          checkGLError("glDisable");

        // Плавное цветовое сглаживание
        gl10.glShadeModel(GL10.GL_SMOOTH);                           checkGLError("glShadeModel");

        // Сглаживание линий и полигонов
        if (lineSmoothEnabled) {
            gl10.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);   checkGLError("glHint");
            gl10.glEnable(GL10.GL_LINE_SMOOTH);                      checkGLError("glEnable");
        }

//        gl10.glEnable(GL10.GL_TEXTURE_2D);                           checkGLError("glEnable");
//        gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);       checkGLError("glEnableClientState");
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);              checkGLError("glEnableClientState");

        //todo: Правильная константа?
//      gl10.glEnable(GL10.GL_DEPTH_TEST);                           checkGLError("glEnable");

    }

//    public GL10 getGl() {
//        return gl10;
//    }

    public void setViewport(int x, int y, int width, int height) {
        viewportLeft   = x;
        viewportRight  = x + width;
        viewportTop    = y ;
        viewportBottom = y + height;
        gl10.glViewport(x, y, width, height);
        checkGLError("glViewport");
    }

    /**
     * Очистить буферы и настроить матрицы проекции и просмотра, тем самым подготовить графический контекст к очередному
     * рендерингу.
     */
    public void clearBuffers() {
//        if ((byte) (backgroundColor >>> 24) == (byte) 255) {
//            gl10.glDisable(GL10.GL_BLEND);
//        } else {
//            gl10.glEnable(GL10.GL_BLEND);
//        }
        int clearFlags = GL10.GL_COLOR_BUFFER_BIT;    // Требуется очистка буфера цвета
        if (depthTestEnabled) {
            clearFlags |= GL10.GL_DEPTH_BUFFER_BIT;   // Требуется очистка буфера глубины
        }
        if (stencilTestEnabled) {
            clearFlags |= GL10.GL_STENCIL_BUFFER_BIT; // Требуется очистка буфера трафарета
        }
        gl10.glClear(clearFlags);               checkGLError("glClear");

        //todo: Применить glEnable/glDisable(GL10.GL_BLEND) для color

        // Выбор матрицы проектирования
        gl10.glMatrixMode(GL10.GL_PROJECTION);  checkGLError("glMatrixMode");
        // Сброс матрицы проектирования
        gl10.glLoadIdentity();                  checkGLError("glLoadIdentity");
        // Установка матрицы проекции
//        if (perspectiveModeEnabled) {
//            int viewportHeight = viewportBottom - viewportTop;
//            float aspect = (viewportRight - viewportLeft) / (viewportHeight == 0 ? 1 : viewportHeight);
//            float fovY = 45;
//            GLU.gluPerspective(gl10, fovY, aspect, 10, 1000);
//            checkGLError("gluPerspective");
//        } else {
            // Устанавливаем ортографическую проекцию, где система координат настраивается таким образом, чтобы верхний
            // левый угол совпадал с координатой (viewportLeft, viewportTop), а левый нижний совпадал с координатой
            // (viewportRight, viewportBottom).
//            GLU.gluOrtho2D(gl10, viewportLeft, viewportRight, viewportBottom, viewportTop);  checkGLError("gluOrtho2D");
            gl10.glOrthof(viewportLeft, viewportRight, viewportBottom, viewportTop, -1, 1);  checkGLError("glOrthox");
//
//        }

        // Выбор матрицы просмотра
        gl10.glMatrixMode(GL10.GL_MODELVIEW);   checkGLError("glMatrixMode");
        // Сброс матрицы просмотра
        gl10.glLoadIdentity();                  checkGLError("glLoadIdentity");
    }

    /**
     * Переключить буферы. Если включена двойная буфферизация, то содержимое буфера будет перенесено на графический контекст.
     */
    public void swapBuffers() {
        //todo: Нужно?
//        gl10.glFinish();
//        gl10.glFlush();
    }

    //******************************************************************************************************************
    //**     Изображения                                                                                              **
    //******************************************************************************************************************

//    // Нарисовать изображение в соответствующих координатах и с заданным выравниванием
//    void drawImage(Image image, int x, int y);
//    void drawImage(Image image, float x, float y);
//
//    // Нарисовать часть изображения image ограниченную прямоугольником (srcX1, srcY1), (srcX2, srcY2) в пределах
//    // целевого прямоугольника (destX1, destY1), (destX2, destY2)
//    boolean drawImage(Image image,
//                      int srcX1, int srcY1, int srcX2, int srcY2,
//                      int destX1, int destY1, int destX2, int destY2);
//    boolean drawImage(Image image,
//                      float srcX1, float srcY1, float srcX2, float srcY2,
//                      float destX1, float destY1, float destX2, float destY2);
////    boolean drawImage(Image image, Rect source, Rect dest);
//    boolean drawImage(Image image, Region source, Region dest);
//
//    // Нарисовать часть изображения
//    // drawRegion
//    void drawSubimage(Image image, int srcX, int srcY, int width, int height, int dstX, int dstY/*, PAffine affine*/);
//    void drawSubimage(Image image, float srcX, float srcY, float width, float height, float dstX, float dstY);
//
//    // Нарисовать фрейм, часть изображения в заданных координатах
//    public void drawFrame(PFrame frame, int x, int y);
//    public void drawFrame(PFrame frame, float x, float y);

    //todo: Добавить возможность рисования изображений или их частей с заполнением, т.е. с дублированием изображения

////    void onCache(int len);//включить кеширование картинок
//
//    void addTexture(Image image);
//
//    Image createImage(byte[] array, int i, int data_len) throws IOException;
//
//    Image createImage();

    public boolean glIsTexture(int texture) {
        return gl11.glIsTexture(texture);
    }

    public int getMaxTextureSize() {
        int[] params = new int[1];
        gl10.glGetIntegerv(gl10.GL_MAX_TEXTURE_SIZE, params, 0);
        checkGLError("glGetIntegerv");
        return params[0];
    }

    //******************************************************************************************************************
    //**     Рисуемые точки                                                                                           **
    //******************************************************************************************************************

    /**
     * Получить значение размера рисуемых точек.
     * @return значение в пикселях
     */
    public float getPointSize() {
        //todo: функция не протестирована!
        return getOGLFloatValue1(gl11.GL_POINT_SIZE);
    }

    /**
     * Установить значение размера рисуемых точек.
     * Размер линии должен быть больше 0.0 и по умолчанию равен 1.0.
     * Важно: стоит учесть, что существуют ограничения на минимальный и максимальный размеры, а также размер должен быть
     * кратным определенному значению шага, но получить значение шага невозможно под андроидом. Также следует различать,
     * сглаженные и несглаженные точки, для каждой из них, значения PointSizeMin и PointSizeMax могут быть различны.
     * @param size размер точек в пикселях
     * @see #getAliasedPointSizeMin()
     * @see #getAliasedPointSizeMax()
     * @see #getSmoothPointSizeMin()
     * @see #getSmoothPointSizeMax()
     */
    public void setPointSize(float size) {
        //todo: функция не протестирована!
        gl10.glPointSize(size);
    }

    //todo: что такое GL_POINT_SIZE_MIN и GL_POINT_SIZE_MAX ?

    /**
     * Получить минимально возможное значение размера, с которым могут рисоваться несглаженные точки.
     * @return
     */
    public float getAliasedPointSizeMin() {
        //todo: функция не протестирована!
        return getOGLFloatValue2(gl10.GL_ALIASED_POINT_SIZE_RANGE, 0);
    }

    /**
     * Получить максимально возможное значение размера, с которым могут рисоваться несглаженные точки.
     * @return
     */
    public float getAliasedPointSizeMax() {
        //todo: функция не протестирована!
        return getOGLFloatValue2(gl10.GL_ALIASED_POINT_SIZE_RANGE, 1);
    }

    /**
     * Получить минимально возможное значение размера, с которым могут рисоваться сглаженные точки.
     * @return
     */
    public float getSmoothPointSizeMin() {
        //todo: функция не протестирована!
        return getOGLFloatValue2(gl10.GL_SMOOTH_POINT_SIZE_RANGE, 0);
    }

    /**
     * Получить максимально возможное значение размера, с которым могут рисоваться сглаженные точки.
     * @return
     */
    public float getSmoothPointSizeMax() {
        //todo: функция не протестирована!
        return getOGLFloatValue2(gl10.GL_SMOOTH_POINT_SIZE_RANGE, 1);
    }

    // Нарисовать точку в указанных координатах
    public void drawPoint(short x, short y) {
        vertexBuffer.position(0);
        vertexBuffer.put(x);
        vertexBuffer.put(y);
        vertexBuffer.position(0);
        gl10.glVertexPointer(2, VERTEX_TYPE, 0, vertexBuffer);  checkGLError("glVertexPointer");
        gl10.glDrawArrays(GL10.GL_POINTS, 0, 1);                checkGLError("glDrawArrays");
    }

//    public void drawPoint(float x, float y) {
//
//    }

//    /**
//     * Нарисовать множество точек в указанных координатах
//     * @param points массив координат точек, где каждая точка представлена двумя значениями в массиве, следующими друг за
//     *               другом. Очевидно, что массив должен иметь четную длинну.
//     */
//    public void drawPoints(int[] points) {
//
//    }
//
//    public void drawPoints(float[] points) {
//
//    }

    //******************************************************************************************************************
    //**     Линии                                                                                                    **
    //******************************************************************************************************************

    /**
     * Получить значение толщины линии.
     * @return значение в пикселях
     */
    public float getLineWidth() {
        //todo: функция не протестирована!
        return getOGLFloatValue1(gl11.GL_LINE_WIDTH);
    }

    /**
     * Установить толщину линии в пикселях.
     * Ширина линии должна быть больше 0.0 и по умолчанию равна 1.0.
     * Важно: стоит учесть, что существуют ограничения на минимальную и максимальную ширины линий, а также ширина должна
     * быть кратной определенному значению шага, но получить значение шага невозможно под андроидом. Также следует
     * различать, сглаженные и несглаженные линии, для каждой из них, значения LineWidthMin и LineWidthMax могут быть
     * различны.
     * @param width значение толщины линий в пикселях.
     * @see #getAliasedLineWidthMin()
     * @see #getAliasedLineWidthMax()
     * @see #getSmoothLineWidthMin()
     * @see #getSmoothLineWidthMax()
     */
    public void setLineWidth(float width) {
        //todo: функция не протестирована!
        gl10.glLineWidth(width);
    }

    /**
     * Получить минимально возможное значение ширины, с которым могут рисоваться несглаженные линии.
     * @return
     */
    public float getAliasedLineWidthMin() {
        //todo: функция не протестирована!
        return getOGLFloatValue2(gl10.GL_ALIASED_LINE_WIDTH_RANGE, 0);
    }

    /**
     * Получить максимально возможное значение ширины, с которым могут рисоваться несглаженные линии.
     * @return
     */
    public float getAliasedLineWidthMax() {
        //todo: функция не протестирована!
        return getOGLFloatValue2(gl10.GL_ALIASED_LINE_WIDTH_RANGE, 1);
    }

    /**
     * Получить минимально возможное значение ширины, с которым могут рисоваться сглаженные линии.
     * @return
     */
    public float getSmoothLineWidthMin() {
        //todo: функция не протестирована!
        return getOGLFloatValue2(gl10.GL_SMOOTH_LINE_WIDTH_RANGE, 0);
    }

    /**
     * Получить максимально возможное значение ширины, с которым могут рисоваться сглаженные линии.
     * @return
     */
    public float getSmoothLineWidthMax() {
        //todo: функция не протестирована!
        return getOGLFloatValue2(gl10.GL_SMOOTH_LINE_WIDTH_RANGE, 1);
    }

    /**
     * Установить шаблон линий.
     * Шаблонирование линий не доступно под андроидом.
     */
//    public void setLineStipple(float factor, short pattern) {
//        gl10.glLineStipple();
//        gl10.glEnable(GL10.GL_LINE_STIPPLE);
//    }

//    public void resetLineStipple() {
//        gl10.glDisable(GL10.GL_LINE_STIPPLE);
//    }

    // Нарисовать линию от точки (x1, y1) до (x2, y2)
    public void drawLine(short x1, short y1, short x2, short y2) {
        vertexBuffer.position(0);
        vertexBuffer.put(x1);
        vertexBuffer.put(y1);
        vertexBuffer.put(x2);
        vertexBuffer.put(y2);
        vertexBuffer.position(0);
        gl10.glVertexPointer(2, VERTEX_TYPE, 0, vertexBuffer);  checkGLError("glVertexPointer");
        gl10.glDrawArrays(GL10.GL_LINES, 0, 2);                 checkGLError("glDrawArrays");
    }

//    public void drawLine(float x1, float y1, float x2, float y2) {
//        floatBuffer.position(0);
//        floatBuffer.put(x1);
//        floatBuffer.put(y1);
//        floatBuffer.put(x2);
//        floatBuffer.put(y2);
//        floatBuffer.position(0);
//        gl10.glVertexPointer(2, GL10.GL_FLOAT, 0, floatBuffer);  checkGLError("glVertexPointer");
//        gl10.glDrawArrays(GL10.GL_LINES, 0, 2);                  checkGLError("glDrawArrays");
//    }

//    /**
//     * Нарисовать ломанную линию
//     * @param points массив координат точек, где каждая точка представлена двумя значениями в массиве, следующими друг за
//     *               другом. Каждая линия рисуется от точки i до точки i+1. Ломанная линия нигде не прерывается.
//     */
//    void drawLines(int[] points);
//    void drawLines(float[] points);

    //******************************************************************************************************************
    //**     Прямоугольники                                                                                           **
    //******************************************************************************************************************

    // Нарисовать прямоугольную рамку без заполнения этого прямоугольника.
    public void drawRect(short x, short y, short width, short height) {
        vertexBuffer.position(0);
        vertexBuffer.put(x);                   // v0
        vertexBuffer.put(y);
        vertexBuffer.put((short)(x + width));  // v1
        vertexBuffer.put(y);
        vertexBuffer.put((short)(x + width));  // v2
        vertexBuffer.put((short)(y + height));
        vertexBuffer.put(x);                   // v3
        vertexBuffer.put((short)(y + height));
        vertexBuffer.position(0);
        gl10.glVertexPointer(2, VERTEX_TYPE, 0, vertexBuffer);  checkGLError("glVertexPointer");
        gl10.glDrawArrays(GL10.GL_LINE_LOOP, 0, 4);             checkGLError("glDrawArrays");
    }

//    public void drawRect(float x, float y, float width, float height) {
//        floatBuffer.position(0);
//        floatBuffer.put(x);
//        floatBuffer.put(y);
//        floatBuffer.put(x + width);
//        floatBuffer.put(y);
//        floatBuffer.put(x + width);
//        floatBuffer.put(y + height);
//        floatBuffer.put(x);
//        floatBuffer.put(y + height);
//        floatBuffer.position(0);
//        gl10.glVertexPointer(2, GL10.GL_FLOAT, 0, floatBuffer);  checkGLError("glVertexPointer");
//        gl10.glDrawArrays(GL10.GL_LINE_LOOP, 0, 4);              checkGLError("glDrawArrays");
//    }

////    void drawRect(Rect rect);
//    void drawRect(Region region);

    //******************************************************************************************************************
    //**     Другие фигуры                                                                                            **
    //******************************************************************************************************************

//    void drawCircle(float cx, float cy, float radius, android.graphics.Paint paint);
//
//    void drawArc(android.graphics.RectF oval, float startAngle, float sweepAngle, boolean useCenter);
//
//    void drawRoundRect(android.graphics.RectF rect, float rx, float ry);

    //******************************************************************************************************************
    //**     Полигоны с заливкой                                                                                      **
    //******************************************************************************************************************

    // Нарисовать заполненную прямоугольную область
    public void fillRect(short x, short y, short width, short height) {
        vertexBuffer.position(0);
        vertexBuffer.put(x);                   // v0
        vertexBuffer.put(y);
        vertexBuffer.put(x);                   // v1
        vertexBuffer.put((short)(y + height));
        vertexBuffer.put((short)(x + width));  // v2
        vertexBuffer.put(y);
        vertexBuffer.put((short)(x + width));  // v3
        vertexBuffer.put((short)(y + height));
        vertexBuffer.position(0);
        gl10.glVertexPointer(2, VERTEX_TYPE, 0, vertexBuffer);  checkGLError("glVertexPointer");
        gl10.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);        checkGLError("glDrawArrays");
    }
//    void fillRect(float x, float y, float width, float height);
////    void fillRect(Rect rect);
//    void fillRect(Region region);

    // Нарисовать заполненный выпуклый многоугольник, причем для каждой вершины фигуры возможно задать собственный цвет.
    // fillPolygon2D
    public void fillRect(short x, short y, short width, short height, int[] colors) {
//        if (colors.length != 4) {
//            //todo: вывести ошибку
//        }
        vertexBuffer.position(0);
        vertexBuffer.put(x);                   // v0
        vertexBuffer.put(y);
        vertexBuffer.put(x);                   // v1
        vertexBuffer.put((short)(y + height));
        vertexBuffer.put((short)(x + width));  // v2
        vertexBuffer.put(y);
        vertexBuffer.put((short)(x + width));  // v3
        vertexBuffer.put((short)(y + height));
        vertexBuffer.position(0);
        gl10.glVertexPointer(2, VERTEX_TYPE, 0, vertexBuffer);  checkGLError("glVertexPointer");

        colorBuffer.position(0);
        for (int i = 0; i < colors.length; i++) {
            colorBuffer.put(((colors[i] >>> 16) & 0xff) / 255f);  // red
            colorBuffer.put(((colors[i] >>>  8) & 0xff) / 255f);  // green
            colorBuffer.put(((colors[i]       ) & 0xff) / 255f);  // blue
            colorBuffer.put(((colors[i] >>> 24) & 0xff) / 255f);  // alpha
        }
        colorBuffer.position(0);
        gl10.glEnableClientState(GL10.GL_COLOR_ARRAY);          checkGLError("glEnableClientState");  //todo: Делать это из другого места
        gl10.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);  checkGLError("glColorPointer");
        gl10.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);        checkGLError("glDrawArrays");
        gl10.glDisableClientState(GL10.GL_COLOR_ARRAY);         checkGLError("glDisableClientState"); //todo: Делать это из другого места
    }

    //******************************************************************************************************************
    //**     Настройки цвета                                                                                          **
    //******************************************************************************************************************

    // Вернуть и установить цвет.
    // Цвет кодируется в формате ARGB
    public int getColorARGB() {
//        return  alpha << 24 |
//                red   << 16 |
//                green <<  8 |
//                blue;
        return color;
    }

    public int setColorARGB(int color) {
        int colorBackup = getColorARGB();
        if (colorBackup != color) {
//            alpha = (byte)(color >>> 24);
//            red   = (byte)(color >>> 16);
//            green = (byte)(color >>> 8);
//            blue  = (byte)(color);
            this.color = color;
            colorChanged();
        }
        return colorBackup;
    }

    public int setColorARGB(byte red, byte green, byte blue, byte alpha) {
        int newColor =
                (alpha & 0xff) << 24 |
                (red   & 0xff) << 16 |
                (green & 0xff) <<  8 |
                (blue  & 0xff);
        int colorBackup = getColorARGB();
        if (colorBackup != newColor) {
            this.color = newColor;
            colorChanged();
        }
        return colorBackup;
    }

    // Вернуть и установить цвет
    // Цвет кодируется в формате RGB
    public int getColor() {
        //todo: а что с alpha каналом? может его установить в 0xff ?
//        return (red   & 0xff << 16 |
//                green & 0xff <<  8 |
//                blue  & 0xff);
        return color & 0x00ffffff;
    }

    public int setColor(int color) {
        int colorBackup = getColor();
        if (colorBackup != color) {
//            red   = (byte)(color >>> 16);
//            green = (byte)(color >>> 8);
//            blue  = (byte)(color);
            this.color = this.color & 0xff000000 | color & 0x00ffffff;
            colorChanged();
        }
        return colorBackup;
    }

    // Вернуть и установить уровень прозрачности
    public byte getTransparency() {
        return (byte)(color >>> 24);
    }

    public byte setTransparency(byte alpha) {
        byte alphaBackup = getTransparency();
        if (alphaBackup != alpha) {
            this.color = (alpha & 0xff << 24) | this.color & 0x00ffffff;
            colorChanged();
        }
        return alphaBackup;
    }

    // Вернуть и установить фоновый цвет.
    // Цвет кодируется в формате ARGB
    public int getBackgroundColorARGB() {

        return backgroundColor;
    }

    public int setBackgroundColorARGB(int color) {
        int colorBackup = getBackgroundColorARGB();
        if (colorBackup != color) {
            this.backgroundColor = color;
            backgroundColorChanged();
        }
        return colorBackup;
    }

    // applyColor
    private void colorChanged() {
        int alpha = (color >>> 24) & 0xff;
        gl10.glColor4f(
                ((color >>> 16) & 0xff) / 255f,  // red
                ((color >>> 8) & 0xff) / 255f,  // green
                ((color) & 0xff) / 255f,  // blue
                alpha / 255f); // alpha
//        gl10.glColor4ub(
//                (byte) (color >>> 16), // red
//                (byte) (color >>>  8), // green
//                (byte) (color       ), // blue
//                alpha);               // alpha
//        gl10.glColor4ub(red, green, blue, alpha);
        checkGLError("glColor4f");
        if (alpha == 255 & !lineSmoothEnabled) {
            gl10.glDisable(GL10.GL_BLEND); checkGLError("glDisable");
        } else {
            gl10.glEnable(GL10.GL_BLEND);  checkGLError("glEnable");
        }
    }

    // applyBackgroundColor
    private void backgroundColorChanged() {
        gl10.glClearColor(
                ((backgroundColor >>> 16) & 0xff) / 255f,  // red
                ((backgroundColor >>>  8) & 0xff) / 255f,  // green
                ((backgroundColor       ) & 0xff) / 255f,  // blue
                ((backgroundColor >>> 24) & 0xff) / 255f); // alpha
        checkGLError("glClearColor");
//        if (alpha == 255 & !lineSmoothEnabled) {
//            gl10.glDisable(GL10.GL_BLEND);
//        } else {
//            gl10.glEnable(GL10.GL_BLEND);
//        }
    }

    //******************************************************************************************************************
    //**     Шрифты                                                                                                   **
    //******************************************************************************************************************

//    // Вернуть и установить шрифт
//    public PFont getFont() {
//
//    }
//
//    PFont setFont(PFont font);
//
//    //
//    int getAscent(); //todo: удаляем ?
//
//    //
//    int getBaseLine(); //todo: удаляем ?
//
//    int getFontSize(); //todo: удаляем

    //******************************************************************************************************************
    //**     Строки                                                                                                   **
    //******************************************************************************************************************

//    // Нарисовать строку
//    void drawString(String string, int x, int y, int anchor);
//
//    // Нарисовать часть строки
//    void drawSubstring(String string, int position, int length, int x, int y, int anchor);
//
//    // Вернуть вычисленную ширину символа, используя текущий шрифт
//    int getCharWidth(char character);
//
//    // Вернуть вычисленную ширину набора символов, используя текущий шрифт
//    int getStringWidth(ArrayChar characters, int position, int length);
//
//    // Вернуть вычисленную ширину строки, используя текущий шрифт
//    int getStringWidth(String string);
//
//    // Вернуть вычисленную ширину подстроки, используя текущий шрифт
//    int getStringWidth(String string, int position, int length);

    //******************************************************************************************************************
    //**                                                                                                              **
    //******************************************************************************************************************

//    // Установить область отсечения
//    void clipRect(int x, int y, int w, int h);
//    void clipRect(float x, float y, float w, float h);
//    void clipRegion(Region region);
//
//    // Сбросить область отсечения
//    void resetClip();
//
//    Region getClipBounds(); //todo: Использовать класс Rect

    //******************************************************************************************************************
    //**                                                                                                              **
    //******************************************************************************************************************

//    public native void translate(float v, float v1);
//
//    public native void scale(float v, float v1);
//
//    public final void scale(float sx, float sy, float px, float py) { /* compiled code */ }
//
//    public native void rotate(float v);
//
//    public final void rotate(float degrees, float px, float py) { /* compiled code */ }

    //******************************************************************************************************************
    //**                                                                                                              **
    //******************************************************************************************************************

//    void translate(int x, int y);

    //******************************************************************************************************************
    //**                                                                                                              **
    //******************************************************************************************************************

//    // Вернуть и установить масштабирование
//    int getScale();
//
//    void setScale(int scale);
//    void setScale(int scaleX, int scaleY);
//
//    void dropScale();
//
//    void pushState();
//
//    void popState();

    //******************************************************************************************************************
    //**                                                                                                              **
    //******************************************************************************************************************

    public boolean isOpenGL() {
        return true;
    }

    //******************************************************************************************************************
    //**                                                                                                              **
    //******************************************************************************************************************

    public String getVendorString() {
        String value = gl10.glGetString(gl10.GL_VENDOR);
        checkGLError("glGetString");
        return value;
    }

    public String getRendererString() {
        String value = gl10.glGetString(gl10.GL_RENDERER);
        checkGLError("glGetString");
        return value;
    }

    public String getVersionString() {
        String value = gl10.glGetString(gl10.GL_VERSION);
        checkGLError("glGetString");
        return value;
    }

    //******************************************************************************************************************
    //**                                                                                                              **
    //******************************************************************************************************************

    public boolean isShaderSupported() {
        return false;
    }

//    // перенести в класс Display
//    public boolean isAntiAliasSupported() {
//
//    }
//
//    public boolean isAntiAlias() {
//
//    }
//
//    public void setAntiAlias(boolean enabled) {
//
//    }


    /**
Examples:

Vendor = Android
Renderer = Android PixelFlinger 1.4
OpenGL version = OpenGL ES-CM 1.0
Maximum texture size = 4096
 */

    //******************************************************************************************************************
    //**                                                                                                              **
    //******************************************************************************************************************

    public int getOGLIntValue1(int pname) {
        int[] params = new int[1];
        gl10.glGetIntegerv(pname, params, 0);
        checkGLError("glGetIntegerv");
        return params[0];
    }

    public int getOGLIntValue2(int pname, int pindex) {
        int[] params = new int[2];
        gl10.glGetIntegerv(pname, params, 0);
        checkGLError("glGetIntegerv");
        return params[pindex];
    }

    public int getOGLIntValue4(int pname, int pindex) {
        int[] params = new int[4];
        gl10.glGetIntegerv(pname, params, 0);
        checkGLError("glGetIntegerv");
        return params[pindex];
    }

    public float getOGLFloatValue1(int pname) {
        float[] params = new float[1];
        gl11.glGetFloatv(pname, params, 0);
        checkGLError("glGetFloatv");
        return params[0];
    }

    public float getOGLFloatValue2(int pname, int pindex) {
        float[] params = new float[2];
        gl11.glGetFloatv(pname, params, 0);
        checkGLError("glGetFloatv");
        return params[pindex];
    }

    public float getOGLFloatValue4(int pname, int pindex) {
        float[] params = new float[4];
        gl11.glGetFloatv(pname, params, 0);
        checkGLError("glGetFloatv");
        return params[pindex];
    }

    //******************************************************************************************************************
    //**                                                                                                              **
    //******************************************************************************************************************

    private void checkGLError(String caller) {
        int error = gl10.glGetError();
        if (error == GL10.GL_NO_ERROR) {
            return;
        }

        StringBuilder buf = new StringBuilder("glGetError() returned the following error codes after a call to '" + caller + "': ");

        String errorString = GLU.gluErrorString(error);
        if ( errorString == null ) {
            buf.append("unknown error");
        } else {
            buf.append(errorString);
        }
//        switch (error) {
//            case GL10.GL_INVALID_ENUM:      buf.append("GL_INVALID_ENUM = ");      break;
//            case GL10.GL_INVALID_VALUE:     buf.append("GL_INVALID_VALUE = ");     break;
//            case GL10.GL_INVALID_OPERATION: buf.append("GL_INVALID_OPERATION = "); break;
//            case GL10.GL_STACK_OVERFLOW:    buf.append("GL_STACK_OVERFLOW = ");    break;
//            case GL10.GL_STACK_UNDERFLOW:   buf.append("GL_STACK_UNDERFLOW = ");   break;
//            case GL10.GL_OUT_OF_MEMORY:     buf.append("GL_OUT_OF_MEMORY = ");     break;
//            default:                        buf.append("Unknown glGetError() return value: ");
//        }
        buf.append(" (code: " + error + ", 0x" + Integer.toHexString(error) + ")");

        throw new GLException(buf.toString());
    }

    // android.opengl.GLException
    public static class GLException extends RuntimeException {

        public GLException(String string) {
            super(string);
        }

    }

}
