package com.example.shoppinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list_item")
data class ShoppingListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "item_name")
    val itemName: String,

    @ColumnInfo(name = "item_info")
    val itemInfo: String?,

    @ColumnInfo(name = "item_checked")
    val checkItem: Int = 0,

    @ColumnInfo(name = "list_id")
    val itemId: Int,

    @ColumnInfo(name = "item_type")
    val itemType: Int = 0
)
