package com.example.shoppinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "shopping_list_names")
data class ShoppingList(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "list_name")
    val listName: String,

    @ColumnInfo(name = "creation_time")
    val creationTime: String,

    @ColumnInfo(name = "all_item_count")
    val allItemCounter: Int,

    @ColumnInfo(name = "checked_items_counter")
    val checkedItemsCounter: Int,

    @ColumnInfo(name = "items_id")
    val itemsId: String
): Serializable
