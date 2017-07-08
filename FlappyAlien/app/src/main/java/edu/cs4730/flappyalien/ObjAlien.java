package edu.cs4730.flappyalien;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by Seker on 7/8/2017.
 */

public class ObjAlien {
    Bitmap alien1Bmp, alien2Bmp;
    int x, y;  //alien topleft position
    int width, height;  //boardsize info.
    Context myContext;
    int tick = 0;
    String TAG = "objAlien";
    ObjAlien() {

    }

    ObjAlien(Context c, int x, int y, int w, int h) {
        myContext =c;
        this.x =x;
        this.y = y;
        width = w; height = h;
        alien1Bmp = BitmapFactory.decodeResource(myContext.getResources(),  R.mipmap.alien );
        alien2Bmp = BitmapFactory.decodeResource(myContext.getResources(),  R.mipmap.alien2 );
        Log.wtf(TAG, "Alien height is " + alien1Bmp.getHeight());
    }

    public int getHeight( ) {
        return alien1Bmp.getHeight();
    }

    //going up, so decrement y
    public void decr() {
        y-=1;
        if (y<0) y =0;
    }

    //going down, so incr y
    public void incr() {
        y+=2;
        if (y + alien1Bmp.getHeight() >height) y = height - alien1Bmp.getHeight();
    }
    public void draw(Canvas c) {
       tick++;
        if (tick >20) tick =0;
        if (tick <10) {
            c.drawBitmap(alien1Bmp,x, y, null);
        } else {
            c.drawBitmap(alien2Bmp,x, y, null);
        }

    }
}
