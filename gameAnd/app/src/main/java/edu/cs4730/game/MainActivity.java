package edu.cs4730.game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

/*
 * basic activity.  puts MySurfaceView to the screen
 * and creates an exit menu item.
 * 
 * bulk of the project is in the mySurfaceView class.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // no layout used, since using mySurfaceView
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new mySurfaceView(this));
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

    @Override public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exit:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}