package edu.cs4730.textureviewdemo_kt

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * This basically new myTextureView, so most of the code in myTextView.java instead of here.
 * This could even be setup with a R.layout.x instead.
 */
class SeparateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_separate)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        //this will cause the TextureView to fill the screen.
        val myLayout = findViewById<FrameLayout>(R.id.main)
        myLayout.addView(myTextureView(this))


    }
}