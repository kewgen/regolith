package com.geargames.graphics;

//import com.geargames.common.packer.PAffine;
import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PFrame;
import com.geargames.common.util.ArrayChar;
import com.geargames.common.util.Region;

import java.io.IOException;

/**
 * User: kewgen, abarakov
 * Date: 10.09.12, 07.03.13
 */
//todo: Переименовать в Canvas?
public interface Graphics {
    //todo: перетащить константы в TextLayout
    int HCENTER  = 1 << 0;
    int VCENTER  = 1 << 1;
    int LEFT     = 1 << 2;
    int RIGHT    = 1 << 3;
    int TOP      = 1 << 4;
    int BOTTOM   = 1 << 5;
    int BASELINE = 6;
    int SOLID    = 7;
    int DOTTED   = 8;

    // Нарисовать изображение в соответствующих координатах и с заданным выравниванием
    void drawImage(Image image, int x, int y);
    void drawImage(Image image, float x, float y);

    // Нарисовать часть изображения image ограниченную прямоугольником (srcX1, srcY1), (srcX2, srcY2) в пределах
    // целевого прямоугольника (destX1, destY1), (destX2, destY2)
    boolean drawImage(Image image,
                      int srcX1,  int srcY1,  int srcX2,  int srcY2,
                      int destX1, int destY1, int destX2, int destY2);
    boolean drawImage(Image image,
                      float srcX1,  float srcY1,  float srcX2,  float srcY2,
                      float destX1, float destY1, float destX2, float destY2);
//    boolean drawImage(Image image, Rect source, Rect dest);
    boolean drawImage(Image image, Region source, Region dest);

    // Нарисовать часть изображения
    // drawRegion
    void drawSubimage(Image image, int srcX, int srcY, int width, int height, int dstX, int dstY/*, PAffine affine*/);
    void drawSubimage(Image image, float srcX, float srcY, float width, float height, float dstX, float dstY);

    // Нарисовать фрейм, часть изображения в заданных координатах
    public void drawFrame(PFrame frame, int x, int y);
    public void drawFrame(PFrame frame, float x, float y);

    //todo: Добавить возможность рисования изображений или их частей с заполнением, т.е. с дублированием изображения

    // Нарисовать точку в указанных координатах
    void drawPoint(int x, int y);
    void drawPoint(float x, float y);

    /**
     * Нарисовать множество точек в указанных координатах
     * @param points массив координат точек, где каждая точка представлена двумя значениями в массиве, следующими друг за
     *               другом. Очевидно, что массив должен иметь четную длинну.
     */
    void drawPoints(int[] points);
    void drawPoints(float[] points);

    // Нарисовать линию от точки (x1, y1) до (x2, y2)
    void drawLine(int x1, int y1, int x2, int y2);
    void drawLine(float x1, float y1, float x2, float y2);

    /**
     * Нарисовать ломанную линию
     * @param points массив координат точек, где каждая точка представлена двумя значениями в массиве, следующими друг за
     *               другом. Каждая линия рисуется от точки i до точки i+1. Ломанная линия нигде не прерывается.
     */
    void drawLines(int[] points);
    void drawLines(float[] points);

    // Нарисовать прямоугольную рамку без заполнения этого прямоугольника.
    void drawRect(int x, int y, int w, int h);
    void drawRect(float x, float y, float w, float h);
//    void drawRect(Rect rect);
    void drawRect(Region region);

//    void drawCircle(float cx, float cy, float radius, android.graphics.Paint paint);
//
//    void drawArc(android.graphics.RectF oval, float startAngle, float sweepAngle, boolean useCenter);
//
//    void drawRoundRect(android.graphics.RectF rect, float rx, float ry);


    // Нарисовать заполненную прямоугольную область
    void fillRect(int x, int y, int w, int h);
    void fillRect(float x, float y, float w, float h);
//    void fillRect(Rect rect);
    void fillRect(Region region);

    // Нарисовать строку
    void drawString(String string, int x, int y, int anchor);

    // Нарисовать часть строки
    void drawSubstring(String string, int position, int length, int x, int y, int anchor);

    // Вернуть и установить цвет
    int getColor();

    int setColor(int color);

    // Вернуть и установить шрифт
    PFont getFont();

    PFont setFont(PFont font);

    //
    int getAscent(); //todo: удаляем ?

    //
    int getBaseLine(); //todo: удаляем ?

    int getFontSize(); //todo: удаляем

    // Вернуть вычисленную ширину символа, используя текущий шрифт
    int getCharWidth(char character);

    // Вернуть вычисленную ширину набора символов, используя текущий шрифт
    int getStringWidth(ArrayChar characters, int position, int length);

    // Вернуть вычисленную ширину строки, используя текущий шрифт
    int getStringWidth(String string);

    // Вернуть вычисленную ширину подстроки, используя текущий шрифт
    int getStringWidth(String string, int position, int length);

//    Render getRender(); //todo: это здесь должно быть?

    // Вернуть и установить уровень прозрачности
    int getTransparency();

    int setTransparency(int transparency);

    // Установить область отсечения
    void clipRect(int x, int y, int w, int h);
    void clipRect(float x, float y, float w, float h);
    void clipRegion(Region region);

    // Сбросить область отсечения
    void resetClip();

    Region getClipBounds(); //todo: Использовать класс Rect

//    public native void translate(float v, float v1);
//
//    public native void scale(float v, float v1);
//
//    public final void scale(float sx, float sy, float px, float py) { /* compiled code */ }
//
//    public native void rotate(float v);
//
//    public final void rotate(float degrees, float px, float py) { /* compiled code */ }

    void translate(int x, int y);

    // Вернуть и установить масштабирование
    int getScale();

    void setScale(int scale);
    void setScale(int scaleX, int scaleY);

    void dropScale();

    void pushState();

    void popState();

//    void onCache(int len);//включить кеширование картинок

    void addTexture(Image image);

    Image createImage(byte[] array, int i, int data_len) throws IOException;

    Image createImage();




    // перенести в класс Display
    boolean isAntiAliasSupported();

    boolean isAntiAlias();

    void setAntiAlias(boolean enabled);



}
