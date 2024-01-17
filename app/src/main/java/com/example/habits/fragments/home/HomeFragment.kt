package com.example.habits.fragments.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habits.R
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

            sharedPreferences = requireActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE)

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

    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    R.id.menu_theme -> {
                        val nightMode = sharedPreferences?.getBoolean("night", false)

                        if(nightMode == true) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            editor = sharedPreferences?.edit()
                            editor?.putBoolean("night", false)
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            editor = sharedPreferences?.edit()
                            editor?.putBoolean("night", true)
                        }
                        editor?.apply()
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}