package edu.cs4730.drawdemo3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
 *  In this example, we want movement even when the user is not interacting with the
 *  screen.
 *    An alien is randomly placed on the screen and the moves to the right.  It reset to the left
 *    side if it is not touched.
 *    if touched, it moves to another place on the screen.
 *  The alien and movement is scaled based on the density of the screen as well.
 *
 */
public class mySurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    private myThread thread;
    public Paint  black;
    public int x, y;
    private int height = 480, width=480 ;  //defaults incase not set yet.
    private int alienheight, alienwidth;
    public Rect myRect;
    Bitmap alien;
    private Random myRand = new Random();
    float scale;

    public mySurfaceView(Context context) {
        super(context);

        //load a picture and draw it onto the screen.
        alien = BitmapFactory.decodeResource(getResources(), R.drawable.alien);
        scale = getResources().getDisplayMetrics().density; //this gives me the scale value for a mdpi baseline of 1.
        //scale up the alien, so it bigger for dp
        alienheight = (int) (alien.getHeight() * scale);
        alienwidth = (int) (alien.getWidth() * scale);
        myRect = new Rect();
        x = myRand.nextInt(width - alienwidth);  //kept it inside the screen.
        y = myRand.nextInt(height - alienheight);
        myRect.set(x,y,x +alienwidth,y+alienheight);

        black = new Paint();  //default is black and we really are not using it.  need it to draw the alien.

        //setup the thread for animation.
        getHolder().addCallback(this);
        thread = new myThread(getHolder(), this);
    }

    @Override
    public void draw(Canvas c) {
        super.draw(c);
        c.drawColor(Color.WHITE);
        c.drawBitmap(alien, null, myRect, black);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //everything is setup, now start.
        height = getHeight();
        width = getWidth();
        thread.setRunning(true);
        thread.start();
    }
    /*
     * touch event to deal with when the user touches the alien.
     *
     */
    @Override public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        // Retrieve the new x and y touch positions
        int touchx = (int) event.getX();
        int touchy = (int) event.getY();
        if (myRect.contains(touchx,touchy)) {
            //touched the alien
            Log.v("hi", "collison");
            x = myRand.nextInt(width - alienwidth);
            y = myRand.nextInt(height - alienheight);
            //we could set each manually, or just use the set method.
           //myRect.left =  x;
           // myRect.top = y;
            myRect.set(x,y,x +alienwidth,y+alienheight);
        }
        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // simply copied from sample application LunarLander:
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // we will try it again and again...
            }
        }
    }

    class myThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private mySurfaceView _mySurfaceView;
        private boolean _run = false;

        public myThread(SurfaceHolder surfaceHolder, mySurfaceView SurfaceView) {
            _surfaceHolder = surfaceHolder;
            _mySurfaceView = SurfaceView;
        }
        public void setRunning(boolean run) {
            _run = run;
        }
        @Override
        public void run() {
            Canvas c;
            x=10;
            while (_run) {
                c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {
                        //_mySurfaceView.onDraw(c);
                        _mySurfaceView.draw(c);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                //move the alien accross the screen.
                x += 2*scale;
                if (x>width - alienwidth) {x=10;}
                myRect.left = x; myRect.right =x + alienwidth;
                //sleep for a short period of time.
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        }
    }


}
