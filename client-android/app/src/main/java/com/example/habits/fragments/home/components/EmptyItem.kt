package com.example.habits.fragments.home.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.data.baseObjects.BaseItem
import com.example.habits.databinding.AddHabitItemBinding
import com.example.habits.databinding.EmptyItemBinding

class EmptyItem: BaseItem() {
    override val viewType: Int
        get() = EMPTY_HABIT

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    class ViewHolder(
        val binding: EmptyItemBinding
    ): RecyclerView.ViewHolder(binding.root)

    companion object {
        fun create(
            parent: ViewGroup
        ): RecyclerView.ViewHolder {
            val binding = EmptyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }
    }
}