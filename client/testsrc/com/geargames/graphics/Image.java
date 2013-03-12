package com.geargames.graphics;

import com.geargames.common.resource.Resource;

/**
 * User: abarakov
 * Date: 07.03.13
 */
public abstract class Image extends Resource {

//    Image getRescaledImage(int w, int h) throws IOException;
//
//    Image getSubImage(int x, int y, int w, int h);
//
//    Graphics getGraphics();

    public abstract int getWidth();

    public abstract int getHeight();

//    void resizeCanvas(int w, int h);
//
//    void recycle();
//
//    boolean isCreated();

}
