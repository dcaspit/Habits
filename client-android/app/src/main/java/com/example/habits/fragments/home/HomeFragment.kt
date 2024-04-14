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
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
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
import com.example.habits.databinding.CalendarDayBinding
import com.example.habits.databinding.FragmentHomeBinding
import com.example.habits.fragments.home.components.EmptyItem
import com.example.habits.fragments.home.components.HabitItem
import com.example.habits.utils.displayText
import com.example.habits.utils.getColorCompat
import com.example.habits.utils.makeGone
import com.google.android.material.snackbar.Snackbar
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekDayBinder
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class HomeFragment : Fragment() {
    private val mDatabaseViewModel: DatabaseViewModel by viewModels()
    private val baseRecyclerAdapter: BaseRecyclerAdapter by lazy { BaseRecyclerAdapter() }
    private var selectedDate = LocalDate.now()
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    private val binding get() = _binding!!
    private var _binding: FragmentHomeBinding? = null

    override fun onStart() {
        super.onStart()
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        try {
            _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
            //binding.lifecycleOwner = viewLifecycleOwner

            setupRecyclerView()

            mDatabaseViewModel.getAllHabits.observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    baseRecyclerAdapter.setData(listOf(EmptyItem()))
                } else {
                    baseRecyclerAdapter.setData(it.map { habit -> HabitItem(habit) })
                }
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
        class DayViewContainer(view: View) : ViewContainer(view) {
            val bind = CalendarDayBinding.bind(view)
            lateinit var day: WeekDay

            init {
                view.setOnClickListener {
                    if (selectedDate != day.date) {
                        val oldDate = selectedDate
                        selectedDate = day.date
                        binding.viewCalendar.notifyDateChanged(day.date)
                        oldDate?.let { binding.viewCalendar.notifyDateChanged(it) }
                    }
                }
            }

            fun bind(day: WeekDay) {
                this.day = day
                bind.dateText.text = dateFormatter.format(day.date)
                bind.dayText.text = day.date.dayOfWeek.displayText()

                if (day.date == selectedDate) {
                    bind.dateText.setTextColor(view.context.getColorCompat(R.color.example_7_yellow))
                }
                bind.selectedView.isVisible = day.date == selectedDate
            }
        }

        binding.viewCalendar.dayBinder = object : WeekDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: WeekDay) = container.bind(data)
        }

        val currentMonth = YearMonth.now()
        binding.viewCalendar.setup(
            currentMonth.minusMonths(5).atStartOfMonth(),
            currentMonth.plusMonths(5).atEndOfMonth(),
            firstDayOfWeekFromLocale(),
        )
        binding.viewCalendar.scrollToDate(LocalDate.now())
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = baseRecyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        swipeToDelete(recyclerView)
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