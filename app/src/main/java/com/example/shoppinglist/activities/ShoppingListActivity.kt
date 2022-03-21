package com.example.shoppinglist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityShoppingListBinding
import com.example.shoppinglist.dialogs.EditListDialog
import com.example.shoppinglist.entities.LibraryItem
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.entities.ShoppingListItem
import com.example.shoppinglist.rv_adapter.ShoppingItemAdapter
import com.example.shoppinglist.rv_adapter.ShoppingItemAdapter.Companion.ADD_LIBRARY_ITEM
import com.example.shoppinglist.rv_adapter.ShoppingItemAdapter.Companion.CHECK_BOX
import com.example.shoppinglist.rv_adapter.ShoppingItemAdapter.Companion.DELETE_LIBRARY_ITEM
import com.example.shoppinglist.rv_adapter.ShoppingItemAdapter.Companion.EDIT
import com.example.shoppinglist.rv_adapter.ShoppingItemAdapter.Companion.EDIT_LIBRARY_ITEM
import com.example.shoppinglist.utils.ShareHelper
import com.example.shoppinglist.view_models.MainViewModel

class ShoppingListActivity : AppCompatActivity(), ShoppingItemAdapter.Listener {
    private lateinit var binding: ActivityShoppingListBinding
    private lateinit var adapter: ShoppingItemAdapter
    private var shoppingList: ShoppingList? = null
    private lateinit var saveMenuButton: MenuItem
    private var editTextNewShoppingItem: EditText? = null
    private lateinit var textWatcher: TextWatcher
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

        setShoppingListItemsObserver(true)
    }

    private fun setShoppingListItemsObserver(state: Boolean) {
        if (state) {
            mainViewModel.getAllItemsFromList(shoppingList?.id!!).observe(this) {
                adapter.submitList(it)
                binding.tvShoppingListEmpty.visibility =
                    if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        } else {
            mainViewModel.getAllItemsFromList(shoppingList?.id!!)
                .removeObservers(this@ShoppingListActivity)
        }
    }

    private fun setLibraryItemObserver(state: Boolean) {
        if (state) {
            mainViewModel.libraryItems.observe(this) {
                val tempShoppingListItem = ArrayList<ShoppingListItem>()
                it.forEach { item ->
                    val shoppingItem = ShoppingListItem(
                        id = item.id,
                        itemName = item.item,
                        itemInfo = "",
                        checkItem = false,
                        itemId = 0,
                        itemType = 1
                    )
                    tempShoppingListItem.add(shoppingItem)
                }
                adapter.submitList(tempShoppingListItem)
                binding.tvShoppingListEmpty.visibility =
                    if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        } else {
            mainViewModel.libraryItems.removeObservers(this@ShoppingListActivity)
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
            R.id.save_shopping_list -> addNewShoppingItem(editTextNewShoppingItem?.text.toString())

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

    private fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mainViewModel.getAllLibraryItems("%$s%")
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
    }

    private fun addNewShoppingItem(nameItem: String) {
        if (nameItem.isEmpty()) return
        val shoppingListItem = ShoppingListItem(
            id = null,
            itemName = nameItem,
            itemInfo = "",
            checkItem = false,
            itemId = shoppingList?.id!!,
            itemType = 0
        )
        editTextNewShoppingItem?.text?.clear()
        mainViewModel.insertShoppingListItem(shoppingListItem)
    }

    private fun expandActionView(): MenuItem.OnActionExpandListener {
        return object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem?): Boolean {
                saveMenuButton.isVisible = true
                editTextNewShoppingItem?.addTextChangedListener(textWatcher)
                setShoppingListItemsObserver(false)
                setLibraryItemObserver(true)
                mainViewModel.getAllLibraryItems("%%")
                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem?): Boolean {
                saveMenuButton.isVisible = false
                editTextNewShoppingItem?.removeTextChangedListener(textWatcher)
                invalidateOptionsMenu()
                editTextNewShoppingItem?.text?.clear()
                setLibraryItemObserver(false)
                setShoppingListItemsObserver(true)
                return true
            }

        }
    }

    companion object {
        const val SHOPPING_LIST = "shopping_list"
    }

    override fun onClickItem(shoppingListItem: ShoppingListItem, action: String) {
        when (action) {
            EDIT -> EditListDialog.showEditDialog(this, shoppingListItem,
                object : EditListDialog.Listener {
                    override fun onClick(shoppingListItem: ShoppingListItem) {
                        mainViewModel.updateShoppingListItem(shoppingListItem)
                    }
                })

            CHECK_BOX -> mainViewModel.updateShoppingListItem(shoppingListItem)

            EDIT_LIBRARY_ITEM -> {
                EditListDialog.showEditDialog(this, shoppingListItem,
                    object : EditListDialog.Listener {
                        override fun onClick(shoppingListItem: ShoppingListItem) {
                            mainViewModel.updateLibraryItem(
                                LibraryItem(
                                    shoppingListItem.id,
                                    shoppingListItem.itemName
                                )
                            )
                            mainViewModel
                                .getAllLibraryItems("%${editTextNewShoppingItem?.text.toString()}%")
                        }
                    })
            }
            DELETE_LIBRARY_ITEM -> {
                mainViewModel.deleteLibraryItem(shoppingListItem.id!!)
                mainViewModel
                    .getAllLibraryItems("%${editTextNewShoppingItem?.text.toString()}%")
            }
            ADD_LIBRARY_ITEM -> {
                addNewShoppingItem(shoppingListItem.itemName)
            }
        }
    }
}