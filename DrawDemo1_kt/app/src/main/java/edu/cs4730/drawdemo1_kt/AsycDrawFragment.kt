package edu.cs4730.drawdemo1_kt

import android.annotation.SuppressLint
import android.graphics.*
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import edu.cs4730.drawdemo1_kt.databinding.FragmentAsycdrawBinding

/**
 * This uses a async method to animate the clear.  verses a thread clear in the AnDrawFragment.
 * Note, yes this class (and this part of example) is Deprecated, I'll leave it until it's removed
 */
class AsycDrawFragment : Fragment() {
    lateinit var binding: FragmentAsycdrawBinding

    var theboard: Bitmap? = null
    var theboardc: Canvas? = null

    var which = 1
    val boardsize = 480

    //for drawing
    var firstx = 0f
    var firsty = 0f
    var first = true
    var myRecF = RectF()
    var myColor: Paint = Paint()
    var myColorList = ColorList()
    lateinit var alien: Bitmap
    var isAnimation = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAsycdrawBinding.inflate(inflater, container, false);

        //Simple clear button, reset the image to white.
        binding.clear.setOnClickListener {
            isAnimation = true
            val task: animator = animator()
            task.execute(0)
        }

        //changes to the next color in the list
        binding.next.setOnClickListener {
            myColorList.next()
            myColor.color = myColorList.getNum()
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
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
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
        theboardc = Canvas(theboard!!)
        theboardc!!.drawColor(Color.WHITE) //background color for the board.
        binding.boardfield.setImageBitmap(theboard)
        binding.boardfield.setOnTouchListener(myTouchListener())

        //For drawing
        //myColor = Paint() //default black
        myColor.color = myColorList.getNum()
        myColor.style = Paint.Style.FILL
        myColor.strokeWidth = 10f
        myColor.textSize *= 4

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

            //Don't draw if there is animation going on.
            if (isAnimation) return true //but say we handled (ignored) the event.

            //We just need the x and y position, to draw on the canvas
            //so, retrieve the new x and y touch positions
            if (event.action == MotionEvent.ACTION_UP) { //fake it for tap.
                v.performClick()
                drawBmp(event.x, event.y)
                return true
            }
            return false
        }
    }

    fun drawBmp(x: Float, y: Float) {
        //"Point", "Line", "Rect", "Circle", "Arc", "Oval", "Pic", "Text"
        when (which) {
            0 -> theboardc!!.drawPoint(x, y, myColor)
            1 -> if (first) { //store the point for later
                firstx = x
                firsty = y
                first = false
            } else {
                theboardc!!.drawLine(firstx, firsty, x, y, myColor)
                first = true
            }

            2 -> if (first) { //store the point for later
                myRecF.top = y
                myRecF.left = x
                first = false
            } else {
                myRecF.bottom = y
                myRecF.right = x
                theboardc!!.drawRect(myRecF, myColor)
                first = true
            }

            3 -> theboardc!!.drawCircle(x, y, 20.0f, myColor)
            4 -> if (first) { //store the point for later
                myRecF.top = y
                myRecF.left = x
                first = false
            } else {
                myRecF.bottom = y
                myRecF.right = x
                theboardc!!.drawArc(myRecF, 0.0f, 45.0f, true, myColor)
                first = true
            }

            5 -> if (first) { //store the point for later
                myRecF.top = y
                myRecF.left = x
                first = false
            } else {
                myRecF.bottom = y
                myRecF.right = x
                theboardc!!.drawOval(myRecF, myColor)
                first = true
            }

            6 -> theboardc!!.drawBitmap(alien, x, y, myColor)
            7 -> theboardc!!.drawText("Hi there", x, y, myColor)
            else -> Log.v("hi", "NOT working? $which")
        }
        binding.boardfield.setImageBitmap(theboard)
        binding.boardfield.invalidate()
    }

    fun refreshBmp() {
        binding.boardfield.setImageBitmap(theboard)
        binding.boardfield.invalidate()
    }

    /* (non-Javadoc)
 * @see android.app.Activity#finish()
 */
    fun finish() {
        isAnimation = false
    }

    /*
    * Note, yes this class (and this part of example) is Deprecated, I'll leave it until it's removed
    *
    * this is an thread, so it can run the "animated" clear button"
    * So it draws a line with the current color down the image, and then white back up to clear
    *
    * Since this is a sub class, it has access to all the "global" variables.  But it's a thread
    * so it can't change the widgets.  That's what the handler does.  When a new image is ready
    * it sends a message to the handler that is on the main thread.  The handler then updates the
    * imageview with the bitmap image.
     */
    internal inner class animator : AsyncTask<Int?, Int?, Int>() {

        protected override fun doInBackground(vararg params: Int?): Int {
            var done = false
            val block = boardsize / 10
            var x = 0
            var i = 0
            //first draw a red down the board
            //myColor.setColor(Color.RED);//use current color instead of red.
            while (!done) {
                if (!isAnimation) {
                    return 100
                } //To stop the thread, if the system is paused or killed.
                //clear part of the board
                /*
            for (i=0; i<=block; i++) {
			  theboardc.drawLine(0, x+i, boardsize, x+i, myColor);
			}
			x += block;
			*/theboardc!!.drawLine(0f, x.toFloat(), boardsize.toFloat(), x.toFloat(), myColor!!)
                x++

                //send message to redraw
                publishProgress(1)
                //now wait a little bit.
                try {
                    Thread.sleep(10) //change to 100 for a commented out block, instead of just a line.
                } catch (e: InterruptedException) {
                    //don't care
                }
                //determine if we are done or move the x?
                if (x >= boardsize) done = true
            }
            //now draw white back up the board
            x = boardsize - block
            done = false
            myColor.color = Color.WHITE
            while (!done) {
                if (!isAnimation) {
                    return 10
                } //To stop the thread, if the system is paused or killed.
                //clear part of the board
                i = 0
                while (i <= block) {
                    theboardc!!.drawLine(
                        0f,
                        (x + i).toFloat(),
                        boardsize.toFloat(),
                        (x + i).toFloat(),
                        myColor
                    )
                    i++
                }
                x -= block
                //send message to redraw
                publishProgress(1)
                //now wait a little bit.
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    //don't care
                }
                //determine if we are done or move the x?
                if (x < 0) done = true
            }
            //now draw white back up the board
            myColor.color = myColorList.num
            isAnimation = false
            return 100
        }

        override fun onProgressUpdate(vararg progress: Int?) {
            refreshBmp()
        }

        override fun onPostExecute(result: Int) {
            refreshBmp()
        }

    }
}