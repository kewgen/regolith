package com.geargames.platform;

import android.opengl.GLES20;
import android.opengl.GLU;
import android.os.Debug;

import java.nio.*;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class GGRenderer implements android.opengl.GLSurfaceView.Renderer {

	FloatBuffer triangleBuffer;
    AndroidGraphics graphics;
  
    public GGRenderer() {
        float [] triangle = {
                0, 0, 0,  // A
                1, 0, 0,  // B
                0, 1, 0   // C
        };

        ByteBuffer bb = ByteBuffer.allocateDirect(36);
        bb.order(ByteOrder.nativeOrder());
        triangleBuffer = bb.asFloatBuffer();
        triangleBuffer.put(triangle);
        triangleBuffer.position(0);
    }
  
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        System.out.println("onSurfaceCreated");

        graphics = new AndroidGraphics(gl);

        System.out.println("Vendor = "               + graphics.getVendorString());
        System.out.println("Renderer = "             + graphics.getRendererString());
        System.out.println("OpenGL version = "       + graphics.getVersionString());
        System.out.println("Maximum texture size = " + graphics.getMaxTextureSize());

        graphics.setBackgroundColorARGB(0xffffffff); // белый
//        graphics.setBackgroundColorARGB(0xff6cb6ff); // голубоватый цвет фона
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
        System.out.println("onSurfaceChanged");
//        if (graphics.getGl() != gl) {
//            throw new Exception("");
//        }
        graphics.setViewport(0, 0, width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
//        System.out.println("onDrawFrame");
//        if (graphics.getGl() != gl) {
//            throw new Exception("");
//        }
        graphics.clearBuffers();

//        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glColor4f(1f, 0f, 0f, 1f);
//        gl.glDisable(GL10.GL_BLEND);

//        graphics.setColorARGB((byte) 0xe9, (byte) 0xb5, (byte) 0xa7, (byte) 0xd4);

//        graphics.setTransparency((byte) 0xfe);
        graphics.setColorARGB(0xffff0000); // красный непрозрачный цвет
//        gl.glEnable(GL10.GL_BLEND);
        graphics.drawRect((short)10, (short)10, (short)300, (short)200);

        graphics.drawLine((short)0, (short)100, (short)400, (short)100);

//        graphics.setColor(0x00ff00); // зеленый цвет
//        graphics.drawRect(-0.8f, -0.8f, 1.6f, 1.6f);
//
//        graphics.setColor(0x0000ff); // синий цвет
//        graphics.drawRect(-0.7f, -0.7f, 1.4f, 1.4f);

//        graphics.setColor(0x007f00); // темно-зеленый цвет
//        for (int i = 0; i < 10; i++) {
//            graphics.drawRect(n-0.2f * i, -0.2f * i, 0.4f * i, 0.4f * i);
//        }

        graphics.drawLine((short)10, (short) 10, (short)310, (short)210);
        graphics.drawLine((short)10, (short)210, (short)310, (short) 10);

//        graphics.setTransparency((byte) 0x7f);
//        graphics.setColorARGB(0x7f00ff00); // зеленый полупрозрачный цвет
//        gl.glEnable(GL10.GL_BLEND);
//        graphics.fillRect(50, 20, 350, 180);

        gl.glEnable(GL10.GL_BLEND);
        graphics.fillRect((short)50, (short)20, (short)350, (short)180, new int[]{
                0xafff0000,
                0x9f00ff00,
                0x8f0000ff,
                0x7fffffff
        });


        short y = 0;
        for (int i = 0; i <= 255; i += 5, y++) {
            // Красная полоска
            graphics.setColorARGB((byte)255, (byte)i, (byte)i, (byte)255);
//            gl.glColor4f(1f, i * 0.01f, i * 0.01f, 1f);
            graphics.drawLine((short)20, (short)(40+y), (short)80, (short)(40+y));

            // Зеленая полоска
            graphics.setColorARGB((byte)i, (byte)255, (byte)i, (byte)255);
//            gl.glColor4f(i * 0.01f, 1f, i * 0.01f, 1f);
            graphics.drawLine((short)80, (short)(40+y), (short)140, (short)(40+y));

            // Синяя полоска
            graphics.setColorARGB((byte)i, (byte)i, (byte)255, (byte)255);
//            gl.glColor4f(i * 0.01f, i * 0.01f, 1f, 1f);
            graphics.drawLine((short)140, (short)(40+y), (short)200, (short)(40+y));

            // Серая полупрозрачная полоска
            graphics.setColorARGB((byte)127, (byte)127, (byte)127, (byte)i);
//            gl.glColor4f(0.5f, 0.5f, 0.5f, i * 0.01f);
            graphics.drawLine((short)200, (short)(40+y), (short)260, (short)(40+y));
        }

//        gl.glMatrixMode(GL10.GL_MODELVIEW);
//        gl.glLoadIdentity();
////        gl.glTranslatef(0, 0, -3.0f);
//
//        gl.glDisable(GL10.GL_BLEND);
//        gl.glColor4x(0x10000, 0, 0, 0x10000);
//        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//
//        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, triangleBuffer);
//        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
    }
	
//	@Override
//	public int[] getConfigSpec() {
//	    int[] configSpec = {
//	            EGL10.EGL_NONE
//	    };
//	    return configSpec;
//	}

}
