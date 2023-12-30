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
import com.example.habits.fragments.home.components.HabitAddItem
import com.example.habits.data.baseObjects.BaseRecyclerAdapter
import com.example.habits.fragments.home.components.HabitItem
import com.example.habits.data.baseObjects.BaseItem
import com.example.habits.data.viewModels.DatabaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val mDatabaseViewModel: DatabaseViewModel by viewModels()

    private val binding get() = _binding!!
    private var _binding: FragmentHomeBinding? = null

    private val baseRecyclerAdapter: BaseRecyclerAdapter by lazy { BaseRecyclerAdapter(arrayListOf()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        try {
            _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            binding.recyclerView.adapter = baseRecyclerAdapter
            binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)

            mDatabaseViewModel.getAllHabits.observe(viewLifecycleOwner) {
                val items: List<BaseItem> = it.map { habit -> HabitItem(habit) }
                val updatedItems = items + HabitAddItem()
                baseRecyclerAdapter.setData(updatedItems)
                binding.recyclerView.scheduleLayoutAnimation()
            }

            return binding.root
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}