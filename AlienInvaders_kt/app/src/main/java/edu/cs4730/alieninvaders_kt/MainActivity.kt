package edu.cs4730.alieninvaders_kt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // no layout used, since using mySurfaceView
        setContentView(mySurfaceView(this))
    }
}