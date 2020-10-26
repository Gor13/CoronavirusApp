package com.hardzei.coronavirusapp.view.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.hardzei.coronavirusapp.R
import com.hardzei.coronavirusapp.view.screens.countrieslistscreen.CountriesListScreenFragment

class MainScreenActivity : AppCompatActivity() {

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

        setContentView(R.layout.activity_main)

        if (supportFragmentManager.backStackEntryCount == 0) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.frame_container,
                    CountriesListScreenFragment()
                ).commit()
        }
    }
    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}