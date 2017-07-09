package edu.cs4730.flappyalien;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


/**
 * Created by Seker on 7/8/2017.
 *
 * This is the code to represent the "pipe".
 */

public class ObjPipe {
    private Context myContext;
    private int width, height;
    private int x, y;   //the bottom, left of the top "pipe"
    private int gap = 100;
    private float scale;
    private Rect toprec, bottomrec;
    private Paint color = new Paint();
    private boolean scored = false;
    ObjPipe() {

    }

    ObjPipe(Context c, int w, int h, int g, float s) {
        myContext = c;
        width = w;
        height = h;
        gap = g;
        scale = s;
        scored = false;
    }

    public void setup(int spot) {
        //fake it for now.
        y = spot;
        x = width;
        scored = false;
        int pipew = Math.round(50 * scale);

        toprec = new Rect(x, 0, x + pipew, y);
        bottomrec = new Rect(x, spot + gap, x + pipew, height);

    }

    public boolean move() {
        toprec.left = toprec.left - 1;
        toprec.right = toprec.right - 1;

        bottomrec.left = bottomrec.left - 1;
        bottomrec.right = bottomrec.right - 1;

        if (toprec.right <= 0) return true;
        return false; //pipe is still on screen.
    }

    public void imsecond() {
        toprec.left = toprec.left + width / 2;
        toprec.right = toprec.right + width / 2;

        bottomrec.left = bottomrec.left + width / 2;
        bottomrec.right = bottomrec.right + width / 2;
    }


    public void draw(Canvas c) {

        c.drawRect(toprec, color);
        c.drawRect(bottomrec, color);
    }

    public boolean collide(Rect alien) {
        return ((Rect.intersects(toprec,alien)) ||
                (Rect.intersects(bottomrec,alien)));
    }

    public void setScored(boolean s) {
        scored = s;
    }

    public boolean getScored() {
        return scored;
    }

    public Rect getRect() {
        return toprec;
    }
}
