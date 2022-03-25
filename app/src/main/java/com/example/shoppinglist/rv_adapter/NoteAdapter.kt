package com.example.shoppinglist.rv_adapter

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.NoteRvItemBinding
import com.example.shoppinglist.entities.NoteItem
import com.example.shoppinglist.utils.HtmlManager
import com.example.shoppinglist.utils.TimeManager

class NoteAdapter(private val listener: Listener, private val sharedPreferences: SharedPreferences) :
    ListAdapter<NoteItem, NoteAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder =
        ItemHolder.create(parent)

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener, sharedPreferences)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = NoteRvItemBinding.bind(view)

        fun setData(noteItem: NoteItem, listener: Listener, sharedPreferences: SharedPreferences) =
            with(binding) {
                tvTitle.text = noteItem.title
                tvDescription.text = HtmlManager.getFromHtml(noteItem.content).trim()
                tvTime.text = TimeManager.getTimeFormat(noteItem.creationTime, sharedPreferences)
                itemView.setOnClickListener {
                    listener.onClickItem(noteItem)
                }
                ibtnDelete.setOnClickListener {
                    listener.deleteItem(noteItem.id!!)
                }
            }

        companion object {
            fun create(parent: ViewGroup): ItemHolder = ItemHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.note_rv_item, parent, false)
            )
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<NoteItem>() {
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean =
            oldItem == newItem
    }

    interface Listener {
        fun deleteItem(id: Int)
        fun onClickItem(noteItem: NoteItem)
    }
}