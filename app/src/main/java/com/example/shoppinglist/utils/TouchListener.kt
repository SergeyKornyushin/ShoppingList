package com.example.shoppinglist.utils

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

class TouchListener: View.OnTouchListener {
    var xDelta = 0.0f
    var yDelta = 0.0f
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                xDelta = view.x - event.rawX
                yDelta = view.y - event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                view.x = xDelta + event.rawX
                view.y = yDelta + event.rawY
            }
        }
        return true
    }
}