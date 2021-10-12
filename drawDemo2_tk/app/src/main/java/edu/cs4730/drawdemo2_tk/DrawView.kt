package edu.cs4730.drawdemo2_tk

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * This is a demo of a customview.  I'm ignoring some things like
 * all the xml attributes that could be set.
 */
class DrawView : View {
    var black: Paint? = null
    var other: Paint? = null
    var incr = 0
    var size = 8
    var mheight = 0
    var mwidth = 0
    var leftside = 0
    var rightside = 0
    var boardwidth = 0
    var maze: Array<IntArray>? = null
    var myContext: Context

    /*
	 * default constructor
	 */
    constructor(context: Context) : super(context) {
        myContext = context
        setup()
    }

    /*
	 * should be the constructor that I'm calling!
	 */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        myContext = context
        setup()
    }

    /*
	 * no clue on this constructor, it came with the example.
	 */
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        myContext = context
        setup()
    }

    /*
	 * Setups all the default variables.  Since I'm not sure which of the constructors is
	 * called, I'm using this method and have all of them call it.
	 */
    fun setup() {
        black = Paint()
        black!!.color = Color.BLACK //defaults to black, but just making sure.
        black!!.style = Paint.Style.STROKE
        other = Paint() //I'll set  this one as I needed it.
        other!!.style = Paint.Style.FILL
        if (maze != null) {
            maze = null
        }
        maze = Array(size) { IntArray(size) }
        for (i in 0 until size) {
            for (j in 0 until size) {
                maze!![i][j] = Color.BLACK
            }
        }
        if (mheight > 0) setsizes() //in case not on screen.
    }

    /*
	 * Setups up the default sizes for whatever the size of the screen is
	 * so that the grid takes up most of the space with a margin around it.
	 * 
	 */
    fun setsizes() {
        incr = mwidth / (size + 2) //give a margin.
        leftside = incr - 1
        rightside = incr * 9
        boardwidth = incr * size
        Log.i("setsizes", "incr is $incr")
    }

    /*
	 * clears the grid and then has the view redraw.
	 */
    fun clearmaze() {
        for (i in 0 until size) {
            for (j in 0 until size) {
                maze!![i][j] = Color.BLACK
            }
        }
        invalidate()
    }

    /*
	 * (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 *
	 * This is one of two methods I overrode.   The onDraw method
	 * is the one that will draw everything for this view.  
	 * in this case, an 8x8 grid on the screen.
	 * 	 
	 */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var x = incr
        var y = incr
        canvas.drawColor(Color.WHITE)
        //top line  not needed anymore.
        //canvas.drawLine(x, y -1, rightside, y-1, black);
        //draw squares across, then down
        for (yi in 0 until size) {
            for (xi in 0 until size) {
                canvas.drawRect(
                    x.toFloat(),
                    y.toFloat(),
                    (x + incr).toFloat(),
                    (y + incr).toFloat(),
                    black!!
                ) //draw black box.
                if (maze!![xi][yi] != Color.BLACK) {
                    Log.i("onDraw", "Drawing different color")
                    other!!.color = maze!![xi][yi]
                    canvas.drawRect(
                        (x + 1).toFloat(),
                        (y + 1).toFloat(),
                        (x + incr).toFloat(),
                        (y + incr).toFloat(),
                        other!!
                    )
                }
                x += incr //move to next square across
            }
            x = incr
            y += incr
        }
        //bottom line  not needed anymore.
        //canvas.drawLine(leftside, rightside, rightside, rightside,black);
    }

    /*
	 * used by the ontouch event to figure out which box (if any) was "touched"
	 * 
	 */
    fun where(x: Int, y: Int, color: Int, recolor: Boolean): Boolean {
        var x = x
        var y = y
        var cx = -1
        var cy = -1
        if (y >= leftside && y < rightside && x >= leftside && x < rightside) {
            y -= incr
            x -= incr //simplifies the math here.
            cx = x / incr
            cy = y / incr
            if (cx < size && cy < size) {
                if (maze!![cx][cy] == Color.BLACK) {
                    maze!![cx][cy] = color
                } else if (recolor) maze!![cx][cy] = color
            } else {
                Log.i("where", "Error in Where, cx=$cx cy=$cy")
            }
            return true
        }

        /* 
         * If outside the lines, then popup a dialog and ask about reseting the board.
         * Interestingly, nothing is deprecated in the view
         */
        var dialog: Dialog? = null
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(myContext)
        builder.setMessage("Reset board?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id -> clearmaze() }
            .setNegativeButton("No") { dialog, id -> dialog.cancel() }
        dialog = builder.create()
        dialog.show()
        return false
    }

    /*
     * (non-Javadoc)
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     * 
     * overrode this event to get all the touch events for this view.
     * 
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        // Retrieve the new x and y touch positions
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                Log.i("onTouchEvent", "Action_down")
                where(x, y, Color.BLUE, true)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                where(x, y, Color.YELLOW, false)
                invalidate()
            }
        }
        return true
    }

    /*
	 * (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 * 
	 * 
	 * using this to get the size of the view.
	 * now... I should likely set the height if the
	 * view is using wrapcontent instead matchparent
	 * ie likely it comes back with a zero height or width!
	 * and then nothing will be draw (no call to onDraw either).
	 */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.i("MSW", "" + measuredWidth)
        Log.i("MSH", "" + measuredHeight)
        mwidth = measuredWidth
        mheight = measuredHeight
        if (mheight > 0 && mwidth > mheight) {
            mwidth = mheight
        } else if (mheight == 0) {
            mheight = mwidth
        }
        setsizes()

        //setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        setMeasuredDimension(mwidth, mheight)
    }
}