package edu.cs4730.surfaceviewdemo_kt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/*
  Nothing to see here.  All the code is in mySurfaceView
  This is why it's called a SeparateActivity, all the code to deal with the surfaceView is in
  the SurfaceView.
 */
class SeparateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mySurfaceView(this))
    }
}