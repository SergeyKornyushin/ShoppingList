package com.example.shoppinglist.rv_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.LibraryListItemBinding
import com.example.shoppinglist.databinding.ListNameRvItemBinding
import com.example.shoppinglist.databinding.NoteRvItemBinding
import com.example.shoppinglist.databinding.ShoppingListItemBinding
import com.example.shoppinglist.entities.NoteItem
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.entities.ShoppingListItem
import com.example.shoppinglist.utils.HtmlManager
import com.google.android.material.color.MaterialColors

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
                if (shoppingListItem.itemInfo.isEmpty())
                    tvListItemDescription.visibility = View.GONE
                else {
                    tvListItemDescription.visibility = View.VISIBLE
                    tvListItemDescription.text = shoppingListItem.itemInfo
                }
                checkBoxItem.isChecked = shoppingListItem.checkItem
                setPainFlagAndColor(binding)
                checkBoxItem.setOnClickListener {
                    listener.onClickItem(
                        shoppingListItem.copy(checkItem = checkBoxItem.isChecked),
                        CHECK_BOX
                    )
                }
                ibtnEditItem.setOnClickListener {
                    listener.onClickItem(shoppingListItem, EDIT)
                }
            }
        }

        fun setLibraryData(shoppingListItem: ShoppingListItem, listener: Listener) {
            val binding = LibraryListItemBinding.bind(view)
            binding.apply {
                tvLibraryItemTitle.text = shoppingListItem.itemName
                ibtnDeleteLibraryItem.setOnClickListener {
                    listener.onClickItem(shoppingListItem, DELETE_LIBRARY_ITEM)
                }
                ibtnEditLibraryItem.setOnClickListener {
                    listener.onClickItem(shoppingListItem, EDIT_LIBRARY_ITEM)
                }
                itemView.setOnClickListener {
                    listener.onClickItem(shoppingListItem, ADD_LIBRARY_ITEM)
                }
            }
        }

        private fun setPainFlagAndColor(binding: ShoppingListItemBinding) {
            binding.apply {
                if (checkBoxItem.isChecked) {
                    tvListItemTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvListItemDescription.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvListItemTitle.setTextColor(
                        MaterialColors.getColor(
                            binding.root.context,
                            R.attr.textColorCheckedItems,
                            Color.BLACK
                        )
                    )
                } else {
                    tvListItemTitle.paintFlags = Paint.ANTI_ALIAS_FLAG
                    tvListItemDescription.paintFlags = Paint.ANTI_ALIAS_FLAG
                    tvListItemTitle.setTextColor(
                        MaterialColors.getColor(
                            binding.root.context,
                            R.attr.textColorItems,
                            Color.BLACK
                        )
                    )
                }
            }
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
        fun onClickItem(shoppingListItem: ShoppingListItem, action: String)
    }

    companion object {
        const val EDIT = "edit"
        const val CHECK_BOX = "check_box"
        const val EDIT_LIBRARY_ITEM = "edit_library_item"
        const val DELETE_LIBRARY_ITEM = "delete_library_item"
        const val ADD_LIBRARY_ITEM = "add_library_item"
    }
}