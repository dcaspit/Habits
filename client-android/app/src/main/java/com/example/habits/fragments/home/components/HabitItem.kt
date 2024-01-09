package com.example.habits.fragments.home.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.databinding.HabitItemBinding
import com.example.habits.fragments.home.HomeFragmentDirections
import com.example.habits.data.baseObjects.BaseItem
import com.example.habits.fragments.home.adapters.HabitItemHorizontalRecyclerAdapter
import com.example.habits.data.models.HabitData
import com.example.habits.data.models.HabitDate
import com.example.habits.data.models.HabitIntervals

class HabitItem(val habitData: HabitData) : BaseItem() {

    override val viewType: Int = HABIT

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder) {
            holder.bind(habitData)
        }
    }

    class ViewHolder(
        private val binding: HabitItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        private val adapter: HabitItemHorizontalRecyclerAdapter by lazy { HabitItemHorizontalRecyclerAdapter() }
        fun bind(habitData: HabitData) {
            binding.habitName.text = habitData.title
            binding.habitInterval.text = HabitIntervals.values().find { it.ordinal == habitData.interval }?.name ?: ""
            val recyclerView = binding.calendar
            adapter.setData(HabitDateCreator.getListOfHabitDates())
            recyclerView.adapter = adapter
            val horizonatalLayoutManager = LinearLayoutManager(
                binding.root.context,
                LinearLayoutManager.HORIZONTAL, false
            )
            recyclerView.layoutManager = horizonatalLayoutManager
            binding.root.setOnClickListener {
                val action = habitData.id?.let { id ->
                    HomeFragmentDirections.actionHomePageToDetailsPage(id)
                }
                if (action != null) {
                    binding.root.findNavController().navigate(action)
                }
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