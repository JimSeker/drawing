package edu.cs4730.alieninvaders;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // no layout used, since using mySurfaceView
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new mySurfaceView(this));

    }
}
