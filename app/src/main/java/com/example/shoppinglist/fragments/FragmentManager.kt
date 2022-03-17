package com.example.shoppinglist.fragments

import androidx.appcompat.app.AppCompatActivity
import com.example.shoppinglist.R

object FragmentManager {
    var currentFrag: BaseFragment? = null

    fun setFragment(newFrag: BaseFragment, activity: AppCompatActivity) {
//        val transaction = activity.supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fragment_holder, newFrag)
//        transaction.commit()

        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_holder, newFrag)
            .commit()

        currentFrag = newFrag

    }
}