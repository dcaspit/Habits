package com.example.habits.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.habits.utils.baseObjects.BaseItem

class BaseItemDiffUtils(private val oldList: List<BaseItem>,
                        private val newList: List<BaseItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].viewType == newList[newItemPosition].viewType
    }
}