package edu.cs4730.surfaceviewdemo_kt

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.SurfaceView
import android.view.SurfaceHolder
import android.view.MotionEvent
import java.util.Random


/**
 * In this example, we want movement even when the user is not interacting with the
 * screen.
 * An alien is randomly placed on the screen and the moves to the right.  It reset to the left
 * side if it is not touched.
 * if touched, it moves to another place on the screen.
 * The alien and movement is scaled based on the density of the screen as well.
 */
class mySurfaceView(context: Context?) : SurfaceView(context), SurfaceHolder.Callback {
    private var thread: myThread? = null
    var black: Paint
    var x: Int
    var y: Int
    var myHeight = 480
    var myWidth = 480 //defaults incase not set yet.
    private val alienheight: Int
    private val alienwidth: Int
    var myRect: Rect
    var alien: Bitmap
    private val myRand = Random()
    var scale: Float
    var TAG = "mySurfaceView"
    override fun draw(c: Canvas) {
        super.draw(c)
        c.drawColor(Color.WHITE)
        c.drawBitmap(alien, null, myRect, black)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        //everything is setup, now start.
        Log.v(TAG, "surfaceCreated")
        myHeight = getHeight()
        myWidth = getWidth()
        thread = myThread(getHolder(), this)
        thread!!.setRunning(true)
        thread!!.start()
    }

    /*
     * touch event to deal with when the user touches the alien.
     *
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        // Retrieve the new x and y touch positions
        val touchx = event.x.toInt()
        val touchy = event.y.toInt()
        if (myRect.contains(touchx, touchy)) {
            //touched the alien
            Log.v("hi", "collision")
            x = myRand.nextInt(myWidth - alienwidth)
            y = myRand.nextInt(myHeight - alienheight)
            //we could set each manually, or just use the set method.
            //myRect.left =  x;
            // myRect.top = y;
            myRect[x, y, x + alienwidth] = y + alienheight
            performClick()
        }
        return true
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
        thread!!.setRunning(false)
        while (retry) {
            try {
                thread!!.join()
                retry = false
            } catch (e: InterruptedException) {
                // we will try it again and again...
            }
        }
    }

    /*
     * this is the thread that causes the drawing and movement of an alien (pic) moving accross the screen.
     */
    internal inner class myThread(
        private val _surfaceHolder: SurfaceHolder,
        private val _mySurfaceView: mySurfaceView
    ) : Thread() {
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
                        //_mySurfaceView.onDraw(c);
                        _mySurfaceView.draw(c)
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
                if (x > myWidth - alienwidth) {
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

    init {

        //load a picture and draw it onto the screen.
        alien = BitmapFactory.decodeResource(resources, R.drawable.alien)
        scale =
            resources.displayMetrics.density //this gives me the scale value for a mdpi baseline of 1.
        //scale up the alien, so it bigger for dp
        alienheight = (alien.height * scale).toInt()
        alienwidth = (alien.width * scale).toInt()
        myRect = Rect()
        x = myRand.nextInt(myWidth - alienwidth) //kept it inside the screen.
        y = myRand.nextInt(myHeight - alienheight)
        myRect[x, y, x + alienwidth] = y + alienheight
        black =
            Paint() //default is black and we really are not using it.  need it to draw the alien.

        //setup the thread for animation.
        holder.addCallback(this)
    }
}