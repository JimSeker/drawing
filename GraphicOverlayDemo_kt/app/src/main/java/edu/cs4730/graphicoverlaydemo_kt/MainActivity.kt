package edu.cs4730.graphicoverlaydemo_kt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)

        // if not using arch navigation, then you need to implement this.

        // if not using arch navigation, then you need to implement this.
        navView.setOnItemSelectedListener { item -> //setup the fragments here.
            val id = item.itemId
            if (id == R.id.nav_camera) {
                supportFragmentManager.beginTransaction().replace(R.id.container, CameraFragment())
                    .commit()
            } else if (id == R.id.nav_simple) {
                supportFragmentManager.beginTransaction().replace(R.id.container, SimpleFragment())
                    .commit()
            } else if (id == R.id.nav_multi) {
                supportFragmentManager.beginTransaction().replace(R.id.container, MultiFragment())
                    .commit()
            }
            item.isChecked = true
            true
        }
        //set the first one in place.
        supportFragmentManager.beginTransaction().add(R.id.container, SimpleFragment()).commit()
    }

}
