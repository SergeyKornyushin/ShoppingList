package com.example.shoppinglist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.viewModels
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityShoppingListBinding
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.entities.ShoppingListItem
import com.example.shoppinglist.rv_adapter.ShoppingItemAdapter
import com.example.shoppinglist.view_models.MainViewModel

class ShoppingListActivity : AppCompatActivity(), ShoppingItemAdapter.Listener {
    private lateinit var binding: ActivityShoppingListBinding
    private var shoppingList: ShoppingList? = null
    private lateinit var saveMenuButton: MenuItem
    private var editTextNewShoppingItem: EditText? = null
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shoppingList = intent.getSerializableExtra(SHOPPING_LIST) as ShoppingList

        val adapter = ShoppingItemAdapter(this)
        binding.rvShoppingList.adapter = adapter

        mainViewModel.getAllItemsFromList(shoppingList?.id!!).observe(this) {
            adapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shopping_list_menu, menu)
        saveMenuButton = menu?.findItem(R.id.save_shopping_list)!!
        val newMenuButton = menu.findItem(R.id.new_shopping_list)
            .setOnActionExpandListener(expandActionView())
        editTextNewShoppingItem = newMenuButton.actionView
            .findViewById(R.id.et_new_shopping_item) as EditText
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save_shopping_list) {
            addNewShoppingItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNewShoppingItem() {
        if (editTextNewShoppingItem?.text.toString().isNotEmpty()) {
            val shoppingListItem = ShoppingListItem(
                id = null,
                itemName = editTextNewShoppingItem?.text.toString(),
                itemInfo = null,
                checkItem = 0,
                itemId = shoppingList?.id!!,
                itemType = 0
            )
            editTextNewShoppingItem?.text?.clear()
            mainViewModel.insertShoppingListItem(shoppingListItem)
        }
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

    override fun deleteItem(id: Int) {

    }

    override fun editItem(shoppingList: ShoppingList) {

    }

    override fun onClickItem(shoppingList: ShoppingList) {

    }
}