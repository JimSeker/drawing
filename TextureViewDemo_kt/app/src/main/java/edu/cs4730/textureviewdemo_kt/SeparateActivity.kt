package edu.cs4730.textureviewdemo_kt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.cs4730.textureviewdemo_kt.myTextureView
import android.widget.FrameLayout
import android.view.Gravity

/**
 * This basically new myTextureView, so most of the code in myTextView.java instead of here.
 * This could even be setup with a R.layout.x instead.
 */
class SeparateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this will cause the TextureView to fill the screen.
        setContentView(myTextureView(this))

        //if you wanted to set the size via a layout, use this code
        /*
        val content = FrameLayout(this)
        val mTextureView = myTextureView(this)
        mTextureView.isOpaque = false
        content.addView(mTextureView, FrameLayout.LayoutParams(500, 500, Gravity.CENTER))
        setContentView(content)
        */
    }
}