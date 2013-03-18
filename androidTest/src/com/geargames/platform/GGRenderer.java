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

        graphics.setTransparency((byte) 0xfe);
        graphics.setColor(0xff0000); // красный цвет
        graphics.drawRect(10, 10, 300, 200);

        graphics.drawLine(0, 100, 400, 100);

//        graphics.setColor(0x00ff00); // зеленый цвет
//        graphics.drawRect(-0.8f, -0.8f, 1.6f, 1.6f);
//
//        graphics.setColor(0x0000ff); // синий цвет
//        graphics.drawRect(-0.7f, -0.7f, 1.4f, 1.4f);

//        graphics.setColor(0x007f00); // темно-зеленый цвет
//        for (int i = 0; i < 10; i++) {
//            graphics.drawRect(n-0.2f * i, -0.2f * i, 0.4f * i, 0.4f * i);
//        }

        graphics.drawLine(10,  10, 310, 210);
        graphics.drawLine(10, 210, 310,  10);

//        graphics.setTransparency((byte) 0x7f);
//        graphics.setColorARGB(0x7f00ff00); // зеленый полупрозрачный цвет
//        gl.glEnable(GL10.GL_BLEND);
//        graphics.fillRect(50, 20, 350, 180);

        gl.glDisable(GL10.GL_BLEND);
        graphics.fillRect(50, 20, 350, 180, new int[]{
                0xffff0000,
                0xff00ff00,
                0xff0000ff,
                0xffffffff
        });

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
