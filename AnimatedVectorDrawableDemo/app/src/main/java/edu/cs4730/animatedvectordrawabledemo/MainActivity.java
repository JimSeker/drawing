package edu.cs4730.animatedvectordrawabledemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import edu.cs4730.animatedvectordrawabledemo.databinding.ActivityMainBinding;

/**
 * and attempt at a animated vector demo.   it sort of works.
 * second one is from https://stories.uplabs.com/animated-icons-on-android-ee635307bd6 that works much better.
 *
 * Note, the style is left as light, because you can't see anything in a dark theme.
 */

public class MainActivity extends AppCompatActivity {
    Animatable animatable, animatable2;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.iv.setClickable(true);
        animatable = (Animatable) binding.iv.getDrawable();
        binding.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (animatable.isRunning()) {
                    animatable.stop();
                } else
                    animatable.start();
            }
        });

        ImageView iv2 = findViewById(R.id.iv2);
        iv2.setClickable(true);
        animatable2 = (Animatable) iv2.getDrawable();
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (animatable2.isRunning()) {
                    animatable2.stop();
                } else
                    animatable2.start();
            }
        });


    }
}
