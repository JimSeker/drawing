package edu.cs4730.drawdemo2;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


/*
 *  this does nothing except get the fragment
 *  see fragment code for what this example does.
 * 
 */


public class MainActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new Draw2_Fragment()).commit();
			}
	}
}
