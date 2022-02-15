package edu.cs4730.animatedvectordrawabledemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * and attempt at a animated vector demo.   it sort of works.
 * second one is from https://stories.uplabs.com/animated-icons-on-android-ee635307bd6 that works much better.
 */

public class MainActivity extends AppCompatActivity {
    Animatable animatable, animatable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView iv = findViewById(R.id.iv);
        iv.setClickable(true);
        animatable = (Animatable) iv.getDrawable();
        iv.setOnClickListener(new View.OnClickListener() {
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
