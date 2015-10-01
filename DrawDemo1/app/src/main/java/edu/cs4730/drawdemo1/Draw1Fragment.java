package edu.cs4730.drawdemo1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


/**
 * Only draws one ojbect to the image at time and ones where two taps are needed, with animate
 * the drawing of the object, until the finger is lifted on the seoncd tap.
 */
public class Draw1Fragment extends Fragment {
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


    public Draw1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_draw1, container, false);

        //Simple clear button, reset the image to white.
        btnClear = (Button) myView.findViewById(R.id.button2);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theboardc.drawColor(Color.WHITE);  //background color for the board.
                refreshBmp();
            }
        });

        //changes to the next color in the list
        btnNColor = (Button) myView.findViewById(R.id.button3);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
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
        //draw it on the screen.
        //theboardc.drawBitmap(alien, null, new Rect(0, 0, 300, 300), myColor);
        return myView;
    }

    /*
    * TouchListener will draw a square on the image where "touched".
    * If doing an animated clear, it will return without doing anything.
    */
    class myTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            //We just need the x and y position, to draw on the canvas
            //so, retrieve the new x and y touch positions
            if (event.getAction() == MotionEvent.ACTION_UP) { //fake it for tap.
                drawBmp((int) event.getX(), (int) event.getY(), MotionEvent.ACTION_UP);

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


}
