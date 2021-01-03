package com.hardzei.coronavirusapp.view.screens

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.hardzei.coronavirusapp.R
import kotlinx.android.synthetic.main.activity_main_screen.*

class MainScreenActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        if (PreferenceManager
            .getDefaultSharedPreferences(this)
            .getBoolean("black_theme", false)
        ) {
            setTheme(R.style.DarkTheme)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            setTheme(R.style.LightTheme)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_screen)
        setSupportActionBar(toolbar)

        // Setting up NavController and NavHostFragment
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.viewPagerHelpFragment) {
                // Hide toolbar in the landing screen
                toolbar.visibility = View.GONE
            } else {
                // Show toolbar in all other cases
                toolbar.visibility = View.VISIBLE
            }
        }
    }
}
