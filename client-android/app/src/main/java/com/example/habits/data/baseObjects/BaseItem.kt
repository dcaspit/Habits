package com.example.habits.data.baseObjects

import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.jvm.internal.impl.util.ModuleVisibilityHelper.EMPTY

abstract class BaseItem{
    abstract val viewType: Int
    abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    companion object {
        const val HABIT = 0
        const val ADD_HABIT = 1
        const val EMPTY_HABIT = 2
        const val TITLE = 3
    }
}


