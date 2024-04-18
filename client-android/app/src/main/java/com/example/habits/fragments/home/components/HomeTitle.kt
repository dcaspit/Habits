package com.example.habits.fragments.home.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.data.baseObjects.BaseItem
import com.example.habits.databinding.HabitItemBinding
import com.example.habits.databinding.HomeTitleBinding

class HomeTitle(
    val title: String
): BaseItem() {
    override val viewType: Int = TITLE

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder) {
            holder.bind(title)
        }
    }

    class ViewHolder(
        private val binding: HomeTitleBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.title.text = title
        }
    }

    companion object {
        fun create(parent: ViewGroup): RecyclerView.ViewHolder {
            val binding = HomeTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }
    }
}