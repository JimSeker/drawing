package edu.cs4730.graphicoverlaydemo_kt

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.cs4730.graphicoverlaydemo_kt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // if not using arch navigation, then you need to implement this.
        binding.navView.setOnItemSelectedListener { item -> //setup the fragments here.
            val id = item.itemId
            if (id == R.id.nav_camera) {
                supportFragmentManager.beginTransaction().replace(binding.container.id, CameraFragment())
                    .commit()
            } else if (id == R.id.nav_simple) {
                supportFragmentManager.beginTransaction().replace(binding.container.id, SimpleFragment())
                    .commit()
            } else if (id == R.id.nav_multi) {
                supportFragmentManager.beginTransaction().replace(binding.container.id, MultiFragment())
                    .commit()
            }
            item.isChecked = true
            true
        }
        //set the first one in place.
        supportFragmentManager.beginTransaction().add(binding.container.id, SimpleFragment()).commit()
    }

}
