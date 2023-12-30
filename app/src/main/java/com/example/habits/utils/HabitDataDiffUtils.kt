package com.example.habits.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.habits.data.models.HabitDate

class HabitDataDiffUtils(
    private val oldList: List<HabitDate>,
    private val newList: List<HabitDate>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].day == newList[newItemPosition].day
                && oldList[oldItemPosition].num == newList[newItemPosition].num
    }
}