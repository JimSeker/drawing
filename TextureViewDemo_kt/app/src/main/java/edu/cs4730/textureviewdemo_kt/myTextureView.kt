package edu.cs4730.textureviewdemo_kt

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.graphics.SurfaceTexture
import kotlin.jvm.Volatile
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.util.Log

/**
 * This separates all the TextureView code into the TextureView.
 */
class myTextureView : TextureView, SurfaceTextureListener {
    private lateinit var mThread: RenderingThread
    var TAG = "myTextView"

    // set of consturctors needed for the TextureView, most of which we then ignore the parameters anyway.
    constructor(context: Context?) : super(context!!) {
        Log.v(TAG, "constructor")
        surfaceTextureListener = this //Required or the TextureView never starts up.
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!
    ) {
        Log.v(TAG, "constructor2")
        surfaceTextureListener = this //Required or the TextureView never starts up.
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!
    ) {
        Log.v(TAG, "constructor3")
        surfaceTextureListener = this //Required or the TextureView never starts up.
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context!!
    ) {
        Log.v(TAG, "constructor4")
        surfaceTextureListener = this //Required or the TextureView never starts up.
    }

    /**
     * TextureView.SurfaceTextureListener overrides below, that start up the drawing thread.
     */
    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        Log.v(TAG, "onSurfaceTextureAvailable")
        //We can't override the draw(canvas) function, so we need to access the surface
        //via here and pass it to the thread to draw on it.
        mThread = RenderingThread(this)
        mThread.start()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        // Ignored
        Log.v(TAG, "onSurfaceTextureSizeChanged")
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        Log.v(TAG, "onSurfaceTextureDestroyed")
        mThread.stopRendering()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        //Log.v(TAG,  "onSurfaceTextureUpdated");  //this is called a lot!
        // Ignored
    }

    /*
     * Thread to draw a green square moving around the textureView.
     */
    internal inner class RenderingThread(private val mSurface: TextureView) : Thread() {
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
                val canvas = mSurface.lockCanvas(null)
                try {
                    canvas!!.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR)
                    //canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
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