package edu.cs4730.textureviewdemo_kt

import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.view.TextureView.SurfaceTextureListener
import android.view.TextureView
import android.os.Bundle
import android.widget.FrameLayout
import android.view.Gravity
import android.graphics.SurfaceTexture
import kotlin.jvm.Volatile
import android.graphics.PorterDuff

/**
 * original example from  here: http://pastebin.com/J4uDgrZ8  with much thanks.
 * Everything for the texture in in the MainActivity.  And a generic textureView is used
 */
class AllinOneActivity : AppCompatActivity(), SurfaceTextureListener {
    private lateinit var mTextureView: TextureView
    private lateinit var mThread: RenderingThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //get a layout, which we will use later.
        val content = FrameLayout(this)

        //get a TextureView and set it up with all code below
        mTextureView = TextureView(this)
        mTextureView.surfaceTextureListener = this
        //mTextureView.setOpaque(false);  //normally, yes, but we need to see the block here.

        //add the TextureView to our layout from above.
        content.addView(mTextureView, FrameLayout.LayoutParams(500, 500, Gravity.CENTER))
        setContentView(content)
    }

    /**
     * TextureView.SurfaceTextureListener overrides below, that start up the drawing thread.
     */
    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        mThread = RenderingThread(mTextureView)
        mThread.start()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        // Ignored
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        mThread.stopRendering()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        // Ignored
    }

    /*
     * Thread to draw a green square moving around the textureView.
     */
    private class RenderingThread(private val mSurface: TextureView?) : Thread() {
        @Volatile
        private var mRunning = true
        override fun run() {
            var x = 0.0f
            var y = 0.0f
            var speedX = 5.0f
            var speedY = 3.0f
            val paint = Paint()
            paint.color = -0xff0100
            while (mRunning && !interrupted()) {
                val canvas = mSurface!!.lockCanvas(null)
                try {
                    //canvas.drawColor(0x00000000, PorterDuff.Mode.CLEAR);
                    canvas!!.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR)
                    canvas.drawRect(x, y, x + 20.0f, y + 20.0f, paint)
                } finally {
                    mSurface.unlockCanvasAndPost(canvas!!)
                }
                if (x + 20.0f + speedX >= mSurface.width || x + speedX <= 0.0f) {
                    speedX = -speedX
                }
                if (y + 20.0f + speedY >= mSurface.height || y + speedY <= 0.0f) {
                    speedY = -speedY
                }
                x += speedX
                y += speedY
                try {
                    sleep(15)
                } catch (e: InterruptedException) {
                    // Interrupted
                }
            }
        }

        fun stopRendering() {
            interrupt()
            mRunning = false
        }
    }
}