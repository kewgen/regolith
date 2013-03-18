package com.geargames.platform;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import com.example.R;

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
