package edu.cs4730.surfaceviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/*
  Nothing to see here.  All the code is in mySurfaceView
  This is why it's called a SeparateActivity, all the code to deal with the surfaceView is in
  the SurfaceView.
 */

public class SeparateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new mySurfaceView(this));

    }
}
