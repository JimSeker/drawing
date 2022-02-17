package edu.cs4730.graphicoverlaydemo_kt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * First attempt at a "graphic Overlay" class.  It should be set over another
 * Widget, using a relativeLayout.
 */
public class GraphicOverLayDraw extends View {

    Context context;
    int mheight = 0, mwidth = 0;
    Paint myColor;
    float x = -1.0f, y = 1.0f;
    Path myPath;

    public GraphicOverLayDraw(Context context) {
        super(context);
        this.context = context;
        setup();
    }

    public GraphicOverLayDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setup();
    }

    public GraphicOverLayDraw(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setup();
    }

    protected void setup() {
        myColor = new Paint();
        myColor.setColor(Color.RED);
        myColor.setStyle(Paint.Style.STROKE);
        myColor.setStrokeWidth(10);
        myPath = new Path();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        // Retrieve the new x and y touch positions
        x = event.getX();
        y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                myPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                myPath.lineTo(x,y);
                break;
            case MotionEvent.ACTION_UP:
                myPath.lineTo(x,y);
                performClick();
                break;
        }

        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*
        if (x >= 0.0 && y >= 0.0) {
            canvas.drawPoint(x, y, myColor);
        }
        */
        canvas.drawPath(myPath,myColor);
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
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mwidth = getMeasuredWidth();
        mheight = getMeasuredHeight();
        if (mheight > 0 && mwidth > mheight) {
            mwidth = mheight;
        } else if (mheight == 0) {
            mheight = mwidth;
        }


        //setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        setMeasuredDimension(mwidth, mheight);

    }
}
