package edu.cs4730.drawdemo2_tk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

/**
 * This demos a custom view.  There is almost nothing in this activity
 * except for a call to the customview to reset the grid from a menu item.
 *
 */


class MainActivity : AppCompatActivity() {
    lateinit var dv: DrawView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dv = findViewById(R.id.dv1);
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.reset -> {
                dv.clearmaze()
                return true
            }
        }
        return false
    }
}