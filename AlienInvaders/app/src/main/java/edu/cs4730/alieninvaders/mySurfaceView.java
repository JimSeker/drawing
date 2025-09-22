package edu.cs4730.alieninvaders;

import android.annotation.SuppressLint;
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
import java.util.Vector;

/**
 * This code implements a basic space like invaders game.   The code to move the aliens is in
 * the process of being rewritten, but I'm still not sure I shouldn't just burn it and start over.
 * <p>
 * The rest of the code should be pretty good at this point with minor fixes needed here and
 * there.
 */

public class mySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    String TAG = "mySurfaceView";

    private myThread thread;

    //for drawing
    public Paint color, black;
    Bitmap shipBmp, alien1Bmp, alien2Bmp, shotBmp, bg;

    //basic where are they
    float left, right, top, bottom;
    float leftbtn, firebtn, rightbtn;
    float scale;
    float fontHeight;
    float gameOverX, gameOverY;

    //game variables
    boolean gameover = false;
    int alienmove = 1, shotmove = 1, shipmove = 1;
    int maxAliens = 5, maxShots = 3;
    int score = 0;
    int moveship = 0;
    boolean tofire = false;
    obj ship;
    Vector<obj> shots, aliens;

    Random myRandom;
    ColorList myColor = new ColorList();

    public mySurfaceView(Context context) {
        super(context);

        //BitmapFactory.Options() has methods to scale or not to scale.
        alien1Bmp = BitmapFactory.decodeResource(getResources(), R.drawable.alien);
        alien2Bmp = BitmapFactory.decodeResource(getResources(), R.drawable.alien2);
        shotBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.shot);
        shipBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);

        //now deal with the density ie dpi for the screen size.  All above of this assumes mdpi (default 1:1), except
        //well the new phones and stuff are xxdvpi (which android says won't happen...), xdpi, etc.  So need scaling
        //this gives me the scale value for a mdpi baseline of 1.
        scale = getResources().getDisplayMetrics().density;

        //basic setup of paint objects.
        color = new Paint();
        color.setColor(Color.BLUE);
        color.setStyle(Paint.Style.FILL);
        //
        black = new Paint();  //default is black
        //black.setStyle(Paint.Style.STROKE);
        black.setStyle(Paint.Style.FILL);
        // black.setTextSize(black.getTextSize() * 2.0f* scale);  //scale the font size too
        black.setTextSize(20 * scale);  //20 point font? (not really)  with scaling.
        black.setFakeBoldText(true);
        myRandom = new Random();

        //game variable setup.
        moveship = 0;
        tofire = false;
        aliens = new Vector<obj>();
        shots = new Vector<obj>();
        shotmove *= (int) scale;
        alienmove *= (int) scale;
        shipmove += (int) scale;

        //required call to kick start the surfaceView callbacks.
        getHolder().addCallback(this);
    }

    //this is likely only run once, when the app starts up.
    //so lots of setup is done here, but we need to wait until we have height and width.
    void setupBG(int w, int h) {
        bg = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bg);

        left = 0;
        right = w;
        //approx 80 pixels at the top and bottom.  the bottom is where we have the left/fire/right "buttons"
        float standoff = 80 * scale;
        float third = w / 3f;
        //third of the width.  plus "height" of buttons
        leftbtn = third;
        firebtn = third + third; //less computation in add then multiple by 2.
        rightbtn = w;
        bottom = h - standoff;
        top = standoff;

        //font information:
        Paint.FontMetrics fm = black.getFontMetrics();
        fontHeight = fm.descent - fm.ascent;

        //drawing the bg image
        black.setStrokeWidth(3 * scale);
        //now draw top area in blue  with score text.
        color.setColor(myColor.getColorbyIndex(19));  //should be a blue.
        c.drawRect(0, 0, w, top, color);

        //leave middle as light gray
        color.setColor(myColor.getColorbyIndex(9));  //should be a blue.
        c.drawRect(w, top + 1, 0, bottom - 1, color);
        //draw green bottom, with lines for the buttons.  plus text.
        color.setColor(myColor.getColorbyIndex(4));  //should be a blue.
        c.drawRect(0, bottom, w, h, color);
        c.drawLine(leftbtn, bottom, leftbtn, h, black);
        c.drawLine(firebtn, bottom, firebtn, h, black);
        //now draw text
        float heightcenter = bottom + (standoff / 2 + fontHeight / 2);  //fonts are drawn from the bottom of the font.
        Rect bounds = new Rect();
        black.getTextBounds("LEFT", 0, "Left".length(), bounds);
        c.drawText("LEFT", third / 2.0f - bounds.width() / 2.0f, heightcenter, black);  //should be centered.
        black.getTextBounds("FIRE", 0, "FIRE".length(), bounds);
        c.drawText("FIRE", third + (third / 2.0f) - bounds.width() / 2.0f, heightcenter, black);  //should be centered.
        black.getTextBounds("RIGHT", 0, "RIGHT".length(), bounds);
        c.drawText("RIGHT", third + third + (third / 2.0f) - bounds.width() / 2.0f, heightcenter, black);  //should be centered.

        ship = new objShip(((int) (left + right) / 2) - shipBmp.getWidth(), (int) bottom - shipBmp.getHeight(), 0, shipBmp.getWidth(), shipBmp.getHeight(), shipBmp);

        //game over location
        black.getTextBounds("Game Over", 0, "Game Over".length(), bounds);
        gameOverX = right / 2.0f - bounds.width() / 2.0f;
        gameOverY = (h / 2.0f) - (fontHeight / 2.0f);
        Log.wtf(TAG, "x and y is " + gameOverX + " " + gameOverY);
    }

    /*
     *  Override the draw method.  This is were all the "screen" drawing goes.
     */
    @Override
    public void draw(Canvas c) {
        super.draw(c);
        c.drawColor(Color.BLACK);  //entire screen black, then draw on the background.

        if (bg == null) return;  //init setup has not completed.  don't draw yet.
        //draw background
        c.drawBitmap(bg, 0, 0, null);

        //draw score
        c.drawText("Score: " + score, 0, fontHeight, black);
        //draw the ship
        ship.draw(c);

        //draw aliens
        if (!aliens.isEmpty()) {
            for (int i = 0; i < aliens.size(); i++) {
                ((obj) aliens.elementAt(i)).draw(c);
            }
        }

        //draw shots
        if (!shots.isEmpty()) {
            for (int i = 0; i < shots.size(); i++) {
                ((obj) shots.elementAt(i)).draw(c);

            }
        }
        if (gameover) {
            c.drawText("GAME OVER", gameOverX, gameOverY, black);
        }
    }


    void checkGameState() {

        objAlien tempAlien;
        objShot tempShot;

        //move ship and deal with new shot.
        if (moveship != 0) { //going left maybe
            if (ship.x + moveship >= left && ship.x + moveship <= right - shipBmp.getWidth()) { //don't move ship off edge of the board
                ship.move(moveship, 0);
            }
        }
        if (tofire) {  //user pushed the "fire button"
            if (shots.size() < maxShots) {
                shots.addElement(new objShot(ship.x + (shipBmp.getWidth() / 2) - (shotBmp.getWidth() / 2), (int) bottom - shipBmp.getHeight() - shotBmp.getHeight(), 0, shotBmp.getWidth(), shotBmp.getHeight(), shotBmp));
            }
            tofire = false;
        }

        //aliens section
        if (!aliens.isEmpty()) {
            //move aliens and check for landing.
            for (int i = 0; i < aliens.size(); i++) {
                tempAlien = (objAlien) aliens.elementAt(i);
                tempAlien.move(alienmove, left, right);
                if (tempAlien.y + alien1Bmp.getHeight() >= bottom) {  //alien landed.  game over!
                    gameover = true;
                }
            }
            //add another alien?
            if (aliens.size() < maxAliens) {
                if (myRandom.nextInt(100) > 98) { //3 percent change of a new alien being added right now.
                    int x = (int) left + myRandom.nextInt((int) right - (int) left - alien1Bmp.getWidth());

                    aliens.addElement(new objAlien(x, (int) top, 0, alien1Bmp.getWidth(), alien1Bmp.getHeight(), alien1Bmp, alien2Bmp));
                }
            }
        } else {  //no aliens on the board, so add a new one
            int x = (int) left + myRandom.nextInt((int) right - (int) left - alien1Bmp.getWidth());
            aliens.addElement(new objAlien(x, (int) top, 0, alien1Bmp.getWidth(), alien1Bmp.getHeight(), alien1Bmp, alien2Bmp));
        }

        //move shots
        if (!shots.isEmpty()) {
            for (int i = 0; i < shots.size(); i++) {
                //first move it.
                tempShot = (objShot) shots.elementAt(i);
                tempShot.move(-shotmove, shotmove, top);
                if (!tempShot.isAlive()) { //return false if dead

                    //so remove it and check the next;
                    shots.removeElementAt(i);
                    --i;
                }

            }
        }
        //check for collisions
        if (!(shots.isEmpty() || aliens.isEmpty())) { //no collisions if no aliens or no shots
            for (int i = 0; i < shots.size(); i++) {
                tempShot = (objShot) shots.elementAt(i);
                for (int j = 0; j < aliens.size() && tempShot.alive; j++) {
                    tempAlien = (objAlien) aliens.elementAt(j);
                    if (tempShot.collision(tempAlien.rec)) {
                        tempShot.dead();
                        tempAlien.dead();
                        score += tempAlien.score();
                        aliens.removeElementAt(j);
                    }
                    if (!tempShot.alive) {
                        shots.removeElementAt(i);
                        i--;
                    }
                }
            }
        }
    }

    /*
     * touch event to deal with the left, right, and fire "button"
     *
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        // Retrieve the new x and y touch positions
        float x = event.getX();
        float y = event.getY();

        if (action == MotionEvent.ACTION_DOWN) {
            if (y > bottom) { //below playing area
                if (x < leftbtn) { //left
                    moveship = -shipmove;  //and yet this seems to be dpi independent, I think... why?
                } else if (x < firebtn) { //fire
                    tofire = true;
                } else if (x <= rightbtn) {
                    moveship = shipmove;
                }
                return true;
            }
        } else if (action == MotionEvent.ACTION_UP) { //stop moving left or right.  user has lifted their finger.
            performClick();
            if (y > bottom) { //below playing area
                if (x < leftbtn) { //left
                    moveship = 0;
                } else if (x < firebtn) { //don't care about fire, handled in down.
                    return true;
                } else if (x <= rightbtn) {
                    moveship = 0;
                }
                return true;
            }

        }
        return false;
    }


    /*
     *  Three surface methods to override.  created, changed, and destroyed.
     *  setup on create, do nothing in changed, and shut it all down in destroyed.
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.v(TAG, "surfaceCreated ");
        // setup the background picture.
        Log.v(TAG, "size is width=" + getWidth() + " height is " + getHeight());
        setupBG(getWidth(), getHeight());

        thread = new myThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.v(TAG, "surfaceChanged ");
        Log.v(TAG, "size is width=" + width + " height is " + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.v(TAG, "surfaceDestroyed");
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

    /*
     *  basic thread to do the drawing that is started in SurfaceView Created and ended in destroyed.
     *  most of the code is above surfaceview methods, such as draw, and checkgamestate.
     */
    private class myThread extends Thread {
        private final SurfaceHolder _surfaceHolder;
        private mySurfaceView _mySurfaceView;
        private boolean running = false;

        private myThread(SurfaceHolder surfaceHolder, mySurfaceView SurfaceView) {
            _surfaceHolder = surfaceHolder;
            _mySurfaceView = SurfaceView;
        }

        void setRunning(boolean run) {
            running = run;
        }

        @Override
        public void run() {
            Canvas c;
            gameover = false;
            while (running) {

                if (!gameover) {
                    //verify game state, move objects, etc...
                    checkGameState();
                }


                c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    if (!running || c == null) return; //just in case checkGameState took a while.
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
                if (!running) return; //no need to sleep, we are done.
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
