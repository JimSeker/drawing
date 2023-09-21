package edu.cs4730.drawdemo1;

import androidx.annotation.NonNull;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

/**
 * This uses an animated clear button, instead of just clearing it.
 */
public class AnDrawFragment extends Fragment {
    ImageView theboardfield;
    Bitmap theboard;
    Canvas theboardc;
    Button btnClear, btnNColor;
    Spinner mySpinner;
    int which = 1;
    final int boardsize = 480;
    //for drawing
    float firstx, firsty;
    boolean first = true;
    RectF myRecF = new RectF();
    Paint myColor;
    ColorList myColorList = new ColorList();
    Bitmap alien;

    //for the animation.
    boolean isAnimation = false;
    protected Handler handler;
    //for the thread
    Thread myThread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_andraw, container, false);

        //Simple clear button, reset the image to white.
        btnClear = myView.findViewById(R.id.clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startThread();  //done to simply a few things.
            }
        });

        //changes to the next color in the list
        btnNColor = myView.findViewById(R.id.next);
        btnNColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myColorList.next();
                myColor.setColor(myColorList.getNum());
            }
        });

        //setup the spinner
        String[] list = {"Point", "Line", "Rect", "Circle", "Arc", "Oval", "Pic", "Text"};
        //first we will work on the spinner1 (which controls the seekbar)
        mySpinner = (Spinner) myView.findViewById(R.id.spinner);
        //create the ArrayAdapter of strings from my List.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, list);
        //set the dropdown layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //finally set the adapter to the spinner
        mySpinner.setAdapter(adapter);
        //set the selected listener as well
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                which = position;
                first = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                which = 1; //point
                first = true;
            }
        });


        //get the imageview and create a bitmap to put in the imageview.
        //also create the canvas to draw on.
        theboardfield = (ImageView) myView.findViewById(R.id.boardfield);
        theboard = Bitmap.createBitmap(boardsize, boardsize, Bitmap.Config.ARGB_8888);
        theboardc = new Canvas(theboard);
        theboardc.drawColor(Color.WHITE);  //background color for the board.
        theboardfield.setImageBitmap(theboard);
        theboardfield.setOnTouchListener(new myTouchListener());

        //For drawing

        myColor = new Paint();  //default black
        myColor.setColor(myColorList.getNum());
        myColor.setStyle(Paint.Style.FILL);
        myColor.setStrokeWidth(10);
        myColor.setTextSize(myColor.getTextSize() * 4);

        //load a picture and draw it onto the screen.
        alien = BitmapFactory.decodeResource(getResources(), R.drawable.alien);

        //message handler for the animation.
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 0) { //redraw image
                    if (theboard != null && theboardfield != null) {
                        refreshBmp();
                    }
                }
            }
        };
        //draw it on the screen.
        //theboardc.drawBitmap(alien, null, new Rect(0, 0, 300, 300), myColor);
        return myView;
    }

    /*
     * TouchListener will draw a square on the image where "touched".
     * If doing an animated clear, it will return without doing anything.
     */
    public class myTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            //Don't draw if there is animation going on.
            if (isAnimation) return true;  //but say we handled (ignored) the event.

            //We just need the x and y position, to draw on the canvas
            //so, retrieve the new x and y touch positions
            if (event.getAction() == MotionEvent.ACTION_UP) { //fake it for tap.
                drawBmp((int) event.getX(), (int) event.getY(), MotionEvent.ACTION_UP);
                v.performClick();  //this should be called only ACTION_UP, but can be called for others.
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) { //fake it for tap.

                drawBmp((int) event.getX(), (int) event.getY(), MotionEvent.ACTION_MOVE);
                return true;
            }
            return false;
        }
    }

    void drawBmp(float x, float y, int eventcode) {
        //"Point", "Line", "Rect", "Circle", "Arc", "Oval", "Pic", "Text"
        switch (which) {
            case 0:   //draw a point.
                theboardc.drawColor(Color.WHITE);  //background color for the board.
                theboardc.drawPoint(x, y, myColor);
                break;
            case 1: //line
                if (first) { //store the point for later
                    firstx = x;
                    firsty = y;
                    first = false;
                } else {
                    theboardc.drawColor(Color.WHITE);  //background color for the board.
                    theboardc.drawLine(firstx, firsty, x, y, myColor);
                    if (eventcode == MotionEvent.ACTION_UP)
                        first = true;
                }
                break;
            case 2: //rectangle
                if (first) { //store the point for later
                    myRecF.top = y;
                    myRecF.left = x;

                    first = false;
                } else {

                    myRecF.bottom = y;
                    myRecF.right = x;
                    theboardc.drawColor(Color.WHITE);  //background color for the board.
                    theboardc.drawRect(myRecF, myColor);
                    if (eventcode == MotionEvent.ACTION_UP)
                        first = true;
                }
                break;
            case 3: //circle
                theboardc.drawColor(Color.WHITE);  //background color for the board.
                theboardc.drawCircle(x, y, 20.0f, myColor);
                break;
            case 4: //arc
                if (first) { //store the point for later
                    myRecF.top = y;
                    myRecF.left = x;

                    first = false;
                } else {

                    myRecF.bottom = y;
                    myRecF.right = x;
                    theboardc.drawColor(Color.WHITE);  //background color for the board.
                    theboardc.drawArc(myRecF, 0.0f, 45.0f, true, myColor);
                    if (eventcode == MotionEvent.ACTION_UP)
                        first = true;
                }
                break;
            case 5: //oval
                if (first) { //store the point for later
                    myRecF.top = y;
                    myRecF.left = x;

                    first = false;
                } else {

                    myRecF.bottom = y;
                    myRecF.right = x;
                    theboardc.drawColor(Color.WHITE);  //background color for the board.
                    theboardc.drawOval(myRecF, myColor);
                    if (eventcode == MotionEvent.ACTION_UP)
                        first = true;
                }
                break;
            case 6: //"Pic"
                theboardc.drawColor(Color.WHITE);  //background color for the board.
                theboardc.drawBitmap(alien, x, y, myColor);
                break;
            case 7: // "Text"
                theboardc.drawColor(Color.WHITE);  //background color for the board.
                theboardc.drawText("Hi there", x, y, myColor);
                break;
            default:
                Log.v("hi", "NOT working? " + which);


        }

        theboardfield.setImageBitmap(theboard);
        theboardfield.invalidate();
    }

    void refreshBmp() {
        theboardfield.setImageBitmap(theboard);
        theboardfield.invalidate();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    public void onPause() {
        finish();
        super.onPause();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#finish()
     */
    public void finish() {
        //first make sure the thread is stopped, to prevent a force close.
        if (myThread != null) {
            isAnimation = false;
            try {
                if (myThread.isAlive()) {
                    myThread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myThread = null;
        }
        theboard = null;
        theboardc = null;
        myColor = null;
        handler = null;
//		super.finish();
    }


    /*
     * Starts up a new thread to run some animation.
     */
    public void startThread() {
        isAnimation = true;
        //new Thread(this).start();
        myThread = new Thread(new animator());
        myThread.start();
    }

    /**
     * this is an thread, so it can run the "animated" clear button"
     * So it draws a line with the current color down the image, and then white back up to clear
     * <p>
     * Since this is a sub class, it has access to all the "global" variables.  But it's a thread
     * so it can't change the widgets.  That's what the handler does.  When a new image is ready
     * it sends a message to the handler that is on the main thread.  The handler then updates the
     * imageview with the bitmap image.
     */
    class animator implements Runnable {
        animator() {
            //constructor...
        }

        @Override
        public void run() {
            boolean done = false;
            int block = boardsize / 10;
            int x = 0, i = 0;
            //first draw a red down the board
            //myColor.setColor(Color.RED);//use current color instead of red.
            while (!done) {
                if (!isAnimation) {
                    return;
                }  //To stop the thread, if the system is paused or killed.
                //clear part of the board
            /*
            for (i=0; i<=block; i++) {
			  theboardc.drawLine(0, x+i, boardsize, x+i, myColor);
			}
			x += block;
			*/
                theboardc.drawLine(0, x, boardsize, x, myColor);
                x++;

                //send message to redraw
                handler.sendEmptyMessage(0);
                //now wait a little bit.
                try {
                    Thread.sleep(10);  //change to 100 for a commented out block, instead of just a line.
                } catch (InterruptedException e) {
                    //don't care
                }
                //determine if we are done or move the x?
                if (x >= boardsize) done = true;
            }
            //now draw white back up the board
            x = boardsize - block;
            done = false;
            myColor.setColor(Color.WHITE);
            while (!done) {
                if (!isAnimation) {
                    return;
                }  //To stop the thread, if the system is paused or killed.
                //clear part of the board
                for (i = 0; i <= block; i++) {
                    theboardc.drawLine(0, x + i, boardsize, x + i, myColor);
                }
                x -= block;
                //send message to redraw
                handler.sendEmptyMessage(0);
                //now wait a little bit.
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    //don't care
                }
                //determine if we are done or move the x?
                if (x < 0) done = true;
            }
            //now draw white back up the board

            myColor.setColor(myColorList.getNum());
            isAnimation = false;
        }
    }

}
