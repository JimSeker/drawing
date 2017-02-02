package edu.cs4730.textureviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.FrameLayout;


/*
 *
 *  This basically new myTextureView, so most of the code in myTextView.java instead of here.
 *
 *   This could even be setup with a R.layout.x instead.
 */

public class SeparateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //this will cause the TextureView to fill the screen.
        setContentView(new myTextureView(this));

        //if you wanted to set the size via a layout, use this code
/*        FrameLayout content = new FrameLayout(this);
        myTextureView mTextureView = new myTextureView(this);
        mTextureView.setOpaque(false);
        content.addView(mTextureView, new FrameLayout.LayoutParams(500, 500, Gravity.CENTER));
        setContentView(content);
*/

    }


}
