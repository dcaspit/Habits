package com.example.habits.utils.baseObjects

import androidx.recyclerview.widget.RecyclerView

abstract class BaseItem{
    abstract val viewType: Int
    abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    companion object {
        const val HABIT = 0
        const val ADD_HABIT = 1
    }
}


