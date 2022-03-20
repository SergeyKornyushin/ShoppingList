package com.example.shoppinglist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityShoppingListBinding
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.view_models.MainViewModel

class ShoppingListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingListBinding
    private var shoppingList: ShoppingList? = null
    private lateinit var saveMenuButton: MenuItem
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shoppingList = intent.getSerializableExtra(SHOPPING_LIST) as ShoppingList
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shopping_list_menu, menu)
        saveMenuButton = menu?.findItem(R.id.save_shopping_list)!!
        val newMenuButton = menu.findItem(R.id.new_shopping_list)
                .setOnActionExpandListener(expandActionView())
        return true
    }

    private fun expandActionView(): MenuItem.OnActionExpandListener {
        return object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                saveMenuButton.isVisible = true
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                saveMenuButton.isVisible = false
                invalidateOptionsMenu()
                return true
            }

        }
    }

    companion object {
        const val SHOPPING_LIST = "shopping_list"
    }
}