package edu.cs4730.surfaceviewdemo_kt

import androidx.appcompat.app.AppCompatActivity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import java.util.*

/**
 * simple example of a surfaceView with a picture that moves across the screen with a touchlistener.
 * All the code to make this work is in this class.
 */
class AllinOneActivity : AppCompatActivity(), SurfaceHolder.Callback {
    private lateinit var thread: myThread
    lateinit var black: Paint
    var x = 0
    var y = 0
    private var height = 480
    private var width = 480 //defaults incase not set yet.
    private var alienheight = 0
    private var alienwidth = 0
    lateinit var myRect: Rect
    lateinit var alien: Bitmap
    private val myRand = Random()
    var scale = 0f
    lateinit var mSurfaceView: SurfaceView
    var TAG = "AllinOneActivity"

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setup everything needed.
        //load a picture and draw it onto the screen.
        alien = BitmapFactory.decodeResource(resources, R.drawable.alien)
        scale =
            resources.displayMetrics.density //this gives me the scale value for a mdpi baseline of 1.
        //scale up the alien, so it bigger for dp
        alienheight = (alien.height * scale).toInt()
        alienwidth = (alien.width * scale).toInt()
        myRect = Rect()
        x = myRand.nextInt(width - alienwidth) //kept it inside the screen.
        y = myRand.nextInt(height - alienheight)
        myRect[x, y, x + alienwidth] = y + alienheight
        black =
            Paint() //default is black and we really are not using it.  need it to draw the alien.


        //get a generic surface and all our callbacks to it, with a touchlistener.
        mSurfaceView = SurfaceView(this)
        mSurfaceView.holder.addCallback(this)
        mSurfaceView.setOnTouchListener { v, event ->
            val action = event.action
            // Retrieve the new x and y touch positions
            val touchx = event.x.toInt()
            val touchy = event.y.toInt()
            if (myRect.contains(touchx, touchy)) {
                //touched the alien
                Log.v(TAG, "collision")
                x = myRand.nextInt(width - alienwidth)
                y = myRand.nextInt(height - alienheight)
                //we could set each manually, or just use the set method.
                //myRect.left =  x;
                // myRect.top = y;
                myRect[x, y, x + alienwidth] = y + alienheight
                mSurfaceView.invalidate()
                v.performClick()
            }
            true
        }
        setContentView(mSurfaceView)  //the layout is surfaceview, so binding or xml is needed here.
    }

    //simple helper method to draw on the canvas.    (note in a SurfaceView, this is an overridden method.
    fun draw(c: Canvas?) {
        c!!.drawColor(Color.WHITE)
        c.drawBitmap(alien, null, myRect, black)
    }

    //all the methods needed for the SurfaceHolder.Callback
    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.v(TAG, "surfaceCreated")
        //everything is setup, now start.
        height = mSurfaceView.height
        width = mSurfaceView.width
        //setup the thread for animation.
        thread = myThread(mSurfaceView.holder)
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.v(TAG, "surfaceChanged")
        //ignored.
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.v(TAG, "surfaceDestroyed")
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        var retry = true
        thread.setRunning(false)
        while (retry) {
            try {
                thread.join()
                retry = false
            } catch (e: InterruptedException) {
                // we will try it again and again...
            }
        }
    }

    /*
     * this is the thread that causes the drawing and movement of an alien (pic) moving accross the screen.
     */
    internal inner class myThread(private val _surfaceHolder: SurfaceHolder) : Thread() {
        private var Running = false
        fun setRunning(run: Boolean) {
            Running = run
        }

        override fun run() {
            var c: Canvas?
            x = 10
            while (Running && !interrupted()) {
                c = null
                try {
                    c = _surfaceHolder.lockCanvas(null)
                    synchronized(_surfaceHolder) {
                        //call a method that draws all the required objects onto the canvas.
                        draw(c)
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c)
                    }
                }
                //move the alien accross the screen.
                x += (2 * scale).toInt()
                if (x > width - alienwidth) {
                    x = 10
                }
                myRect.left = x
                myRect.right = x + alienwidth
                //sleep for a short period of time.
                if (!Running) return  //don't sleep, just exit if we are done.
                try {
                    sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }
}