package com.hardzei.coronavirusapp.view.screens.settingscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceFragmentCompat
import com.hardzei.coronavirusapp.R

class SettingsScreenFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        // Get the switch preference
        val blackTheme: CheckBoxPreference? = findPreference("black_theme")
        val lightTheme: CheckBoxPreference? = findPreference("light_theme")

        // Switch preference change listener
        blackTheme?.setOnPreferenceChangeListener { preference, newValue ->
            if (newValue == true && preference.isEnabled) {
                blackTheme.isChecked = true
                lightTheme?.isChecked = false
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                blackTheme.isChecked = true
                lightTheme?.isChecked = false
            }
            false
        }
        // Switch preference change listener
        lightTheme?.setOnPreferenceChangeListener { preference, newValue ->
            if (newValue == true && preference.isEnabled) {
                lightTheme.isChecked = true
                blackTheme?.isChecked = false
                requireActivity().setTheme(R.style.LightTheme)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                lightTheme.isChecked = true
                blackTheme?.isChecked = false
            }
            false
        }
    }
}