package com.example.shoppinglist.data_base

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.entities.NoteItem
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.entities.ShoppingListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Insert
    suspend fun insertNote(note: NoteItem)

    @Query("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Update
    suspend fun updateNote(noteItem: NoteItem)

    @Insert
    suspend fun insertShoppingList(name: ShoppingList)

    @Query("SELECT * FROM shopping_list")
    fun getAllShoppingLists(): Flow<List<ShoppingList>>

    @Query("DELETE FROM shopping_list WHERE id IS :id")
    suspend fun deleteShoppingList(id: Int)

    @Update
    suspend fun updateShoppingList(shoppingList: ShoppingList)

    @Insert
    suspend fun insertShoppingListItem(shoppingListItem: ShoppingListItem)

    @Query("SELECT * FROM shopping_list_item WHERE list_id LIKE :listId")
    fun getAllShoppingListItems(listId: Int): Flow<List<ShoppingListItem>>

    @Update
    suspend fun updateShoppingListItem(shoppingListItem: ShoppingListItem)
}