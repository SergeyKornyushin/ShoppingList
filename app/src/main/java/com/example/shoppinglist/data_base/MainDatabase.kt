package com.example.shoppinglist.data_base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shoppinglist.entities.*

@Database(
    entities = [LibraryItem::class,
        NoteItem::class,
        ShoppingList::class,
        ShoppingListItem::class],
    version = 1,
    exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {
    abstract fun getDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase::class.java,
                    "shopping_list.db"
                ).build()
                instance
            }

    }
}