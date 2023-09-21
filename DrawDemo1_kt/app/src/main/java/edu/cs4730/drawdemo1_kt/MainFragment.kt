package edu.cs4730.drawdemo1_kt

import android.annotation.SuppressLint
import android.graphics.*
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View.OnTouchListener
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import edu.cs4730.drawdemo1_kt.databinding.FragmentMainBinding

/**
 * This is a simple example to show how each of the canvas drawing works.
 *
 */
class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    lateinit var theboard: Bitmap
    lateinit var theboardc: Canvas
    var which = 1
    val boardsize = 480

    //for drawing
    var firstx = 0f
    var firsty = 0f
    var first = true
    var myRecF = RectF()
    var myColor: Paint? = null
    var myColorList = ColorList()
    lateinit var alien: Bitmap

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)

        //Simple clear button, reset the image to white.
        binding.clear.setOnClickListener {
            theboardc.drawColor(Color.WHITE) //background color for the board.
            refreshBmp()
        }

        //changes to the next color in the list
        binding.next.setOnClickListener {
            myColorList.next()
            myColor!!.color = myColorList.getNum()
        }

        //setup the spinner
        val list = arrayOf("Point", "Line", "Rect", "Circle", "Arc", "Oval", "Pic", "Text")
        //first we will work on the spinner1 (which controls the seekbar)
        //create the ArrayAdapter of strings from my List.
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list)
        //set the dropdown layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //finally set the adapter to the spinner
        binding.spinner.adapter = adapter
        //set the selected listener as well
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                which = position
                first = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                which = 1 //point
                first = true
            }
        }


        //get the imageview and create a bitmap to put in the imageview.
        //also create the canvas to draw on.
        theboard = Bitmap.createBitmap(boardsize, boardsize, Bitmap.Config.ARGB_8888)
        theboardc = Canvas(theboard)
        theboardc.drawColor(Color.WHITE) //background color for the board.
        binding.boardfield.setImageBitmap(theboard)
        binding.boardfield.invalidate()
        binding.boardfield.setOnTouchListener(myTouchListener())

        //For drawing
        myColor = Paint() //default black
        myColor!!.color = myColorList.getNum()
        myColor!!.style = Paint.Style.FILL
        myColor!!.strokeWidth = 10f
        myColor!!.textSize = myColor!!.textSize * 4

        //load a picture and draw it onto the screen.
        alien = BitmapFactory.decodeResource(resources, R.drawable.alien)
        //draw it on the screen.
        //theboardc.drawBitmap(alien, null, new Rect(0, 0, 300, 300), myColor);
        return binding.root
    }

    /*
    * TouchListener will draw a square on the image where "touched".
    * If doing an animated clear, it will return without doing anything.
    */
    internal inner class myTouchListener : OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {

            //We just need the x and y position, to draw on the canvas
            //so, retrieve the new x and y touch positions
            if (event.action == MotionEvent.ACTION_UP) { //fake it for tap.
                drawBmp(event.x, event.y)
                v.performClick()
                return true
            }
            return false
        }
    }

    fun drawBmp(x: Float, y: Float) {
        //"Point", "Line", "Rect", "Circle", "Arc", "Oval", "Pic", "Text"
        when (which) {
            0 -> theboardc.drawPoint(x, y, myColor!!)
            1 -> if (first) { //store the point for later
                firstx = x
                firsty = y
                first = false
            } else {
                theboardc.drawLine(firstx, firsty, x, y, myColor!!)
                first = true
            }
            2 -> if (first) { //store the point for later
                myRecF.top = y
                myRecF.left = x
                first = false
            } else {
                myRecF.bottom = y
                myRecF.right = x
                theboardc.drawRect(myRecF, myColor!!)
                first = true
            }
            3 -> theboardc.drawCircle(x, y, 20.0f, myColor!!)
            4 -> if (first) { //store the point for later
                myRecF.top = y
                myRecF.left = x
                first = false
            } else {
                myRecF.bottom = y
                myRecF.right = x
                theboardc.drawArc(myRecF, 0.0f, 45.0f, true, myColor!!)
                first = true
            }
            5 -> if (first) { //store the point for later
                myRecF.top = y
                myRecF.left = x
                first = false
            } else {
                myRecF.bottom = y
                myRecF.right = x
                theboardc.drawOval(myRecF, myColor!!)
                first = true
            }
            6 -> theboardc.drawBitmap(alien, x, y, myColor)
            7 -> theboardc.drawText("Hi there", x, y, myColor!!)
            else -> Log.v("hi", "NOT working? $which")
        }
        binding.boardfield.setImageBitmap(theboard)
        binding.boardfield.invalidate()
    }

    fun refreshBmp() {
        binding.boardfield.setImageBitmap(theboard)
        binding.boardfield.invalidate()
    }
}