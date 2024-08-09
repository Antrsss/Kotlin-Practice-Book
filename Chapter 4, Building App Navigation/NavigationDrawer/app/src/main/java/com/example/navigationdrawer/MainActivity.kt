package com.example.navigationdrawer

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        // the toolbar used in the app by referencing it
        // from the layout and setting it
        setSupportActionBar(findViewById(R.id.toolbar))

        // Retrieving NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Creating top level destinations
        // and adding them to the draw
        // adding the menu items you want to display in the navigation drawer
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_recent,
                R.id.nav_favorites, R.id.nav_archive,
                R.id.nav_bin),
            //  specify which layout should be used when
            // the hamburger menu is selected
            findViewById(R.id.drawer_layout)
        )

        // sets up the app bar with the navigation graph
        // so that any changes that are made to the
        // destinations are reflected in the app bar
        setupActionBarWithNavController(navController,
            appBarConfiguration)

        // specifies the item within the navigation drawer
        // that should be highlighted when the user clicks on it
        findViewById<NavigationView>(R.id.nav_view)
            ?.setupWithNavController(navController)
    }

    //  handles pressing the up button for the secondary destination,
    //  ensuring that it goes back to its parent primary destination
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController
            .navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //  selects the menu to add to the app bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    //  handles what to do when the item is selected using the
    // item.onNavDestinationSelected(findNavController(R.id.nav_host_
    // fragment)) navigation function. This is used to navigate
    // to the destination within the navigation graph
    override fun onOptionsItemSelected(item: MenuItem):
            Boolean {
        return item.onNavDestinationSelected(findNavController
            (R.id.nav_host_fragment))
    }
}