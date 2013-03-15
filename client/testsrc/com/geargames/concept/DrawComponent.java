package com.geargames.concept;

import com.geargames.common.util.Region;

import java.awt.image.BufferedImage;

/**
 * @author  abarakov
 * created: 12.03.2013
 */
public class DrawComponent {

    public abstract class GraphicsAttribute {

        public static final int TRANSFORM_STATE_ATTRIBUTE = 1;
        public static final int COLOR_STATE_ATTRIBUTE     = 2;

    }

    public interface Canvas {

        void create(int width, int height) throws Exception;

        void resize(int width, int height);

    }

    public class BitmapCanvas implements Canvas {

        private java.awt.Image buffer;

        public void create(int width, int height) throws Exception {
            if (width < 0) {
                throw new Exception("");
            }
            if (height < 0) {
                throw new Exception("");
            }
            if (width == 0 || height == 0) {
                buffer = null;
                return;
            }
            buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        public void resize(int width, int height) {
//            buffer.
        }

    }

    public class Graphics {

        private Canvas canvas;

        public void Graphics(Canvas canvas) {
            this.canvas = canvas;
        }

        public Canvas getCanvas() {
            return canvas;
        }

        public void drawRect(int x, int y, int width, int height) {

        }

        public void drawRegion(Region region) {
            drawRect(region.getMinX(), region.getMinY(), region.getWidth(), region.getHeight());
        }

        public void pushAttribute() {

        }

        public void popAttribute() {

        }

        public void translate(int x, int y) {

        }

        public void rotate(short angle) {

        }

        // scale
        public void scaling(byte scale) {

        }

        public void setColor(int color) {

        }

    }

    public interface Drawable {

        void draw(Graphics graphics);
    }

    public class DrawableComponent implements Drawable {
        int x;       //
        int y;       //
        int width;
        int height;
        byte scale;  // масштаб        0..100%
        short angle; // угол поворота  0..359°
        int color;   // цвет           0xAARRGGBB

        public int configureGraphics(Graphics graphics) {
            graphics.pushAttribute();

            int res = GraphicsAttribute.TRANSFORM_STATE_ATTRIBUTE;
            graphics.translate(x, y);
            graphics.rotate(angle);
            graphics.scaling(scale); // scale

            res |= GraphicsAttribute.COLOR_STATE_ATTRIBUTE;
            graphics.setColor(color);

            return res;
        }

        public void draw(Graphics graphics) {
            graphics.drawRect(0, 0, width, height);
        }
    }

}