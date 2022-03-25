package com.example.shoppinglist.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.shoppinglist.R

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val preference: Preference? = findPreference(getString(R.string.pref_theme_key))
        preference?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                if (sharedPreferences.getString(getString(R.string.pref_theme_key), "Dark") != newValue) {
                    requireActivity().recreate()
                }
                true
            }
    }
}