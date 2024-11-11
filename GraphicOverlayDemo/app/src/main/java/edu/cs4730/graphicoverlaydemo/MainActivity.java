package edu.cs4730.graphicoverlaydemo;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import edu.cs4730.graphicoverlaydemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    String TAG = "MainActivity";
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });


        // if not using arch navigation, then you need to implement this.
        binding.navView.setOnItemSelectedListener(
            new BottomNavigationView.OnItemSelectedListener() {
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //setup the fragments here.
                    int id = item.getItemId();
                    if (id == R.id.nav_camera) {
                        getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), new CameraFragment()).commit();

                    } else if (id == R.id.nav_simple) {
                        getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), new SimpleFragment()).commit();
                    } else if (id == R.id.nav_multi) {
                        getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), new MultiFragment()).commit();
                    }
                    item.setChecked(true);
                    return true;
                }
            }

        );
        //set the first one in place.
        getSupportFragmentManager().beginTransaction().add(binding.container.getId(), new SimpleFragment()).commit();
    }


}
