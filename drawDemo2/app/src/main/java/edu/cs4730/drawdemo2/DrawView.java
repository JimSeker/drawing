package edu.cs4730.drawdemo2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * This is a demo of a customview. it draws 5 circles, which the user
 * can click on to recolor.  I'm ignoring some things like
 * all the xml attributes that could be set.
 */
public class DrawView extends View {


    Paint black, other;
    public int  size = 5;
    int mheight = 0, mwidth = 0;
    public Context myContext;

    circle[] myCircles = new circle[5];

    /*
     * default constructor
     */
    public DrawView(Context context) {
        super(context);
        myContext = context;
        setup();
    }

    /*
     * should be the constructor that I'm calling!
     */
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        myContext = context;
        setup();
    }

    /*
     * no clue on this constructor, it came with the example.
     */
    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        myContext = context;
        setup();
    }

    /*
     * Setups all the default variables.  Since I'm not sure which of the constructors is
     * called, I'm using this method and have all of them call it.
     */
    public void setup() {

        black = new Paint();
        black.setColor(Color.BLACK); //defaults to black, but just making sure.
        black.setStyle(Paint.Style.STROKE);

        other = new Paint();  //I'll set  this one as I needed it.
        other.setStyle(Paint.Style.FILL);

        if (mwidth == 0) return;  //something isn't setup yet.

        float radius = mwidth /5f;
        //top left.
        myCircles[0] = new circle(radius, radius,radius);

        //top right
        myCircles[1] = new circle(mwidth - radius,radius, radius);
        //middle
        myCircles[2] = new circle();
        myCircles[2].r = radius;
        myCircles[2].x = mwidth/2f;
        myCircles[2].y = mheight/2f;

        //top left.\
        myCircles[3] = new circle();
        myCircles[3].r = radius;
        myCircles[3].x = radius;
        myCircles[3].y = mheight - radius;
        //top right
        myCircles[4] = new circle();
        myCircles[4].r = radius;
        myCircles[4].x = mwidth - radius;
        myCircles[4].y = mheight - radius;
    }



    /*
     * clears the grid and then has the view redraw.
     */

    void clearmaze() {
        for (int i = 0; i < size; i++) {
           myCircles[i].color = Color.BLACK;
        }
        invalidate();
    }

    /**
     * (non-Javadoc)
     * @see android.view.View#onDraw(android.graphics.Canvas)
     *
     * This is one of two methods I overrode.   The onDraw method
     * is the one that will draw everything for this view.
     * in this case, an 8x8 grid on the screen.
     *
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);
        if (mwidth ==0) return;
        for (int i = 0; i < size; i++) {
           other.setColor(myCircles[i].color);
           canvas.drawCircle(myCircles[i].x, myCircles[i].y,myCircles[i].r, other);
        }

    }


    boolean where(int x, int y, int color) {
        float deltaX, deltaY;
        double distance;
        boolean collided;

        for (int i = 0; i < size; i++) {
            deltaX = myCircles[i].x - x;
            deltaY = myCircles[i].y - y;
            distance = Math.sqrt((deltaX*deltaX + deltaY*deltaY));
            collided = (distance <= myCircles[i].r);
            if (collided) {
                myCircles[i].color = color;
                return true;
            }
        }
        return false;
    }

    /**
     * overrode this event to get all the touch events for this view.
     *
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        // Retrieve the new x and y touch positions
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.i("onTouchEvent", "Action_down");
                where(x, y, Color.BLUE);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:  //move not sure what to do yet.
                where(x, y, Color.YELLOW);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                performClick();
                break;
        }


        return true;
    }

    /**
     * using this to get the size of the view.
     * now... I should likely set the height if the
     * view is using wrapcontent instead matchparent
     * ie likely it comes back with a zero height or width!
     * and then nothing will be draw (no call to onDraw either).
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.i("MSW", "" + getMeasuredWidth());
        Log.i("MSH", "" + getMeasuredHeight());
        mwidth = getMeasuredWidth();
        mheight = getMeasuredHeight();
        if (mheight > 0 && mwidth > mheight) {
            mwidth = mheight;  //make it square.
        } else if (mheight == 0) {
            mheight = mwidth;
        } else if (mheight > mwidth) {
            mheight = mwidth;  //make it square.
        }
        Log.i("MSW", "" + mwidth);
        Log.i("MSH", "" + mheight);
        //setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        setMeasuredDimension(mwidth, mheight);
        setup();
    }

     class circle {
        float x;
        float y;
        float r;
        int color;

         public circle(float mx, float my, float mr) {
             x=mx; y=my; r=mr; color=Color.BLACK;
         }

         public circle() {
             x=0; y=0; r=0; color= Color.BLACK;
         }

    }

}
