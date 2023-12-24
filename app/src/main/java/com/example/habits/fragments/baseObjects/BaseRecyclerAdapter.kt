package com.example.habits.fragments.baseObjects

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.fragments.home.AddHabitItem
import com.example.habits.fragments.home.HabitItem

class BaseRecyclerAdapter(
    val list: ArrayList<BaseItem>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == BaseItem.HABIT) {
            return HabitItem.create(parent)
        }
        return AddHabitItem.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        list[position].onBindViewHolder(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    override fun getItemCount(): Int = list.size
}
