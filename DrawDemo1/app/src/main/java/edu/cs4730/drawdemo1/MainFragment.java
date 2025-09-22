package edu.cs4730.drawdemo1;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import edu.cs4730.drawdemo1.databinding.FragmentMainBinding;


/**
 * This is a simple example to show how each of the canvas drawing works.
 */
public class MainFragment extends Fragment {

    FragmentMainBinding binding;
    Bitmap theboard;
    Canvas theboardc;
    //Button btnClear, btnNColor;
    //Spinner mySpinner;
    int which = 1;
    final int boardsize = 480;
    //for drawing
    float firstx, firsty;
    boolean first = true;
    RectF myRecF = new RectF();
    Paint myColor;
    ColorList myColorList = new ColorList();
    Bitmap alien;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false);

        //Simple clear button, reset the image to white.
        binding.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theboardc.drawColor(Color.WHITE);  //background color for the board.
                refreshBmp();
            }
        });

        //changes to the next color in the list
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myColorList.next();
                myColor.setColor(myColorList.getNum());
            }
        });

        //setup the spinner
        String[] list = {"Point", "Line", "Rect", "Circle", "Arc", "Oval", "Pic", "Text"};
        //first we will work on the spinner1 (which controls the seekbar)
        //create the ArrayAdapter of strings from my List.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, list);
        //set the dropdown layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //finally set the adapter to the spinner
        binding.spinner.setAdapter(adapter);
        //set the selected listener as well
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        theboard = Bitmap.createBitmap(boardsize, boardsize, Bitmap.Config.ARGB_8888);
        theboardc = new Canvas(theboard);
        theboardc.drawColor(Color.WHITE);  //background color for the board.
        binding.boardfield.setImageBitmap(theboard);

        binding.boardfield.setOnTouchListener(new myTouchListener());

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
        return binding.getRoot();
    }

    /*
     * TouchListener will draw a square on the image where "touched".
     * If doing an animated clear, it will return without doing anything.
     */
    class myTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.performClick();
            //We just need the x and y position, to draw on the canvas
            //so, retrieve the new x and y touch positions
            if (event.getAction() == MotionEvent.ACTION_UP) { //fake it for tap.
                v.performClick();
                drawBmp((int) event.getX(), (int) event.getY());
                return true;
            }
            return false;
        }
    }


    void drawBmp(float x, float y) {
        //"Point", "Line", "Rect", "Circle", "Arc", "Oval", "Pic", "Text"
        switch (which) {
            case 0:   //draw a point.

                theboardc.drawPoint(x, y, myColor);
                break;
            case 1: //line
                if (first) { //store the point for later
                    firstx = x;
                    firsty = y;
                    first = false;
                } else {
                    theboardc.drawLine(firstx, firsty, x, y, myColor);
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
                    theboardc.drawRect(myRecF, myColor);
                    first = true;
                }
                break;
            case 3: //circle
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
                    theboardc.drawArc(myRecF, 0.0f, 45.0f, true, myColor);
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
                    theboardc.drawOval(myRecF, myColor);
                    first = true;
                }
                break;
            case 6: //"Pic"
                theboardc.drawBitmap(alien, x, y, myColor);
                break;
            case 7: // "Text"
                theboardc.drawText("Hi there", x, y, myColor);
                break;
            default:
                Log.v("hi", "NOT working? " + which);
        }
        binding.boardfield.setImageBitmap(theboard);
        binding.boardfield.invalidate();
    }

    void refreshBmp() {
        binding.boardfield.setImageBitmap(theboard);
        binding.boardfield.invalidate();
    }

}
