package com.example.habits.fragments.home.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.databinding.HabitItemBinding
import com.example.habits.fragments.home.HomeFragmentDirections
import com.example.habits.data.baseObjects.BaseItem
import com.example.habits.data.models.HabitAction
import com.example.habits.fragments.home.adapters.HabitItemHorizontalRecyclerAdapter
import com.example.habits.data.models.HabitData
import com.example.habits.fragments.add.HabitGoal
import com.example.habits.utils.localDateToString
import com.example.habits.utils.makeGone
import java.time.LocalDate

class HabitItem(val tupple: Map.Entry<HabitData, List<HabitAction>>, val selectedDate: LocalDate) : BaseItem() {

    override val viewType: Int = HABIT

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val habitAction =  tupple.value.find { action -> action.selectedDate == localDateToString(selectedDate) }
            holder.bind(tupple.key, habitAction, selectedDate)
        }
    }

    class ViewHolder(
        private val binding: HabitItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val adapter: HabitItemHorizontalRecyclerAdapter by lazy { HabitItemHorizontalRecyclerAdapter() }
        fun bind(habitData: HabitData, habitAction: HabitAction?, selectedDate: LocalDate) {
            binding.habitName.text = habitData.name
            binding.habitInterval.text = habitData.frequency

            setHabitProgressBar(habitData, habitAction)

            binding.root.setOnClickListener {
                val action = habitData.id?.let { id ->
                    HomeFragmentDirections.actionHomePageToDetailsPage(id, selectedDate.toString())
                }
                if (action != null) {
                    binding.root.findNavController().navigate(action)
                }
            }
        }

        private fun setHabitProgressBar(habitData: HabitData, habitAction: HabitAction?) {
            val types = habitAction?.habitType?.split(",") ?: habitData.habitType.split(",")

            val type = types[0]
            val count = types[1]

            if (type.isEmpty()) {
                binding.progressIndicator.makeGone()
                binding.progressText.makeGone()
                return
            }

            if (type == HabitGoal.NONE.ordinal.toString()) {
                binding.progressIndicator.max = 1
                if(habitAction != null && habitAction.completed) {
                    binding.progressText.text = "1/1"
                    binding.progressIndicator.setProgress(1, true)
                } else{
                    binding.progressText.text = "0/1"
                    binding.progressIndicator.setProgress(0, true)
                }
            } else if (count.isNotEmpty()) {
                binding.progressIndicator.max = count.toInt()
                if(habitAction != null) {
                    binding.progressIndicator.setProgress(habitAction.partialAmount, true)
                    binding.progressText.text = "${habitAction.partialAmount}/$count"
                } else {
                    binding.progressIndicator.setProgress(0, true)
                    binding.progressText.text = "0/$count"
                }
            } else {
                binding.progressIndicator.setProgress(0, true)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup
        ): RecyclerView.ViewHolder {
            val binding =
                HabitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }
    }
}