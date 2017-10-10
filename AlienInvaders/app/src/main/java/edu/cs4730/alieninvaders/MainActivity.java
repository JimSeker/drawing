package edu.cs4730.alieninvaders;

import android.support.v7.app.AppCompatActivity;
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
