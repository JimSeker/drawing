package edu.cs4730.textureviewdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


/**
 * This basically new myTextureView, so most of the code in myTextView.java instead of here.
 * This could even be setup with a R.layout.x instead.
 */

public class SeparateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_separate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        //this will cause the TextureView to fill the screen.
        FrameLayout myLayout = findViewById(R.id.main);
        myLayout.addView(new myTextureView(this));

    }


}
