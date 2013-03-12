package com.geargames.graphics;

import com.geargames.common.packer.PAffine;
import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PFrame;
import com.geargames.common.util.ArrayChar;

import java.io.IOException;

/**
 * User: kewgen, abarakov
 * Date: 10.09.12, 07.03.13
 */
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
    void drawImage(Image image, int x, int y, int anchor);

    // Нарисовать часть изображения
    // drawRegion
    void drawSubimage(Image image, int src_x, int src_y, int w, int h, int dst_x, int dst_y, PAffine affine);

    // Нарисовать фрейм, часть изображения в заданных координатах
    public void drawFrame(PFrame frame, int x, int y);

    // Нарисовать линию
    void drawLine(int x1, int y1, int x2, int y2);

    // Нарисовать прямоугольную рамку без заполнения этого прямоугольника
    void drawRect(int x, int y, int w, int h);

    // Нарисовать строку
    void drawString(String string, int x, int y, int anchor);

    // Нарисовать часть строки
    void drawSubstring(String string, int position, int length, int x, int y, int anchor);

    // Нарисовать заполненную прямоугольную область
    void fillRect(int x, int y, int w, int h);

    // Установить область отсечения
    void setClip(int x, int y, int w, int h);

    // ???
    void clipRect(int x, int y, int w, int h);

    // Сбросить область отсечения
    void resetClip();

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
    int getWidth(char character);

    // Вернуть вычисленную ширину набора символов, используя текущий шрифт
    int getWidth(ArrayChar characters, int position, int length);

    // Вернуть вычисленную ширину строки, используя текущий шрифт
    int getWidth(String string);

    // Вернуть вычисленную ширину подстроки, используя текущий шрифт
    int getWidth(String string, int position, int length);

//    Render getRender(); //todo: это здесь должно быть?

    // Вернуть и установить уровень прозрачности
    int getTransparency();

    int setTransparency(int transparency);

    // Вернуть и установить масштабирование
    int getScale();

    void setScale(int scale);

    void dropScale();

    void onCache(int len);//включить кеширование картинок

    void addTexture(Image image);

    Image createImage(byte[] array, int i, int data_len) throws IOException;

    Image createImage();
}
