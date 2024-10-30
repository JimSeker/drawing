package edu.cs4730.surfaceviewdemo_kt

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Nothing to see here.  All the code is in mySurfaceView
 * This is why it's called a SeparateActivity, all the code to deal with the surfaceView is in
 * the SurfaceView.
 */
class SeparateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_separate)

        //with android 15, we need the insets otherwise the surface draws over the things it should not.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val myLayout = findViewById<LinearLayout>(R.id.main)
        myLayout.addView(mySurfaceView(this))
    }
}