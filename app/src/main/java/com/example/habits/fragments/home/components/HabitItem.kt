package com.example.habits.fragments.home.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.databinding.HabitItemBinding
import com.example.habits.fragments.home.HomeFragmentDirections
import com.example.habits.utils.baseObjects.BaseItem
import com.example.habits.fragments.home.adapters.HabitItemHorizontalRecyclerAdapter
import com.example.habits.models.Habit
import com.example.habits.models.HabitDate

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
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
                HabitDate("Sun", "14"),
            ))
            recyclerView.adapter = adapter
            val horizonatalLayoutManager = LinearLayoutManager(
                binding.root.context,
                LinearLayoutManager.HORIZONTAL, false
            )
            recyclerView.layoutManager = horizonatalLayoutManager
            binding.root.setOnClickListener {
                val action = HomeFragmentDirections.actionHomePageToDetailsPage(Habit(
                    1,
                    habitName,
                    habitInterval,
                    HabitDate("Sun", "14"),
                ))
                binding.root.findNavController().navigate(action)
            }
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