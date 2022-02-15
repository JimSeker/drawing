package edu.cs4730.flappyalien;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;

import java.util.Random;

/**
 * Created by Seker on 7/8/2017.
 * <p>
 * Creates a simple flappy game with an alien.
 */

public class myTextureView extends TextureView implements TextureView.SurfaceTextureListener {

    private Context myContext;
    private myThread thread;
    public Paint red, black;
    boolean gameover = true;
    int height, width, gap = 150;
    float scale;
    int tap;
    int score = 0;
    String TAG = "textureview";
    ObjAlien alien;
    ObjPipe pipe, pipe2;
    Random r;
    int WordHeight;

    public myTextureView(Context context) {
        super(context);
        myContext = context;
        Log.wtf(TAG, "first constructor");
        setSurfaceTextureListener(this);  //Required or the TextureView never starts up.
    }

    public myTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        myContext = context;
        Log.wtf(TAG, "second constructor");
        setSurfaceTextureListener(this);  //Required or the TextureView never starts up.

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.v(TAG, "onSurfaceTextureAvailable");
        gameSetup(width, height);
        thread = new myThread(this);
        thread.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.wtf(TAG, "surfaceChanged");

    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        //Log.v(TAG,  "onSurfaceTextureUpdated");  //this is called a lot!
        // Ignored  we could add on to the surface after everything is drawn, I think.
    }


    public void gameSetup(int w, int h) {
        black = new Paint();
        red = new Paint();
        red.setColor(Color.RED);
        red.setTextSize(red.getTextSize() * 10);
        scale = myContext.getResources().getDisplayMetrics().density;  //this gives me the scale value for a mdpi baseline of 1.
        width = w;
        height = h;
        r = new Random();
        alien = new ObjAlien(myContext, w / 4, h / 2, w, h);
        gap = alien.getHeight() * 3;
        pipe = new ObjPipe(myContext, w, h, gap, scale);
        pipe.setup(r.nextInt(h - gap - 10));
        pipe2 = new ObjPipe(myContext, w, h, gap, scale);
        pipe2.setup(r.nextInt(h - gap - 10));
        pipe2.imsecond();
        score = 0;

        Rect bounds = new Rect();
        String mText = String.valueOf(score);
        red.getTextBounds(String.valueOf(score), 0, mText.length(), bounds);
        WordHeight = bounds.height();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.v(TAG, "onSurfaceTextureDestroyed");
        if (thread != null) thread.stopRendering();
        return true;
    }


    public void myDraw(Canvas c) {
        super.draw(c);
        if ((c != null)) {  //we know height and width of the screen, otherwise, ignore.
            c.drawColor(Color.WHITE);  //entire screen black, then draw on the background.


            pipe.draw(c);
            pipe2.draw(c);
            alien.draw(c);

            if (gameover) {
                //draw game over, tap to start.
                c.drawText("GAME OVER", 100 * scale, 100 * scale, red);
                c.drawText("Tap to start", 100 * scale, (100 * scale) + WordHeight, red);
            }
            String mText = String.valueOf(score);
            c.drawText(mText, 100*scale, WordHeight, red);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {  //assuming click, without dealing with the up
            if (gameover) {
                gameSetup(width, height);
                gameover = false;
            } else {
                tap = 0;
            }
            return true;
        }
        return false;
    }

    void checkGameState() {
        //move pipe
        if (pipe.move()) pipe.setup(r.nextInt(height - gap - 10));
        if (pipe2.move()) pipe2.setup(r.nextInt(height - gap - 10));
        //up/down on alien
        tap++;
        if (tap < 20) {  //going up
            alien.decr();
        } else if (tap > 40) {//goind down
            alien.incr();
        }
        //collisions when I get there.
        if (pipe.collide(alien.getRect()) || pipe2.collide(alien.getRect())) {
            gameover = true;
        }

        //scoring, so alien has passed the pipe and not already scored.
        if (!pipe.getScored()) {
            //now alien is fully past the pipe.
            if (alien.getRect().left > pipe.getRect().right) {
                score++;
                pipe.setScored(true);
            }
        }
        if (!pipe2.getScored()) {
            //now alien is fully past the pipe.
            if (alien.getRect().left > pipe2.getRect().right) {
                score++;
                pipe2.setScored(true);
            }
        }

    }

    class myThread extends Thread {
        private final TextureView mSurface;
        private volatile boolean mRunning = true;

        public myThread(TextureView surface) {
            mSurface = surface;
        }

        void stopRendering() {
            interrupt();
            mRunning = false;
        }

        @Override
        public void run() {
            //animation thread.
            while (mRunning && !Thread.interrupted()) {
                if (!gameover) {
                    //verify game state, move objects, etc...
                    checkGameState();
                }
                //redraw screen
                final Canvas canvas = mSurface.lockCanvas(null);
                try {
                    myDraw(canvas);
                } finally {
                    mSurface.unlockCanvasAndPost(canvas);
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
