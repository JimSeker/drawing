package edu.cs4730.graphicoverlaydemo_kt

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * First attempt at a "graphic Overlay" class.  It should be set over another
 * Widget, using a relativeLayout.
 */
class GraphicOverLayDraw : View {
    var mContext: Context
    var mheight = 0
    var mwidth = 0
    lateinit var myColor: Paint
    var mx = -1.0f
    var my = 1.0f
    lateinit var myPath: Path

    constructor(c: Context) : super(c) {
        mContext = c
        setup()
    }

    constructor(c: Context, attrs: AttributeSet?) : super(c, attrs) {
        mContext = c
        setup()
    }

    constructor(c: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        c,
        attrs,
        defStyleAttr
    ) {
        mContext = c
        setup()
    }

    protected fun setup() {
        myColor = Paint()
        myColor.color = Color.RED
        myColor.style = Paint.Style.STROKE
        myColor.strokeWidth = 10f
        myPath = Path()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        // Retrieve the new x and y touch positions
        mx = event.x
        my = event.y
        when (action) {
            MotionEvent.ACTION_DOWN -> myPath.moveTo(mx, my)
            MotionEvent.ACTION_MOVE -> myPath.lineTo(mx, my)
            MotionEvent.ACTION_UP -> {
                myPath.lineTo(mx, my)
                performClick()
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /*
        if (mx >= 0.0 && my >= 0.0) {
            canvas.drawPoint(x, y, myColor);
        }
        */canvas.drawPath(myPath, myColor)
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
        mwidth = measuredWidth
        mheight = measuredHeight
        if (mheight > 0 && mwidth > mheight) {
            mwidth = mheight
        } else if (mheight == 0) {
            mheight = mwidth
        }
        //setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        setMeasuredDimension(mwidth, mheight)
    }
}