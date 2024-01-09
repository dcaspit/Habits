package com.example.habits.fragments.home.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.databinding.AddHabitItemBinding
import com.example.habits.data.baseObjects.BaseItem
import com.example.habits.fragments.home.HomeFragmentDirections

class HabitAddItem: BaseItem() {

    override val viewType: Int
        get() = ADD_HABIT

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder) {
            holder.bind()
        }
    }

    class ViewHolder(
        private val binding: AddHabitItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.text.text = "Add Habit"
        }
    }

    companion object {
        fun create(
            parent: ViewGroup
        ): RecyclerView.ViewHolder {
            val binding = AddHabitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }
    }
}