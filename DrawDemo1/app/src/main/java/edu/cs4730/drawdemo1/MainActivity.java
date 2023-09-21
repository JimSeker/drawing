package edu.cs4730.drawdemo1;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.cs4730.drawdemo1.databinding.ActivityMainBinding;

/**
 * while there is alot of code here, the example is actually all in the fragments.
 * this is just to get the bottom navigation bar  work.
 */

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        
        //setup a listener, which acts very similar to how menus are handled.
        binding.navView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.drawing) {
                    getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), new MainFragment()).commit();
                    menuItem.setChecked(true);  //make sure to check/highlight the item.
                    return true;
                } else if (id == R.id.drawing1) {
                    menuItem.setChecked(true); //make sure the item is checked/highlighted
                    getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), new Draw1Fragment()).commit();
                    return true;
                } else if (id == R.id.andraw) {
                    getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), new AnDrawFragment()).commit();
                    menuItem.setChecked(true); //make sure the item is checked/highlighted
                    return true;
                } else if (id == R.id.asdraw) {

                    menuItem.setChecked(true); //make sure the item is checked/highlighted
                    getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), new AsycDrawFragment()).commit();
                    return true;
                }
                return false;
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(binding.container.getId(), new MainFragment()).commit();
        }
    }
}
