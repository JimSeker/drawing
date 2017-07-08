package edu.cs4730.flappyalien;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.Random;

/**
 * Created by Seker on 7/8/2017.
 */

public class mySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private Context myContext;
    private myThread thread;
    public Paint red, black;
    boolean setup= false, gameover = false;
    int height, width, gap=150;
    float scale;
    int tap;
    String TAG = "surfaceview";
    ObjAlien alien;
    ObjPipe pipe, pipe2;
    Random r;

    public mySurfaceView(Context context) {
        super(context);
        myContext = context;
        Log.wtf(TAG, "first constructor");
        getHolder().addCallback(this);
        thread = new myThread(getHolder(), this);
    }

    public mySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        myContext = context;
         Log.wtf(TAG, "second constructor");
        getHolder().addCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.wtf(TAG, "sufacecreated");
        thread = new myThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.wtf(TAG, "surfaceChanged");
        gameSetup(width, height);
    }

    public void gameSetup(int w, int h) {
        scale = myContext.getResources().getDisplayMetrics().density;  //this gives me the scale value for a mdpi baseline of 1.
        width=w;
        height= h;
        r = new Random();
        alien = new ObjAlien(myContext,w/4,h/2, w,h);
        gap = alien.getHeight() * 3;
        pipe = new ObjPipe(myContext, w,h, gap, scale);
        pipe.setup(r.nextInt(h-gap - 10));
        pipe2 = new ObjPipe(myContext, w,h, gap, scale);
        pipe2.setup(r.nextInt(h-gap - 10));
        pipe2.imsecond();
        //very last thing
        setup = true;  //now it will start drawing.
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.wtf(TAG, "sufaceDestoryed");
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

    @Override
    public void draw(Canvas c) {
        super.draw(c);
        if ((setup) && (c != null) ){  //we know height and width of the screen, otherwise, ignore.
            c.drawColor(Color.WHITE);  //entire screen black, then draw on the background.


            pipe.draw(c);
            pipe2.draw(c);
            alien.draw(c);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {  //assuming click, without dealing with the up
            tap = 0;
            return true;
        }
        return false;
    }

    void checkGameState() {
        if (setup) {
            boolean test;
            //move pipe
            if (pipe.move()) pipe.setup(r.nextInt(height - gap - 10));
            if (pipe2.move()) pipe2.setup(r.nextInt(height - gap - 10));
            //up/down on alien
            tap++;
            if (tap <20) {  //going up
                alien.decr();
            } else if (tap>40){//goind down
                alien.incr();
            }
            //collisions when I get there.
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
            gameover = false;
            while (_run) {
                if (!gameover) {
                    //verify game state, move objects, etc...
                   checkGameState();
                }
                //redraw screen?
                c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {
                        _mySurfaceView.draw(c);  //lint error, but not sure why.
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                try {
                    Thread.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
