package com.example.habits.fragments.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.databinding.DateItemBinding
import com.example.habits.utils.HabitDataDiffUtils

class HabitItemHorizontalRecyclerAdapter: RecyclerView.Adapter<HabitItemHorizontalRecyclerAdapter.HabitDateViewHolder>() {

    private var dateList = emptyList<HabitDateData>()

    class HabitDateViewHolder(
        val binding: DateItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(habitDateData: HabitDateData) {
            binding.dayName.text = habitDateData.day
            binding.dayNum.text = habitDateData.num
        }

        companion object {
            fun from(parent: ViewGroup): HabitDateViewHolder {
                val binding = DateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return HabitDateViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitDateViewHolder = HabitDateViewHolder.from(parent)

    override fun getItemCount(): Int = dateList.size

    override fun onBindViewHolder(holder: HabitDateViewHolder, position: Int) {
        val currentItem = dateList[position]
        holder.bind(currentItem)
    }

    fun setData(habitData: List<HabitDateData>){
        val jsonMovieDiffUtil = HabitDataDiffUtils(dateList, habitData)
        val movieDiffResult = DiffUtil.calculateDiff(jsonMovieDiffUtil)
        this.dateList = habitData
        movieDiffResult.dispatchUpdatesTo(this)
    }
}

data class HabitDateData(
    val day: String,
    val num: String
)