package com.example.habits.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.databinding.HabitItemBinding
import com.example.habits.fragments.baseObjects.BaseItem

enum class HabitIntervals {
    EVERYDAY,
    EVEYWEEK
}

class HabitItem(
    private val habitName: String,
    private val habitInterval: HabitIntervals
) : BaseItem() {

    override val viewType: Int = HABIT

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder) {
            holder.bind(habitName, habitInterval)
        }
    }

    class ViewHolder(
        private val binding: HabitItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(habitName: String, habitInterval: HabitIntervals) {
            binding.habitName.text = habitName
            binding.habitInterval.text = habitInterval.name
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