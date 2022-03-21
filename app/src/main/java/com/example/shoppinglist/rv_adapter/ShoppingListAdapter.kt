package com.example.shoppinglist.rv_adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ListNameRvItemBinding
import com.example.shoppinglist.databinding.NoteRvItemBinding
import com.example.shoppinglist.entities.NoteItem
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.utils.HtmlManager

class ShoppingListAdapter(private val listener: Listener) :
    ListAdapter<ShoppingList, ShoppingListAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder =
        ItemHolder.create(parent)

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ListNameRvItemBinding.bind(view)

        fun setData(shoppingList: ShoppingList, listener: Listener) =
            with(binding) {
                tvListTitle.text = shoppingList.listName
                tvCreationTime.text = shoppingList.creationTime
                tvCount.text = "${shoppingList.checkedItemsCounter}/${shoppingList.allItemCounter}"
                itemView.setOnClickListener {
                    listener.onClickItem(shoppingList)
                }
                ibtnDeleteShoppingItem.setOnClickListener {
                    listener.deleteItem(shoppingList.id!!)
                }
                ibtnEditShoppingItem.setOnClickListener {
                    listener.editItem(shoppingList)
                }
                pbShopList.max = shoppingList.allItemCounter
                pbShopList.progress = shoppingList.checkedItemsCounter
                pbShopList.progressTintList = ColorStateList.valueOf(
                    getProgressStateColor(
                        shoppingList,
                        binding.root.context
                    )
                )
            }

        private fun getProgressStateColor(shoppingList: ShoppingList, context: Context): Int {
            return when {
                shoppingList.checkedItemsCounter == shoppingList.allItemCounter -> {
                    ContextCompat.getColor(context, R.color.progress_green)
                }
                shoppingList.allItemCounter > shoppingList.checkedItemsCounter * 2 -> {
                    ContextCompat.getColor(context, R.color.progress_red)
                }
                else -> {
                    ContextCompat.getColor(context, R.color.progress_yellow)
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder = ItemHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_name_rv_item, parent, false)
            )
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<ShoppingList>() {
        override fun areItemsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean =
            oldItem == newItem
    }

    interface Listener {
        fun deleteItem(id: Int)
        fun editItem(shoppingList: ShoppingList)
        fun onClickItem(shoppingList: ShoppingList)
    }
}