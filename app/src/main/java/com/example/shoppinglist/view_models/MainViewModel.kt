package com.example.shoppinglist.view_models

import androidx.lifecycle.*
import com.example.shoppinglist.data_base.MainDatabase
import com.example.shoppinglist.entities.LibraryItem
import com.example.shoppinglist.entities.NoteItem
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.entities.ShoppingListItem
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(database: MainDatabase) : ViewModel() {
    private val dao = database.getDao()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allShoppingLists: LiveData<List<ShoppingList>> = dao.getAllShoppingLists().asLiveData()
    val libraryItems = MutableLiveData<List<LibraryItem>>()

    //-------------notes-------------------
    fun insertNote(noteItem: NoteItem) = viewModelScope.launch {
        dao.insertNote(noteItem)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    fun updateNote(noteItem: NoteItem) = viewModelScope.launch {
        dao.updateNote(noteItem)
    }
    //-------------notes-------------------

    //-------------shopping_list-------------------
    fun insertShoppingList(listName: ShoppingList) = viewModelScope.launch {
        dao.insertShoppingList(listName)
    }

    fun deleteShoppingList(id: Int) = viewModelScope.launch {
        dao.deleteShoppingList(id)
        dao.deleteShoppingItemsByParentListId(id)
    }

    fun updateShoppingList(shoppingList: ShoppingList) = viewModelScope.launch {
        dao.updateShoppingList(shoppingList)
    }
    //-------------shopping_list-------------------

    //-------------shopping_list_item-------------------
    fun insertShoppingListItem(shoppingListItem: ShoppingListItem) = viewModelScope.launch {
        dao.insertShoppingListItem(shoppingListItem)
        if (isLibraryItemNotExist(shoppingListItem.itemName)) dao.insertLibraryItem(
            LibraryItem(
                null,
                shoppingListItem.itemName
            )
        )
    }

    fun getAllItemsFromList(listId: Int): LiveData<List<ShoppingListItem>> =
        dao.getAllShoppingListItems(listId).asLiveData()

    fun updateShoppingListItem(shoppingListItem: ShoppingListItem) = viewModelScope.launch {
        dao.updateShoppingListItem(shoppingListItem)
    }

    fun clearShoppingList(shoppingList: ShoppingList) = viewModelScope.launch {
        dao.deleteShoppingItemsByParentListId(shoppingList.id!!)
    }
    //-------------shopping_list_item-------------------

    //-------------library-------------------
    private suspend fun isLibraryItemNotExist(item: String): Boolean {
        return dao.getAllLibraryItems(item).isEmpty()
    }
    fun getAllLibraryItems(name: String) = viewModelScope.launch{
        libraryItems.postValue(dao.getAllLibraryItems(name))
    }
    fun updateLibraryItem(libraryItem: LibraryItem) = viewModelScope.launch {
        dao.updateLibraryItem(libraryItem)
    }
    fun deleteLibraryItem(id: Int) = viewModelScope.launch {
        dao.deleteLibraryItem(id)
    }
    //-------------library-------------------
    class MainViewModelFactory(private val database: MainDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }
    }
}