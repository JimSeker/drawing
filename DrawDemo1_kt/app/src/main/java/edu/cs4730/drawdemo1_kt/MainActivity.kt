package edu.cs4730.drawdemo1_kt

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

/**
 * while there is alot of code here, the example is actually all in the fragments.
 * this is just to get the drawer layout working.
 */


class MainActivity : AppCompatActivity() {
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private lateinit var mDrawerlayout: DrawerLayout
    private lateinit var mNavigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // enable ActionBar app icon to behave as action to toggle nav drawer

        // enable ActionBar app icon to behave as action to toggle nav drawer
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        //standard navigation drawer setup.

        //standard navigation drawer setup.
        mDrawerlayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout

        mDrawerToggle = object : ActionBarDrawerToggle(
            this,  // host activity
            mDrawerlayout,  //drawerlayout object
            toolbar,  //toolbar
            R.string.drawer_open,  //open drawer description  required!
            R.string.drawer_close
        ) {
            //closed drawer description
            //called once the drawer has closed.
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                supportActionBar!!.title = "Categories"
                invalidateOptionsMenu() // creates call to onPrepareOptionsMenu()
            }

            //called when the drawer is now open.
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                supportActionBar!!.setTitle(R.string.app_name)
                invalidateOptionsMenu() // creates call to onPrepareOptionsMenu()
            }
        }
        //To disable the icon for the drawer, change this to false
        //mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerlayout.addDrawerListener(mDrawerToggle)

        //this ia the support Navigation view.
        mNavigationView = findViewById<View>(R.id.navview) as NavigationView

        //setup a listener, which acts very similiar to how menus are handled.
        mNavigationView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem -> //we could just as easily call onOptionsItemSelected(menuItem) and how it deal with it.
            val id = menuItem.itemId
            if (id == R.id.drawing) {
                //load fragment
                //              if (!menuItem.isChecked()) {  //only need to do this if fragment is already loaded.
               // menuItem.isChecked = true //make sure to check/highlight the item.
                supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment())
                    .commit()
                //                }
                mDrawerlayout.closeDrawers() //close the drawer, since the user has selected it.
                return@OnNavigationItemSelectedListener true
            } else if (id == R.id.drawing1) {
                //load fragment
                //                  if (!menuItem.isChecked()) {  //only need to do this if fragment is already loaded.
               // menuItem.isChecked = true //make sure the item is checked/highlighted
                supportFragmentManager.beginTransaction().replace(R.id.container, Draw1Fragment())
                    .commit()
                //                    }
                //now close the nav drawer.
                mDrawerlayout.closeDrawers()
                return@OnNavigationItemSelectedListener true
            } else if (id == R.id.andraw) {
                //load fragment
               // menuItem.isChecked = true //make sure the item is checked/highlighted
                supportFragmentManager.beginTransaction().replace(R.id.container, AnDrawFragment())
                    .commit()
                //now close the nav drawer.
                mDrawerlayout.closeDrawers()
                return@OnNavigationItemSelectedListener true
            } else if (id == R.id.asdraw) {
                //load fragment
               // menuItem.isChecked = true //make sure the item is checked/highlighted
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, AsycDrawFragment()).commit()
                //now close the nav drawer.
                mDrawerlayout.closeDrawers()
                return@OnNavigationItemSelectedListener true
            }
            false
        })

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, MainFragment()).commit()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig)
    }

}