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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.MainActivity
import com.example.habits.R
import com.example.habits.data.baseObjects.BaseRecyclerAdapter
import com.example.habits.data.models.HabitData
import com.example.habits.data.viewModels.DatabaseViewModel
import com.example.habits.databinding.FragmentHomeBinding
import com.example.habits.fragments.home.components.HabitItem
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    private val mDatabaseViewModel: DatabaseViewModel by viewModels()

    private val binding get() = _binding!!
    private var _binding: FragmentHomeBinding? = null

    private val baseRecyclerAdapter: BaseRecyclerAdapter by lazy { BaseRecyclerAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        try {
            _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
            //binding.lifecycleOwner = viewLifecycleOwner

            setupRecyclerView()

            sharedPreferences = requireActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE)

            mDatabaseViewModel.getAllHabits.observe(viewLifecycleOwner) {
                baseRecyclerAdapter.setData(it.map { habit -> HabitItem(habit) })
                binding.recyclerView.scheduleLayoutAnimation()
            }

            binding.floatingActionButton.setOnClickListener {
                val action =  HomeFragmentDirections.actionHomePageToAddFragment()
                binding.root.findNavController().navigate(action)
            }

            return binding.root
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val menuHost: MenuHost = requireActivity()
//        menuHost.addMenuProvider(object : MenuProvider {
//            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                menuInflater.inflate(R.menu.list_fragment_menu, menu)
//            }
//
//            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                when(menuItem.itemId) {
//                    R.id.menu_theme -> {
//                        val nightMode = sharedPreferences?.getBoolean("night", false)
//
//                        if(nightMode == true) {
//                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                            editor = sharedPreferences?.edit()
//                            editor?.putBoolean("night", false)
//                        } else {
//                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                            editor = sharedPreferences?.edit()
//                            editor?.putBoolean("night", true)
//                        }
//                        editor?.apply()
//                    }
//                }
//                return true
//            }
//        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = baseRecyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        //swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = baseRecyclerAdapter.list[viewHolder.adapterPosition]
                if (deletedItem is HabitItem) {
                    mDatabaseViewModel.deleteHabit(deletedItem.habitData)
                    baseRecyclerAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                    // Restore Deleted Item
                    restoreDeletedData(viewHolder.itemView, deletedItem.habitData)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: HabitData) {
        val snackBar = Snackbar.make(
            view, "Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            mDatabaseViewModel.insertHabit(deletedItem)
        }
        snackBar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}