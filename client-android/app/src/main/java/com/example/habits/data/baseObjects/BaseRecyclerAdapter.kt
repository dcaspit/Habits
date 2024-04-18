package com.example.habits.data.baseObjects

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.fragments.home.components.EmptyItem
import com.example.habits.fragments.home.components.HabitAddItem
import com.example.habits.fragments.home.components.HabitItem
import com.example.habits.fragments.home.components.HomeTitle
import com.example.habits.utils.BaseItemDiffUtils

class BaseRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list = emptyList<BaseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            BaseItem.HABIT -> return HabitItem.create(parent)
            BaseItem.EMPTY_HABIT -> return EmptyItem.create(parent)
            BaseItem.TITLE -> return HomeTitle.create(parent)
        }
        return HabitAddItem.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        list[position].onBindViewHolder(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    override fun getItemCount(): Int = list.size

    fun setData(newList: List<BaseItem>) {
        val jsonMovieDiffUtil = BaseItemDiffUtils(list, newList)
        val movieDiffResult = DiffUtil.calculateDiff(jsonMovieDiffUtil)
        this.list = ArrayList(newList)
        movieDiffResult.dispatchUpdatesTo(this)
    }
}
