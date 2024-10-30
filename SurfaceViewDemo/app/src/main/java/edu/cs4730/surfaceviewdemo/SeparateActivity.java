package edu.cs4730.surfaceviewdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.widget.LinearLayout;

/**
 * Nothing to see here.  All the code is in mySurfaceView
 * This is why it's called a SeparateActivity, all the code to deal with the surfaceView is in
 * the SurfaceView.
 */

public class SeparateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_separate);
        //with android 15, we need the insets otherwise the surface draws over the things it should not.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        LinearLayout myLayout = findViewById(R.id.main);
        myLayout.addView(new mySurfaceView(this));
    }
}
