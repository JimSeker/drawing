package edu.cs4730.drawdemo1;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

/*
* while there is alot of code here, the example is actually all in the fragments.
* this is just to get the drawer layout working.
 */


public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerlayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //standard navigation drawer setup.
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this,  // host activity
                mDrawerlayout,  //drawerlayout object
                toolbar,  //toolbar
                R.string.drawer_open,  //open drawer description  required!
                R.string.drawer_close) {  //closed drawer description

            //called once the drawer has closed.
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Categories");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            //called when the drawer is now open.
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        //To disable the icon for the drawer, change this to false
        //mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerlayout.addDrawerListener(mDrawerToggle);

        //this ia the support Navigation view.
        mNavigationView = (NavigationView) findViewById(R.id.navview);
        //setup a listener, which acts very similiar to how menus are handled.
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //we could just as easily call onOptionsItemSelected(menuItem) and how it deal with it.
                //Log.v(TAG, "We got someting?");
                int id = menuItem.getItemId();
                if (id == R.id.drawing) {
                    //load fragment
      //              if (!menuItem.isChecked()) {  //only need to do this if fragment is already loaded.
                        menuItem.setChecked(true);  //make sure to check/highlight the item.
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainFragment()).commit();
    //                }
                    mDrawerlayout.closeDrawers();  //close the drawer, since the user has selected it.
                    return true;
                } else if (id == R.id.drawing1) {
                    //load fragment
  //                  if (!menuItem.isChecked()) {  //only need to do this if fragment is already loaded.
                        menuItem.setChecked(true); //make sure the item is checked/highlighted
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new Draw1Fragment()).commit();
//                    }
                    //now close the nav drawer.
                    mDrawerlayout.closeDrawers();
                    return true;
                }else if (id == R.id.andraw) {
                    //load fragment

                        menuItem.setChecked(true); //make sure the item is checked/highlighted
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new AnDrawFragment()).commit();
                    //now close the nav drawer.
                    mDrawerlayout.closeDrawers();
                    return true;
                }else if (id == R.id.asdraw) {
                    //load fragment

                    menuItem.setChecked(true); //make sure the item is checked/highlighted
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new AsycDrawFragment()).commit();
                    //now close the nav drawer.
                    mDrawerlayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
