package com.example.shoppinglist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityShoppingListBinding
import com.example.shoppinglist.dialogs.EditListDialog
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.entities.ShoppingListItem
import com.example.shoppinglist.rv_adapter.ShoppingItemAdapter
import com.example.shoppinglist.rv_adapter.ShoppingItemAdapter.Companion.EDIT
import com.example.shoppinglist.utils.ShareHelper
import com.example.shoppinglist.view_models.MainViewModel

class ShoppingListActivity : AppCompatActivity(), ShoppingItemAdapter.Listener {
    private lateinit var binding: ActivityShoppingListBinding
    private lateinit var adapter: ShoppingItemAdapter
    private var shoppingList: ShoppingList? = null
    private lateinit var saveMenuButton: MenuItem
    private var editTextNewShoppingItem: EditText? = null
    private  lateinit var textWatcher: TextWatcher
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shoppingList = intent.getSerializableExtra(SHOPPING_LIST) as ShoppingList

        adapter = ShoppingItemAdapter(this)
        binding.rvShoppingList.adapter = adapter

        mainViewModel.getAllItemsFromList(shoppingList?.id!!).observe(this) {
            adapter.submitList(it)
            binding.tvShoppingListEmpty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shopping_list_menu, menu)
        saveMenuButton = menu?.findItem(R.id.save_shopping_list)!!
        val newMenuButton = menu.findItem(R.id.new_shopping_list)
            .setOnActionExpandListener(expandActionView())
        editTextNewShoppingItem = newMenuButton.actionView
            .findViewById(R.id.et_new_shopping_item) as EditText

        textWatcher = getTextWatcher()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_shopping_list -> addNewShoppingItem()

            R.id.delete_shopping_list -> {
                mainViewModel.deleteShoppingList(shoppingList?.id!!)
                finish()
            }

            R.id.clear_shopping_list -> mainViewModel.clearShoppingList(shoppingList!!)

            R.id.share_shopping_list ->
                startActivity(
                    Intent.createChooser(
                        ShareHelper.shareShoppingList(
                            adapter.currentList,
                            shoppingList?.listName!!
                        ), "Share by"
                    )
                )

        }
        return super.onOptionsItemSelected(item)
    }

    private fun getTextWatcher():TextWatcher{
        return object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i("test4", "onTextChanged: $s")
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
    }

    private fun addNewShoppingItem() {
        if (editTextNewShoppingItem?.text.toString().isNotEmpty()) {
            val shoppingListItem = ShoppingListItem(
                id = null,
                itemName = editTextNewShoppingItem?.text.toString(),
                itemInfo = "",
                checkItem = false,
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
                editTextNewShoppingItem?.addTextChangedListener(textWatcher)
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                saveMenuButton.isVisible = false
                editTextNewShoppingItem?.removeTextChangedListener(textWatcher)
                invalidateOptionsMenu()
                return true
            }

        }
    }

    companion object {
        const val SHOPPING_LIST = "shopping_list"
    }

    override fun onClickItem(shoppingListItem: ShoppingListItem, action: String) {
        if (action == EDIT) {
            EditListDialog.showEditDialog(this, shoppingListItem, object : EditListDialog.Listener {
                override fun onClick(shoppingListItem: ShoppingListItem) {
                    mainViewModel.updateShoppingListItem(shoppingListItem)
                }
            })
        } else {
            mainViewModel.updateShoppingListItem(shoppingListItem)
        }

    }
}