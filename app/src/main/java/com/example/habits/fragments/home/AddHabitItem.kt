package com.example.habits.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.databinding.HabitItemBinding
import com.example.habits.fragments.baseObjects.BaseItem

class AddHabitItem: BaseItem() {

    override val viewType: Int
        get() = ADD_HABIT

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder) {
            holder.bind(position)
        }
    }

    class ViewHolder(
        private val binding: HabitItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.text.text = "Add Habit" + position
        }
    }

    companion object {
        fun create(
            parent: ViewGroup
        ): RecyclerView.ViewHolder {
            val binding = HabitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }
    }
}