package edu.cs4730.drawdemo3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 *
 */
public class mySurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    private myThread thread;
    public Paint  black;
    public float x, y;
    Bitmap alien;

    public mySurfaceView(Context context) {
        super(context);

        //load a picture and draw it onto the screen.
        alien = BitmapFactory.decodeResource(getResources(), R.drawable.alien);

        black = new Paint();  //default is black
        black.setStyle(Paint.Style.STROKE);

        getHolder().addCallback(this);
        thread = new myThread(getHolder(), this);
    }

    @Override
    public void onDraw(Canvas c) {
        c.drawColor(Color.WHITE);
        c.drawRect(x, y, x + 90, y + 90, black);
        c.drawCircle(55, 55, 20, black);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
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
                        _mySurfaceView.onDraw(c);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                x++;
                if (x>90) {x=10;}
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block

                }
            }
        }
    }


}
