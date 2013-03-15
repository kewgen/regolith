package com.geargames.graphics.opengl;

//import com.geargames.platform.Manager;
//import com.geargames.common.Port;
//import com.geargames.common.Render;
//import com.geargames.common.String;
//import com.geargames.common.packer.PAffine;
//import com.geargames.common.packer.PFont;
//import com.geargames.common.packer.PFrame;
//import com.geargames.common.packer.PSprite;
//import com.geargames.common.util.ArrayChar;
//
//import java.awt.*;
//import java.awt.Image;
//import java.awt.geom.AffineTransform;
//import java.io.IOException;

/**
 * Порт-wrapper класса Graphics для microedition
 */
public class Graphics /*implements com.geargames.regolith.graphics.Graphics */{

//    public Graphics(java.awt.Graphics g) {
//        graphics = g;
//    }
//
//    public void onCache(int len) {//включить кеширование картинок
//        if (image_cash == null) image_cash = new Image[len];
//    }
//
//    public void addTexture(com.geargames.common.Image image) {}
//
//    public void drawRegion(com.geargames.common.Image image, int src_x, int src_y, int w, int h, int dst_x, int dst_y, PAffine affine) {
//        if (image_cash == null) return;
//        int frame_id = ((com.geargames.regolith.graphics.opengl.Image) image).frame_id;
//        if (image_cash[frame_id] == null) {//bufering
//            try {
//                com.geargames.regolith.graphics.opengl.Image img_ = com.geargames.regolith.graphics.opengl.Image.createImage(w, h);
//                Graphics graphics_ = img_.getGraphics();
//                graphics_.drawImage(image, 0 - src_x, 0 - src_y, 0);
//                image_cash[frame_id] = img_.getImage();
//            } catch (IOException e) {
//                Debug.logEx(e);
//            }
//        }
//
//        //отрисовка без отражения
//        if (affine == null) {
//            graphics.drawImage(image_cash[frame_id], dst_x, dst_y, null);
//            return;
//        }
//
//        //отрисовка с аффинными преобразованиями
//        Graphics2D graphics2D = (Graphics2D) graphics;
//        AffineTransform transformOriginal = graphics2D.getTransform();
//        AffineTransform transformNew = (AffineTransform) (transformOriginal.clone());
//        int dx = 0;
//        int dy = 0;
//        Image awtImage = image_cash[frame_id];
//        Composite compositeOriginal = graphics2D.getComposite();//сохраняем прозрачность
//        if (affine.getTransparency() > 0) {
//            float alpha = 1 - ((float) affine.getTransparency() / 100);
//            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
//        }
//        if (affine.getRotate() != 0) {
//            transformNew.rotate((float) affine.getRotate() * Math.PI / 180.0, dst_x - affine.getX(), dst_y - affine.getY());
//        }
//        if (affine.getScalingX() != 100 || affine.getScalingY() != 100) {
//            int smooth = Image.SCALE_SMOOTH;
//            if (w + h > 350) smooth = Image.SCALE_FAST;
//            awtImage = awtImage.getScaledInstance(w * affine.getScalingX() / 100, h * affine.getScalingY() / 100, smooth);
//        }
//        if (affine.isHmirror()) {
//            transformNew.scale(-1, 1);
//            dx = (dst_x << 1) + w;
//        }
//        if (affine.isVmirror()) {
//            transformNew.scale(1, -1);
//            dy = (dst_y << 1) + h;
//        }
//        graphics2D.setTransform(transformNew);
//        graphics2D.drawImage(awtImage, dst_x - dx, dst_y - dy, null);
//        graphics2D.setTransform(transformOriginal);
//        if (affine.getTransparency() > 0) {
//            //graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
//            graphics2D.setComposite(compositeOriginal);
//        }
//
//
//        //Без буферизации
////        graphics.setClip(dst_x, dst_y, w, h);
////        graphics.drawImage(image.getImage(), dst_x - src_x, dst_y - src_y, null);
////        graphics.setClip(0, 0, Port.getW(), Port.getH());
//        //graphics.drawRegion(image.getImage(), src_x, src_y, w, h, mirror, dst_x, dst_y, anchor);
//    }
//
//    public void drawFrame(PFrame frame, int dst_x, int dst_y) {
//        if (image_cash == null) return;
//        int frame_id = frame.getBid();
//
//        if (image_cash[frame_id] == null) {//buffering
//            try {
//                com.geargames.regolith.graphics.opengl.Image img_ = com.geargames.regolith.graphics.opengl.Image.createImage(frame.getWidth(), frame.getHeight());
//                Graphics graphics_ = img_.getGraphics();
//                graphics_.drawImage(frame.getImage(), 0 - frame.getSrcX(), 0 - frame.getSrcY(), 0);
//                image_cash[frame_id] = img_.getImage();
//            } catch (IOException e) {
//                Debug.logEx(e);
//            }
//        }
//
//
//        PAffine affine = frame.getAffine();
//        //отрисовка без отражения
//        if (affine == null) {
//            if (scale == 100 && (dst_x + frame.getWidth() < 0 || dst_y + frame.getHeight() < 0 || dst_x > Port.getScaledW() || dst_y > Port.getScaledH()))
//                return;
//            graphics.drawImage(image_cash[frame_id], dst_x, dst_y, null);
//            return;
//        }
//
//        //отрисовка с аффинными преобразованиями
//        Graphics2D graphics2D = (Graphics2D) graphics;
//        AffineTransform transformOriginal = graphics2D.getTransform();
//        AffineTransform transformNew = (AffineTransform) (transformOriginal.clone());
//        int dx = 0;
//        int dy = 0;
//        Image awtImage = image_cash[frame_id];
//        Composite compositeOriginal = graphics2D.getComposite();//сохраняем прозрачность
//        if (affine.getTransparency() > 0) {
//            float alpha = 1 - ((float) affine.getTransparency() / 100);
//            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
//        }
//        if (affine.getRotate() != 0) {
//            transformNew.rotate((float) affine.getRotate() * Math.PI / 180.0, dst_x - affine.getX(), dst_y - affine.getY());
//        }
//        if (affine.getScalingX() != 100 || affine.getScalingY() != 100) {
//            int smooth = Image.SCALE_SMOOTH;
//            if (frame.getWidth() + frame.getHeight() > 350) smooth = Image.SCALE_FAST;
//            awtImage = awtImage.getScaledInstance((frame.getWidth() * affine.getScalingX()) / 100, (frame.getHeight() * affine.getScalingY()) / 100, smooth);
//        }
//        if (affine.isHmirror()) {
//            transformNew.scale(-1, 1);
//            dx = (dst_x << 1) + frame.getWidth();
//        }
//        if (affine.isVmirror()) {
//            transformNew.scale(1, -1);
//            dy = (dst_y << 1) + frame.getHeight();
//        }
//        graphics2D.setTransform(transformNew);
//        graphics2D.drawImage(awtImage, dst_x - dx, dst_y - dy, null);
//        graphics2D.setTransform(transformOriginal);
//        if (affine.getTransparency() > 0) {
//            graphics2D.setComposite(compositeOriginal);
//        }
//    }
//
//    public void drawImage(com.geargames.common.Image image, int x, int y, int anchor) {
//        if (anchor == (HCENTER | VCENTER)) {
//            int w = image.getWidth();
//            int h = image.getHeight();
//            x -= w / 2;
//            y -= h / 2;
//        }
//        graphics.drawImage(((com.geargames.regolith.graphics.opengl.Image) image).getImage(), x, y, null);
//    }
//
//    public void drawLine(int x1, int y1, int x2, int y2) {
//        graphics.drawLine(x1, y1, x2, y2);
//    }
//
//    public void drawRect(int x, int y, int w, int h) {
//        graphics.drawRect(x, y, w, h);
//    }
//
//    public void fillRect(int x, int y, int w, int h) {
//        graphics.fillRect(x, y, w, h);
//    }
//
//    public void clipRect(int x, int y, int w, int h) {
//        graphics.clipRect(x, y, w, h);
//    }
//
//    public void setClip(Object object) {
//        graphics.setClip(null);
//    }
//
//    public void setClip(int x, int y, int w, int h) {
//        graphics.setClip(x, y, w, h);
//    }
//
//    public void resetClip() {
//        graphics.setClip(0, 0, Port.getW(), Port.getH());
//    }
//
//    public void setColor(int color) {
//        graphics.setColor(new Color(color));
//    }
//
//
//    public PFont getFont() {
//        return font;
//    }
//
//    public void setFont(PFont font) {
//        this.font = font;
//    }
//
//    public int getAscent() {
//        if (font != null) {
//            return font.getAscent();
//        } else {
//            return graphics.getFontMetrics().getAscent();
//        }
//    }
//
//    public int getBaseLine() {
//        if (font != null) {
//            return font.getBaseLine();
//        } else {
//            return getFontSize() - getAscent();
//        }
//    }
//
//    public int getFontSize() {
//        if (font != null) {
//            return font.getSize();
//        } else {
//            return graphics.getFontMetrics().getHeight();
//        }
//    }
//
//    public int getWidth(char character) {
//        if (font != null) {
//            return font.getWidth(character);
//        } else {
//            return graphics.getFontMetrics().charWidth(character);
//        }
//    }
//
//    public int getWidth(ArrayChar characters, int position, int length) {
//        if (font != null) {
//            return font.getWidth(characters, position, length);
//        } else {
//            return graphics.getFontMetrics().charsWidth(characters.getArray(), position, length);
//        }
//    }
//
//    public int getWidth(String string) {
//        if (font != null) {
//            return font.getWidth(string);
//        } else {
//            return graphics.getFontMetrics().stringWidth(string.toString());
//        }
//    }
//
//    public int getWidth(String string, int position, int length) {
//        if (font != null) {
//            return font.getWidth(string, position, length);
//        } else {
//            return graphics.getFontMetrics().stringWidth(string.toString().substring(position, position + length));
//        }
//    }
//
//    public Render getRender() {
//        return render;
//    }
//
//    public int getTransparency() {
//        return transparency;
//    }
//
//    public void setTransparency(int transparency) {//0..100, 100 - полная прозрачность
//        this.transparency = transparency;
//        float alpha = 1 - ((float) transparency / 100);
//        ((Graphics2D) graphics).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
//    }
//
//    public int getScale() {
//        return scale;
//    }
//
//    public void setScale(int scale) {//масштабирования 0..200, 100 - оригинал
//        this.scale = scale;
//        double alpha = (double) scale / 100;
//        if (affineTransformOriginal == null) affineTransformOriginal = ((Graphics2D) graphics).getTransform();
//        ((Graphics2D) graphics).scale(alpha, alpha);
//    }
//
//    public void dropScale() {
//        scale = 100;
//        ((Graphics2D) graphics).setTransform(affineTransformOriginal);
//    }
//
//    public void loadTexture(com.geargames.common.Image image) {
//    }//затычка андроид OpenGL
//
//    public com.geargames.common.Image createImage(byte[] array, int i, int data_len) throws IOException {
//        return com.geargames.regolith.graphics.opengl.Image.createImage(array, i, data_len);
//    }
//
//    public com.geargames.common.Image createImage() {
//        return new com.geargames.regolith.graphics.opengl.Image(null);
//    }
//
//
//    public Object getGLGraphics() {
//        return null;
//    }//затычка андроид OpenGL. Ожидание инициализации графикса
//
//    public Graphics(Image image) {
//        graphics = image.getGraphics();
//    }
//
//    public void renderSplash(com.geargames.regolith.graphics.opengl.Image image, short elEnd) {//отрисовка сплеша на андроиде
//    }
//
//    public void setManager(Manager manager) {
//
//    }
//
//    private static Image[] image_cash;
//    private java.awt.Graphics graphics;
//
//    private int transparency; //прозрачность при рендере на графикс
//    private int scale;        //масштабирование при рендере на графикс
//    private AffineTransform affineTransformOriginal;
//    private PFont font;
//
//    private Render render;
//
//    public void setRender(Render render) {
//        this.render = render;
//    }
//
//    /**
//     * Нарисовать строку string в координатах (x, y) с якорем anchor.
//     *
//     * @param string
//     * @param x
//     * @param y
//     * @param anchor
//     */
//    public void drawString(String string, int x, int y, int anchor) {
//        if (string == null) {
//            return;
//        }
//        if (font != null) {
//            drawCustomString(string, x, y, anchor);
//        } else {
//            drawSystemString(string, x, y, anchor);
//        }
//    }
//
//    /**
//     * Нарисовать часть строки string в координатах (x, y) с якорем anchor.
//     *
//     * @param string    исходная строка
//     * @param position  индекс первого символа отрисовываемой подстроки
//     * @param length    индекс последнего символа + 1 отрисовываемой подстроки
//     * @param x
//     * @param y
//     * @param anchor
//     */
//    public void drawSubstring(String string, int position, int length, int x, int y, int anchor) {
//        if (string == null) {
//            return;
//        }
//        // TrimRight():
//        int last = position + length - 1;
//        while (last >= position && string.charAt(last) <= String.SPACE) {
//            last--;
//        }
//        length = last - position + 1;
//
//        if (font != null) {
//            drawRasterSubstring(string, position, length, x, y, anchor);
//        } else {
//            String substring = string.substring(position, length + position);
//            drawSystemString(substring, x, y, anchor);
//        }
//    }
//
//    /**
//     * Нарисовать строку string системным шрифтом (векторным).
//     *
//     * @param string
//     * @param x
//     * @param y
//     * @param anchor
//     */
//    public void drawSystemString(String string, int x, int y, int anchor) {
//        switch (anchor) {
//            case Graphics.HCENTER:
//                x -= getWidth(string) / 2;
//                break;
//            case Graphics.RIGHT:
//                x -= getWidth(string);
//                break;
//        }
//        graphics.drawString(string.toString(), x, y);
//    }
//
//    /**
//     * Нарисовать строку string растровым шрифтом.
//     *
//     * @param string
//     * @param x
//     * @param y
//     * @param anchor
//     */
//    public void drawCustomString(String string, int x, int y, int anchor) {
//        drawRasterSubstring(string, 0, string.length(), x, y, anchor);
//    }
//
//    /**
//     * Нарисовать строку string растровым шрифтом.
//     *
//     * @param string    исходная строка
//     * @param position  индекс первого символа отрисовываемой подстроки
//     * @param length    количество символов в отрисовываемой подстроке
//     * @param x
//     * @param y
//     * @param anchor
//     */
//    public void drawRasterSubstring(String string, int position, int length, int x, int y, int anchor) {
//        switch (anchor) {
//            case com.geargames.common.Graphics.HCENTER:
//                x -= getWidth(string, position, length) / 2;
//                break;
//            case com.geargames.common.Graphics.RIGHT:
//                x -= getWidth(string, position, length);
//                break;
//        }
//        length += position;
//        for (int i = position; i < length; i++) {
//            char character = string.charAt(i);
//            PSprite sprite = font.getSprite(character);
//            sprite.draw(this, x, y);
//            x += font.getWidth(character);
//        }
//    }
//
//    public int getFrameW(int o_i_id) {
//        return render.getFrameW(o_i_id);
//    }
//
//
//    public void setGLRenderer(Object glRenderer) {
//
//    }
}
