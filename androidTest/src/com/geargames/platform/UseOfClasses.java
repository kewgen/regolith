package com.geargames.platform;

import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import com.example.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * User: abarakov
 * Date: 16.03.13
 */
public class UseOfClasses {

    // ----- Класс Application -----------------------------------------------------------------------------------------

    public class GGApplication extends android.app.Application {

    }

    // ----- Класс окна приложения (форма) -----------------------------------------------------------------------------

    public class GGActivity extends android.app.Activity
    {
        private GGSurfaceView glView;

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            glView = new GGSurfaceView(this);
            glView.setRenderer(new GGRenderer());
            setContentView(glView);
        }

    }

    // ----- Класс области вывода (контекст рендеринга) --------------------------------------------------------------

    public class GGSurfaceView extends GLSurfaceView {

        public GGSurfaceView(android.content.Context context) {
            super(context);
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.surfaceChanged(holder, format, width, height);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder arg0) {
            super.surfaceDestroyed(arg0);
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {//метод, который и будет обрабатывать MotionEvent'ы.
            return false;
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            return false;
        }

    }

    // ----- Класс рендера (графического контекста) --------------------------------------------------------------------

    public class GGRenderer implements android.opengl.GLSurfaceView.Renderer {

        public GGRenderer() {

        }

        //
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            gl.glViewport(0, 0, width, height);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
        }

    }

}