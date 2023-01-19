package edu.cs4730.graphicoverlaydemo;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // if not using arch navigation, then you need to implement this.
        navView.setOnItemSelectedListener(
            new BottomNavigationView.OnItemSelectedListener() {
                public boolean onNavigationItemSelected(MenuItem item) {
                    //setup the fragments here.
                    int id = item.getItemId();
                    if (id == R.id.nav_camera) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new CameraFragment()).commit();

                    } else if (id == R.id.nav_simple) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SimpleFragment()).commit();
                    } else if (id == R.id.nav_multi) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MultiFragment()).commit();
                    }
                    item.setChecked(true);
                    return true;
                }
            }

        );
        //set the first one in place.
        getSupportFragmentManager().beginTransaction().add(R.id.container, new SimpleFragment()).commit();
    }


}
