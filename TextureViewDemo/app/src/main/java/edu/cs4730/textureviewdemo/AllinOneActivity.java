package edu.cs4730.textureviewdemo;

import androidx.annotation.NonNull;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.TextureView;
import android.widget.FrameLayout;


/**
 * original example from  here: http://pastebin.com/J4uDgrZ8  with much thanks.
 * Everything for the texture in in the MainActivity.  And a generic textureView is used
 */

public class AllinOneActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private TextureView mTextureView;
    private RenderingThread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //get a layout, which we will use later.
        FrameLayout content = new FrameLayout(this);

        //get a TextureView and set it up with all code below
        mTextureView = new TextureView(this);
        mTextureView.setSurfaceTextureListener(this);
        //mTextureView.setOpaque(false);  //normally, yes, but we need to see the block here.

        //add the TextureView to our layout from above.
        content.addView(mTextureView, new FrameLayout.LayoutParams(500, 500, Gravity.CENTER));
        setContentView(content);
    }

    /**
     * TextureView.SurfaceTextureListener overrides below, that start up the drawing thread.
     */
    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        mThread = new RenderingThread(mTextureView);
        mThread.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
        // Ignored
    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        if (mThread != null) mThread.stopRendering();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
        // Ignored
    }

    /*
     * Thread to draw a green square moving around the textureView.
     */
    private static class RenderingThread extends Thread {
        private final TextureView mSurface;
        private volatile boolean mRunning = true;

        public RenderingThread(TextureView surface) {
            mSurface = surface;
        }

        @Override
        public void run() {
            float x = 0.0f;
            float y = 0.0f;
            float speedX = 5.0f;
            float speedY = 3.0f;

            Paint paint = new Paint();
            paint.setColor(0xff00ff00);

            while (mRunning && !Thread.interrupted()) {
                final Canvas canvas = mSurface.lockCanvas(null);
                try {
                    //canvas.drawColor(0x00000000, PorterDuff.Mode.CLEAR);
                    canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
                    canvas.drawRect(x, y, x + 20.0f, y + 20.0f, paint);
                } finally {
                    mSurface.unlockCanvasAndPost(canvas);
                }

                if (x + 20.0f + speedX >= mSurface.getWidth() || x + speedX <= 0.0f) {
                    speedX = -speedX;
                }
                if (y + 20.0f + speedY >= mSurface.getHeight() || y + speedY <= 0.0f) {
                    speedY = -speedY;
                }

                x += speedX;
                y += speedY;

                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    // Interrupted
                }
            }
        }

        void stopRendering() {
            interrupt();
            mRunning = false;
        }
    }
}
