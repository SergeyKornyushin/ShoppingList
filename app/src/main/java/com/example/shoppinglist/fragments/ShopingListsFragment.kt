package com.example.shoppinglist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.example.shoppinglist.activities.MainApp
import com.example.shoppinglist.activities.ShoppingListActivity
import com.example.shoppinglist.databinding.FragmentShopListNamesBinding
import com.example.shoppinglist.dialogs.DeleteDialog
import com.example.shoppinglist.dialogs.NewListDialog
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.rv_adapter.ShoppingListAdapter
import com.example.shoppinglist.utils.TimeManager.getCurrentTime
import com.example.shoppinglist.view_models.MainViewModel

class ShopingListsFragment : BaseFragment(), ShoppingListAdapter.Listener {
    private lateinit var binding: FragmentShopListNamesBinding

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {
                mainViewModel.insertShoppingList(
                    ShoppingList(
                        id = null,
                        listName = name,
                        creationTime = getCurrentTime(),
                        allItemCounter = 0,
                        checkedItemsCounter = 0,
                        itemsId = ""
                    )
                )
            }
        }, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopListNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ShoppingListAdapter(this)
        binding.rvShopList.adapter = adapter
        mainViewModel.allShoppingLists.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShopingListsFragment()
    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity, object : DeleteDialog.Listener {
            override fun onClick() {
                mainViewModel.deleteShoppingList(id)
            }
        })
    }

    override fun editItem(shoppingList: ShoppingList) {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {
                mainViewModel.updateShoppingList(shoppingList.copy(listName = name))
            }
        }, shoppingList.listName)
    }


    override fun onClickItem(shoppingList: ShoppingList) {
        startActivity(Intent(activity, ShoppingListActivity::class.java).apply {
            putExtra(ShoppingListActivity.SHOPPING_LIST, shoppingList)
        })
    }
}