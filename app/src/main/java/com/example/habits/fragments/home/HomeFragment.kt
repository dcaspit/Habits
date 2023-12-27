package com.example.habits.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habits.databinding.FragmentHomeBinding
import com.example.habits.utils.baseObjects.BaseRecyclerAdapter
import com.example.habits.fragments.home.components.AddHabitItem
import com.example.habits.fragments.home.components.HabitIntervals
import com.example.habits.fragments.home.components.HabitItem

class HomeFragment: Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        try{
            _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            binding.recyclerView.adapter = BaseRecyclerAdapter(arrayListOf(
                HabitItem("Medidation", HabitIntervals.EVERYDAY),
                HabitItem("Medidation", HabitIntervals.EVERYDAY),
                HabitItem("Medidation", HabitIntervals.EVERYDAY),
                HabitItem("Medidation", HabitIntervals.EVERYDAY),
                AddHabitItem()
            ))
            binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)

            return binding.root
        } catch (e: Exception){
            e.printStackTrace()
        }
        return null;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}