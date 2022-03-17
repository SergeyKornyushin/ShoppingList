package com.example.shoppinglist.activities

import android.app.Application
import com.example.shoppinglist.data_base.MainDatabase

class MainApp: Application() {
    val database by lazy { MainDatabase.getDatabase(this) }
}