package edu.cs4730.alieninvaders_kt

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.SurfaceView
import android.view.SurfaceHolder
import edu.cs4730.alieninvaders_kt.mySurfaceView.myThread
import edu.cs4730.alieninvaders_kt.obj
import edu.cs4730.alieninvaders_kt.ColorList
import edu.cs4730.alieninvaders_kt.objShip
import edu.cs4730.alieninvaders_kt.objAlien
import edu.cs4730.alieninvaders_kt.objShot
import android.view.MotionEvent
import edu.cs4730.alieninvaders_kt.mySurfaceView
import edu.cs4730.alieninvaders_kt.R
import java.util.*

/**
 * This code implements a basic space like invaders game.   The code to move the aliens is in
 * the process of being rewritten, but I'm still not sure I shouldn't just burn it and start over.
 *
 * The rest of the code should be pretty good at this point with minor fixes needed here and
 * there.
 */
class mySurfaceView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    var TAG = "mySurfaceView"
    private var thread: myThread? = null

    //for drawing
    var color: Paint
    var black: Paint
    var shipBmp: Bitmap
    var alien1Bmp: Bitmap
    var alien2Bmp: Bitmap
    var shotBmp: Bitmap
    lateinit var bg: Bitmap

    //basic where are they
    var left = 0f
    var right = 0f
    var top = 0f
    var bottom = 0f
    var leftbtn = 0f
    var firebtn = 0f
    var rightbtn = 0f
    var scale: Float
    var fontHeight = 0f
    var gameOverX = 0f
    var gameOverY = 0f

    //game variables
    var gameover = false
    var alienmove = 1
    var shotmove = 1
    var shipmove = 1
    var maxAliens = 5
    var maxShots = 3
    var score = 0
    var moveship = 0
    var tofire = false
    lateinit var ship: obj
    var shots: Vector<obj>
    var aliens: Vector<obj>
    var myRandom: Random
    var myColor = ColorList()

    //this is likely only run once, when the app starts up.
    //so lots of setup is done here, but we need to wait until we have height and width.
    fun setupBG(w: Int, h: Int) {
        bg = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        var c = Canvas(bg)
        left = 0f
        right = w.toFloat()
        //approx 80 pixels at the top and bottom.  the bottom is where we have the left/fire/right "buttons"
        val standoff = 80 * scale
        val third = (w / 3).toFloat()
        //third of the width.  plus "height" of buttons
        leftbtn = third
        firebtn = third + third //less computation in add then multiple by 2.
        rightbtn = w.toFloat()
        bottom = h - standoff
        top = standoff

        //font information:
        val fm = black.fontMetrics
        fontHeight = fm.descent - fm.ascent

        //drawing the bg image
        black.strokeWidth = 3 * scale
        //now draw top area in blue  with score text.
        color.color = myColor.getColorbyIndex(19) //should be a blue.
        c.drawRect(0f, 0f, w.toFloat(), top, color)

        //leave middle as light gray
        color.color = myColor.getColorbyIndex(9) //should be a blue.
        c.drawRect(w.toFloat(), top + 1, 0f, bottom - 1, color)
        //draw green bottom, with lines for the buttons.  plus text.
        color.color = myColor.getColorbyIndex(4) //should be a blue.
        c.drawRect(0f, bottom, w.toFloat(), h.toFloat(), color)
        c.drawLine(leftbtn, bottom, leftbtn, h.toFloat(), black)
        c.drawLine(firebtn, bottom, firebtn, h.toFloat(), black)
        //now draw text
        val heightcenter =
            bottom + (standoff / 2 + fontHeight / 2) //fonts are drawn from the bottom of the font.
        val bounds = Rect()
        black.getTextBounds("LEFT", 0, "Left".length, bounds)
        c.drawText(
            "LEFT",
            third / 2.0f - bounds.width() / 2.0f,
            heightcenter,
            black
        ) //should be centered.
        black.getTextBounds("FIRE", 0, "FIRE".length, bounds)
        c.drawText(
            "FIRE",
            third + third / 2.0f - bounds.width() / 2.0f,
            heightcenter,
            black
        ) //should be centered.
        black.getTextBounds("RIGHT", 0, "RIGHT".length, bounds)
        c.drawText(
            "RIGHT",
            third + third + third / 2.0f - bounds.width() / 2.0f,
            heightcenter,
            black
        ) //should be centered.
        ship = objShip(
            (left + right).toInt() / 2 - shipBmp.width,
            bottom.toInt() - shipBmp.height,
            0,
            shipBmp.width,
            shipBmp.height,
            shipBmp
        )

        //game over location
        black.getTextBounds("Game Over", 0, "Game Over".length, bounds)
        gameOverX = right / 2.0f - bounds.width() / 2.0f
        gameOverY = h / 2.0f - fontHeight / 2.0f
        Log.wtf(TAG, "x and y is $gameOverX $gameOverY")
    }

    /*
     *  Override the draw method.  This is were all the "screen" drawing goes.
     */
    override fun draw(c: Canvas) {
        super.draw(c)
        c.drawColor(Color.BLACK) //entire screen black, then draw on the background.
        if (bg == null) return  //init setup has not completed.  don't draw yet.
        //draw background
        c.drawBitmap(bg, 0f, 0f, null)

        //draw score
        c.drawText("Score: $score", 0f, fontHeight, black)
        //draw the ship
        ship.draw(c)

        //draw aliens
        if (!aliens.isEmpty()) {
            for (i in aliens.indices) {
                (aliens.elementAt(i) as obj).draw(c)
            }
        }

        //draw shots
        if (!shots.isEmpty()) {
            for (i in shots.indices) {
                (shots.elementAt(i) as obj).draw(c)
            }
        }
        if (gameover) {
            c.drawText("GAME OVER", gameOverX, gameOverY, black)
        }
    }

    fun checkGameState() {
        var tempAlien: objAlien
        var tempShot: objShot

        //move ship and deal with new shot.
        if (moveship != 0) { //going left maybe
            if (ship.x + moveship >= left && ship.x + moveship <= right - shipBmp.width) { //don't move ship off edge of the board
                ship.move(moveship, 0)
            }
        }
        if (tofire) {  //user pushed the "fire button"
            if (shots.size < maxShots) {
                shots.addElement(
                    objShot(
                        ship.x + shipBmp.width / 2 - shotBmp.width / 2,
                        bottom.toInt() - shipBmp.height - shotBmp.height,
                        0,
                        shotBmp.width,
                        shotBmp.height,
                        shotBmp
                    )
                )
            }
            tofire = false
        }

        //aliens section
        if (!aliens.isEmpty()) {
            //move aliens and check for landing.
            for (i in aliens.indices) {
                tempAlien = aliens.elementAt(i) as objAlien
                tempAlien.move(alienmove, left, right)
                if (tempAlien.y + alien1Bmp.height >= bottom) {  //alien landed.  game over!
                    gameover = true
                }
            }
            //add another alien?
            if (aliens.size < maxAliens) {
                if (myRandom.nextInt(100) > 98) { //3 percent change of a new alien being added right now.
                    val x =
                        left.toInt() + myRandom.nextInt(right.toInt() - left.toInt() - alien1Bmp.width)
                    aliens.addElement(
                        objAlien(
                            x,
                            top.toInt(),
                            0,
                            alien1Bmp.width,
                            alien1Bmp.height,
                            alien1Bmp,
                            alien2Bmp
                        )
                    )
                }
            }
        } else {  //no aliens on the board, so add a new one
            val x = left.toInt() + myRandom.nextInt(right.toInt() - left.toInt() - alien1Bmp.width)
            aliens.addElement(
                objAlien(
                    x,
                    top.toInt(),
                    0,
                    alien1Bmp.width,
                    alien1Bmp.height,
                    alien1Bmp,
                    alien2Bmp
                )
            )
        }

        //move shots
        if (!shots.isEmpty()) {
            var i = 0
            while (i < shots.size) {

                //first move it.
                tempShot = shots.elementAt(i) as objShot
                tempShot.move(-shotmove, shotmove, top)
                if (!tempShot.isAlive) { //return false if dead

                    //so remove it and check the next;
                    shots.removeElementAt(i)
                    --i
                }
                i++
            }
        }
        //check for collisions
        if (!(shots.isEmpty() || aliens.isEmpty())) { //no collisions if no aliens or no shots
            var i = 0
            while (i < shots.size) {
                tempShot = shots.elementAt(i) as objShot
                var j = 0
                while (j < aliens.size && tempShot.alive) {
                    tempAlien = aliens.elementAt(j) as objAlien
                    if (tempShot.collision(tempAlien.rec)) {
                        tempShot.dead()
                        tempAlien.dead()
                        score += tempAlien.score()
                        aliens.removeElementAt(j)
                    }
                    if (!tempShot.alive) {
                        shots.removeElementAt(i)
                        i--
                    }
                    j++
                }
                i++
            }
        }
    }

    /*
     * touch event to deal with the left, right, and fire "button"
     *
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        // Retrieve the new x and y touch positions
        val x = event.x
        val y = event.y
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (y > bottom) { //below playing area
                    if (x < leftbtn) { //left
                        moveship =
                            -shipmove //and yet this seems to be dpi independent, I think... why?
                    } else if (x < firebtn) { //fire
                        tofire = true
                    } else if (x <= rightbtn) {
                        moveship = shipmove
                    }
                    return true
                }
                if (y > bottom) { //below playing area
                    if (x < leftbtn) { //left
                        moveship = 0
                    } else if (x < firebtn) { //don't care about fire, handled in down.
                        return true
                    } else if (x <= rightbtn) {
                        moveship = 0
                    }
                    return true
                }
            }
            MotionEvent.ACTION_UP -> if (y > bottom) {
                if (x < leftbtn) {
                    moveship = 0
                } else if (x < firebtn) {
                    return true
                } else if (x <= rightbtn) {
                    moveship = 0
                }
                return true
            }
        }
        return false
    }

    /*
     *  Three surface methods to override.  created, changed, and destroyed.
     *  setup on create, do nothing in changed, and shut it all down in destroyed.
     */
    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        Log.v(TAG, "surfaceCreated ")
        // setup the background picture.
        Log.v(TAG, "size is width=$width height is $height")
        setupBG(width, height)
        thread = myThread(holder, this)
        thread!!.setRunning(true)
        thread!!.start()
    }

    override fun surfaceChanged(
        surfaceHolder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) {
        Log.v(TAG, "surfaceChanged ")
        Log.v(TAG, "size is width=$width height is $height")
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        Log.v(TAG, "surfaceDestroyed")
        // simply copied from sample application LunarLander:
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
     *  basic thread to do the drawing that is started in SurfaceView Created and ended in destroyed.
     *  most of the code is above surfaceview methods, such as draw, and checkgamestate.
     */
    private inner class myThread(
        private val _surfaceHolder: SurfaceHolder,
        private val _mySurfaceView: mySurfaceView
    ) : Thread() {
        private var running = false
        fun setRunning(run: Boolean) {
            running = run
        }

        override fun run() {
            var c: Canvas?
            gameover = false
            while (running) {
                if (!gameover) {
                    //verify game state, move objects, etc...
                    checkGameState()
                }
                c = null
                try {
                    c = _surfaceHolder.lockCanvas(null)
                    if (!running || c == null) return  //just in case checkGameState took a while.
                    synchronized(_surfaceHolder) {
                        _mySurfaceView.draw(c) //lint error, but not sure why.
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c)
                    }
                }
                if (!running) return  //no need to sleep, we are done.
                try {
                    sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    init {

        //BitmapFactory.Options() has methods to scale or not to scale.
        alien1Bmp = BitmapFactory.decodeResource(resources, R.drawable.alien)
        alien2Bmp = BitmapFactory.decodeResource(resources, R.drawable.alien2)
        shotBmp = BitmapFactory.decodeResource(context.resources, R.drawable.shot)
        shipBmp = BitmapFactory.decodeResource(context.resources, R.drawable.ship)

        //now deal with the density ie dpi for the screen size.  All above of this assumes mdpi (default 1:1), except
        //well the new phones and stuff are xxdvpi (which android says won't happen...), xdpi, etc.  So need scaling
        //this gives me the scale value for a mdpi baseline of 1.
        scale = resources.displayMetrics.density

        //basic setup of paint objects.
        color = Paint()
        color.color = Color.BLUE
        color.style = Paint.Style.FILL
        //
        black = Paint() //default is black
        //black.setStyle(Paint.Style.STROKE);
        black.style = Paint.Style.FILL
        // black.setTextSize(black.getTextSize() * 2.0f* scale);  //scale the font size too
        black.textSize = 20 * scale //20 point font? (not really)  with scaling.
        black.isFakeBoldText = true
        myRandom = Random()

        //game variable setup.
        moveship = 0
        tofire = false
        aliens = Vector()
        shots = Vector()
        shotmove *= scale.toInt()
        alienmove *= scale.toInt()
        shipmove += scale.toInt()

        //required call to kick start the surfaceView callbacks.
        holder.addCallback(this)
    }
}