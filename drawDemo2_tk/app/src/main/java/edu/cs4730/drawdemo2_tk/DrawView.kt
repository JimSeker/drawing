package edu.cs4730.drawdemo2_tk

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View


/**
 * This is a demo of a customview. it draws 5 circles, which the user
 * can click on to recolor.  I'm ignoring some things like
 * all the xml attributes that could be set.
 */
class DrawView : View {
    lateinit var black: Paint
    lateinit var other: Paint
    var size = 5
    var mheight = 0
    var mwidth = 0
    var myContext: Context
    var myCircles = arrayOfNulls<circle>(5)

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
        black.color = Color.BLACK //defaults to black, but just making sure.
        black.style = Paint.Style.STROKE
        other = Paint() //I'll set  this one as I needed it.
        other.style = Paint.Style.FILL
        if (mwidth == 0) return  //something isn't setup yet.
        val radius = mwidth / 5f
        //top left.
        myCircles[0] = circle(radius, radius, radius)

        //top right
        myCircles[1] = circle(mwidth - radius, radius, radius)
        //middle
        myCircles[2] = circle()
        myCircles[2]!!.r = radius
        myCircles[2]!!.x = mwidth / 2f
        myCircles[2]!!.y = mheight / 2f

        //top left.\
        myCircles[3] = circle()
        myCircles[3]!!.r = radius
        myCircles[3]!!.x = radius
        myCircles[3]!!.y = mheight - radius
        //top right
        myCircles[4] = circle()
        myCircles[4]!!.r = radius
        myCircles[4]!!.x = mwidth - radius
        myCircles[4]!!.y = mheight - radius
    }

    /*
     * clears the grid and then has the view redraw.
     */
    fun clearmaze() {
        for (i in 0 until size) {
            myCircles[i]!!.color = Color.BLACK
        }
        invalidate()
    }

    /**
     * (non-Javadoc)
     * @see android.view.View.onDraw
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        if (mwidth == 0) return
        for (i in 0 until size) {
            other.color = myCircles[i]!!.color
            canvas.drawCircle(
                myCircles[i]!!.x, myCircles[i]!!.y, myCircles[i]!!.r,
                other
            )
        }
    }

    fun where(x: Int, y: Int, color: Int): Boolean {
        var deltaX: Float
        var deltaY: Float
        var distance: Double
        var collided: Boolean
        for (i in 0 until size) {
            deltaX = myCircles[i]!!.x - x
            deltaY = myCircles[i]!!.y - y
            distance = Math.sqrt((deltaX * deltaX + deltaY * deltaY).toDouble())
            collided = distance <= myCircles[i]!!.r
            if (collided) {
                myCircles[i]!!.color = color
                return true
            }
        }
        return false
    }

    /*
     * (non-Javadoc)
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     *
     * overrode this event to get all the touch events for this view.
     *
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        // Retrieve the new x and y touch positions
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                Log.i("onTouchEvent", "Action_down")
                where(x, y, Color.BLUE)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                where(x, y, Color.YELLOW)
                invalidate()
            }
            MotionEvent.ACTION_UP -> performClick()
        }
        return true
    }

    /**
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
            mwidth = mheight //make it square.
        } else if (mheight == 0) {
            mheight = mwidth
        } else if (mheight > mwidth) {
            mheight = mwidth //make it square.
        }
        Log.i("MSW", "" + mwidth)
        Log.i("MSH", "" + mheight)
        //setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        setMeasuredDimension(mwidth, mheight)
        setup()
    }

    inner class circle {
        var x: Float
        var y: Float
        var r: Float
        var color: Int

        constructor(mx: Float, my: Float, mr: Float) {
            x = mx
            y = my
            r = mr
            color = Color.BLACK
        }

        constructor() {
            x = 0f
            y = 0f
            r = 0f
            color = Color.BLACK
        }
    }
}
