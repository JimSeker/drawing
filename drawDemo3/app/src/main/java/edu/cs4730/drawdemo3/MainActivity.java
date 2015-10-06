package edu.cs4730.drawdemo3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

/*
  Nothing to see here.  All the code is in mySurfaceView
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new mySurfaceView(this));
    }
}
