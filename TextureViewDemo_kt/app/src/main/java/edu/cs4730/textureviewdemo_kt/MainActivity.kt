package edu.cs4730.textureviewdemo_kt

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.button).setOnClickListener {
            startActivity(Intent(baseContext, AllinOneActivity::class.java))
        }

        findViewById<View>(R.id.button2).setOnClickListener {
            startActivity(Intent(baseContext, SeparateActivity::class.java))
        }
    }
}