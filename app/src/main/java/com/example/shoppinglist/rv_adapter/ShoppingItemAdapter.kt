package com.example.shoppinglist.rv_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ListNameRvItemBinding
import com.example.shoppinglist.databinding.NoteRvItemBinding
import com.example.shoppinglist.databinding.ShoppingListItemBinding
import com.example.shoppinglist.entities.NoteItem
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.entities.ShoppingListItem
import com.example.shoppinglist.utils.HtmlManager

class ShoppingItemAdapter(private val listener: Listener) :
    ListAdapter<ShoppingListItem, ShoppingItemAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder =
        if (viewType == 0) ItemHolder.createShoppingItem(parent)
        else ItemHolder.createLibraryItem(parent)

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (getItem(position).itemType == 0) holder.setItemData(getItem(position), listener)
        else holder.setLibraryData(getItem(position), listener)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }

    class ItemHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun setItemData(shoppingListItem: ShoppingListItem, listener: Listener) {
            val binding = ShoppingListItemBinding.bind(view)
            binding.apply {
                tvListItemTitle.text = shoppingListItem.itemName
                if (shoppingListItem.itemInfo.isNullOrEmpty())
                    tvListItemDescription.visibility = View.GONE
                else {
                    tvListItemDescription.visibility = View.VISIBLE
                    tvListItemDescription.text = shoppingListItem.itemInfo
                }

            }
        }

        fun setLibraryData(shoppingListItem: ShoppingListItem, listener: Listener) {

        }

        companion object {
            fun createShoppingItem(parent: ViewGroup): ItemHolder = ItemHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.shopping_list_item, parent, false)
            )

            fun createLibraryItem(parent: ViewGroup): ItemHolder = ItemHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.library_list_item, parent, false)
            )
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<ShoppingListItem>() {
        override fun areItemsTheSame(
            oldItem: ShoppingListItem,
            newItem: ShoppingListItem
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ShoppingListItem,
            newItem: ShoppingListItem
        ): Boolean =
            oldItem == newItem
    }

    interface Listener {
        fun deleteItem(id: Int)
        fun editItem(shoppingList: ShoppingList)
        fun onClickItem(shoppingList: ShoppingList)
    }
}