package com.example.shoppinglist.utils

import android.content.Intent
import com.example.shoppinglist.entities.ShoppingListItem

object ShareHelper {
    fun shareShoppingList(shoppingList: List<ShoppingListItem>, listName: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plane"
        intent.apply {
            putExtra(Intent.EXTRA_TEXT, makeShareText(shoppingList, listName))
        }
        return intent
    }

    private fun makeShareText(shoppingList: List<ShoppingListItem>, listName: String): String{
        val sBuilder = StringBuilder()
        sBuilder.append("<<$listName>>\n")
        var count = 1
        shoppingList.forEach {
            if (it.itemInfo.isEmpty()){
                sBuilder.append("\n ${count++}) ${it.itemName}")
            } else {
                sBuilder.append("\n ${count++}) ${it.itemName} (${it.itemInfo})")
            }
        }
        return sBuilder.toString()
    }
}