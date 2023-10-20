package edu.cs4730.animatedvectordrawabledemo_kt

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import edu.cs4730.animatedvectordrawabledemo_kt.databinding.ActivityMainBinding


/**
 * and attempt at a animated vector demo.   it sort of works.
 * second one is from https://stories.uplabs.com/animated-icons-on-android-ee635307bd6 that works much better.
 *
 * Note, the style is left as light, because you can't see anything in a dark theme.
 */

class MainActivity : AppCompatActivity() {
    lateinit var animatable: Animatable
    lateinit var animatable2: Animatable
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iv.isClickable = true
        animatable = binding.iv.drawable as Animatable
        binding.iv.setOnClickListener {
            if (animatable.isRunning) {
                animatable.stop()
            } else
                animatable.start()
        }
        val iv2 = findViewById<ImageView>(R.id.iv2)
        iv2.isClickable = true
        animatable2 = iv2.drawable as Animatable
        iv2.setOnClickListener {
            if (animatable2.isRunning) {
                animatable2.stop()
            } else
                animatable2.start()
        }
    }
}
