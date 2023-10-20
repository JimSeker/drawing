package edu.cs4730.surfaceviewdemo_kt

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import edu.cs4730.surfaceviewdemo_kt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            startActivity(Intent(baseContext, AllinOneActivity::class.java))
        }

        binding.button2.setOnClickListener {
            startActivity(Intent(baseContext, SeparateActivity::class.java))
        }
    }
}