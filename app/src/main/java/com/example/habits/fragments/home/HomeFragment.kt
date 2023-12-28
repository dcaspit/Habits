package com.example.habits.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habits.data.viewModels.HabitsViewModel
import com.example.habits.databinding.FragmentHomeBinding
import com.example.habits.utils.baseObjects.BaseRecyclerAdapter
import com.example.habits.fragments.home.components.AddHabitItem
import com.example.habits.fragments.home.components.HabitIntervals
import com.example.habits.fragments.home.components.HabitItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val habitsViewModel: HabitsViewModel by viewModels()

    private val binding get() = _binding!!
    private var _binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        try {
            _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            binding.recyclerView.adapter = BaseRecyclerAdapter(arrayListOf())
            binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)

            habitsViewModel.habits.observe(viewLifecycleOwner) {
                val adapter = binding.recyclerView.adapter
                if (adapter != null && adapter is BaseRecyclerAdapter) {
                    adapter.setData(it.map { habit -> HabitItem(habit.title, habit.interval) })
                }
            }

            return binding.root
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            habitsViewModel.fetchHabits()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}