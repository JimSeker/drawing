package edu.cs4730.flappyalien;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


/**
 * Created by Seker on 7/8/2017.
 */

public class ObjPipe {
    Context myContext;
    int width, height;
    int x, y;   //the bottom, left of the top "pipe"
    int gap = 100;
    float scale;
    Rect toprec, bottomrec;
    Paint color = new Paint();

    ObjPipe() {

    }

    ObjPipe(Context c, int w, int h, int g, float s) {
        myContext = c;
        width = w;
        height = h;
        gap = g;
        scale = s;
    }

    public void setup(int spot) {
        //fake it for now.
        y = spot;
        x = width;
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
        if ((toprec.intersect(alien)) ||
                (bottomrec.intersect(alien)))
            return true;
        return false;
    }
}
