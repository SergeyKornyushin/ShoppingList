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
import com.example.shoppinglist.entities.NoteItem
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.utils.HtmlManager

class ShoppingListAdapter(private val listener:Listener) : ListAdapter<ShoppingList, ShoppingListAdapter.ItemHolder>(ItemComparator()) {

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

                itemView.setOnClickListener {

                }
                ibtnDeleteShoppingItem.setOnClickListener {
                    listener.deleteItem(shoppingList.id!!)
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

    interface Listener{
        fun deleteItem(id: Int)
        fun onClickItem(noteItem: NoteItem)
    }
}