package edu.cs4730.flappyalien;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by Seker on 7/8/2017.
 */

public class ObjAlien {
    private Bitmap alien1Bmp, alien2Bmp;
    private int x, y;  //alien topleft position
    private int width, height;  //boardsize info.
    private Context myContext;
    private int tick = 0;
    private String TAG = "objAlien";
    private Rect myRect;

    ObjAlien(Context c, int x, int y, int w, int h) {
        myContext = c;
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        alien1Bmp = BitmapFactory.decodeResource(myContext.getResources(), R.mipmap.alien);
        alien2Bmp = BitmapFactory.decodeResource(myContext.getResources(), R.mipmap.alien2);
        Log.wtf(TAG, "Alien height is " + alien1Bmp.getHeight());
        myRect = new Rect(x, y, x + alien1Bmp.getWidth(), y + alien1Bmp.getHeight());

    }

    public int getHeight() {
        return alien1Bmp.getHeight();
    }

    //going up, so decrement y
    public void decr() {
        if (y > 0) {
            y -= 1;
            myRect.top = y;
            myRect.bottom = y + alien1Bmp.getHeight();
        }
    }

    //going down, so incr y
    public void incr() {
        if (y + alien1Bmp.getHeight() < height) {
            y += 2;
            myRect.top = y;
            myRect.bottom= y + alien1Bmp.getHeight();
        }
    }

    public void draw(Canvas c) {
        tick++;
        if (tick > 20) tick = 0;
        if (tick < 10) {
            c.drawBitmap(alien1Bmp, x, y, null);
        } else {
            c.drawBitmap(alien2Bmp, x, y, null);
        }

    }

    public Rect getRect() {
        return myRect;
    }
}
