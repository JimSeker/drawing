package edu.cs4730.animatedvectordrawabledemo_kt

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.cs4730.animatedvectordrawabledemo_kt.databinding.ActivityMainBinding

/**
 * and attempt at a animated vector demo.   it sort of works.
 * second one is from https://stories.uplabs.com/animated-icons-on-android-ee635307bd6 that works much better.
 *
 * Note, the style is left as light, because you can't see anything in a dark theme.
 */

class MainActivity : AppCompatActivity() {
    private lateinit var animatable: Animatable
    private lateinit var animatable2: Animatable
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.iv.isClickable = true
        animatable = binding.iv.drawable as Animatable
        binding.iv.setOnClickListener {
            if (animatable.isRunning) {
                animatable.stop()
            } else animatable.start()
        }

        binding.iv2.isClickable = true
        animatable2 = binding.iv2.drawable as Animatable
        binding.iv2.setOnClickListener {
            if (animatable2.isRunning) {
                animatable2.stop()
            } else
                animatable2.start()
        }
    }
}
