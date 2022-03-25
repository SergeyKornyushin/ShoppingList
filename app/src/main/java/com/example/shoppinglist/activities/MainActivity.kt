package com.example.shoppinglist.activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityMainBinding
import com.example.shoppinglist.dialogs.NewListDialog
import com.example.shoppinglist.fragments.FragmentManager
import com.example.shoppinglist.fragments.NoteFragment
import com.example.shoppinglist.fragments.ShoppingListsFragment
import com.example.shoppinglist.settings.SettingsActivity
import com.google.android.material.color.MaterialColors
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var currentMenuItemId = R.id.shopping_list
    private var currentTheme = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        currentTheme = sharedPreferences.getString(getString(R.string.pref_theme_key), "Dark").toString()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        FragmentManager.setFragment(ShoppingListsFragment.newInstance(), this)
        setBottomNavListener()
    }

    //refactor
    private fun setBottomNavListener() {
        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
                R.id.notes -> {
                    currentMenuItemId = R.id.notes
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }
                R.id.shopping_list -> {
                    currentMenuItemId = R.id.shopping_list
                    FragmentManager.setFragment(ShoppingListsFragment.newInstance(), this)
                }
                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.navView.selectedItemId = currentMenuItemId
        if (currentTheme != sharedPreferences.getString(getString(R.string.pref_theme_key), "Dark")){
            recreate()
        }
    }

    private fun getSelectedTheme(): Int =
        when {
            sharedPreferences.getString(getString(R.string.pref_theme_key), "Dark") == "Dark" -> {
                R.style.Theme_ShoppingListDark
            }
            sharedPreferences.getString(getString(R.string.pref_theme_key), "Dark") == "Orange" -> {
                R.style.Theme_ShoppingListOrange
            }
            else -> {
                R.style.Theme_ShoppingListLight
            }
        }
}