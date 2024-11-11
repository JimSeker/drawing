package edu.cs4730.drawdemo1_kt

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationBarView
import edu.cs4730.drawdemo1_kt.databinding.ActivityMainBinding

/**
 * while there is alot of code here, the example is actually all in the fragments.
 * this is just to get the bottom navigation bar  work.
 */

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left, systemBars.top, systemBars.right, systemBars.bottom
            )
            WindowInsetsCompat.CONSUMED
        }
        setSupportActionBar(binding.toolbar)

        //setup a listener, which acts very similar to how menus are handled.
        binding.navView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            if (id == R.id.drawing) {
                supportFragmentManager.beginTransaction()
                    .replace(binding.container.id, MainFragment()).commit()
                menuItem.isChecked = true //make sure to check/highlight the item.
                return@OnItemSelectedListener true
            } else if (id == R.id.drawing1) {
                menuItem.isChecked = true //make sure the item is checked/highlighted
                supportFragmentManager.beginTransaction()
                    .replace(binding.container.id, Draw1Fragment()).commit()
                return@OnItemSelectedListener true
            } else if (id == R.id.andraw) {
                supportFragmentManager.beginTransaction()
                    .replace(binding.container.id, AnDrawFragment()).commit()
                menuItem.isChecked = true //make sure the item is checked/highlighted
                return@OnItemSelectedListener true
            } else if (id == R.id.asdraw) {
                menuItem.isChecked = true //make sure the item is checked/highlighted
                supportFragmentManager.beginTransaction()
                    .replace(binding.container.id, AsycDrawFragment()).commit()
                return@OnItemSelectedListener true
            }
            false
        })
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(binding.container.id, MainFragment())
                .commit()
        }
    }
}