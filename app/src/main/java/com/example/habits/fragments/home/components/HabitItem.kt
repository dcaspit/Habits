package com.example.habits.fragments.home.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.habits.databinding.HabitItemBinding
import com.example.habits.fragments.baseObjects.BaseItem
import com.example.habits.fragments.home.adapters.HabitDateData
import com.example.habits.fragments.home.adapters.HabitItemHorizontalRecyclerAdapter

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

        private val adapter: HabitItemHorizontalRecyclerAdapter by lazy { HabitItemHorizontalRecyclerAdapter() }
        fun bind(habitName: String, habitInterval: HabitIntervals) {
            binding.habitName.text = habitName
            binding.habitInterval.text = habitInterval.name
            val recyclerView = binding.calendar
            adapter.setData(listOf(
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
                HabitDateData("Sun", "14"),
            ))
            recyclerView.adapter = adapter
            val horizonatalLayoutManager = LinearLayoutManager(
                binding.root.context,
                LinearLayoutManager.HORIZONTAL, false
            )
            recyclerView.layoutManager = horizonatalLayoutManager
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