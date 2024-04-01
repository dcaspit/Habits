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
import com.example.habits.fragments.home.components.HabitItem
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

    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    private val mDatabaseViewModel: DatabaseViewModel by viewModels()

    private val binding get() = _binding!!
    private var _binding: FragmentHomeBinding? = null

    private val baseRecyclerAdapter: BaseRecyclerAdapter by lazy { BaseRecyclerAdapter() }

    private var selectedDate = LocalDate.now()

    private val dateFormatter = DateTimeFormatter.ofPattern("dd")

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
        class DayViewContainer(view: View) : ViewContainer(view) {
            val bind = CalendarDayBinding.bind(view)
            lateinit var day: WeekDay

            init {
                view.setOnClickListener {
                    if (selectedDate != day.date) {
                        val oldDate = selectedDate
                        selectedDate = day.date
                        binding.exSevenCalendar.notifyDateChanged(day.date)
                        oldDate?.let { binding.exSevenCalendar.notifyDateChanged(it) }
                    }
                }
            }

            fun bind(day: WeekDay) {
                this.day = day
                bind.exSevenDateText.text = dateFormatter.format(day.date)
                bind.exSevenDayText.text = day.date.dayOfWeek.displayText()

                val colorRes = if (day.date == selectedDate) {
                    R.color.example_7_yellow
                } else {
                    R.color.example_7_white
                }
                bind.exSevenDateText.setTextColor(view.context.getColorCompat(colorRes))
                bind.exSevenSelectedView.isVisible = day.date == selectedDate
            }
        }

        binding.exSevenCalendar.dayBinder = object : WeekDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: WeekDay) = container.bind(data)
        }

        val currentMonth = YearMonth.now()
        binding.exSevenCalendar.setup(
            currentMonth.minusMonths(5).atStartOfMonth(),
            currentMonth.plusMonths(5).atEndOfMonth(),
            firstDayOfWeekFromLocale(),
        )
        binding.exSevenCalendar.scrollToDate(LocalDate.now())
    }

    fun Context.getColorCompat(@ColorRes color: Int) =
        ContextCompat.getColor(this, color)

    fun YearMonth.displayText(short: Boolean = false): String {
        return "${this.month.displayText(short = short)} ${this.year}"
    }

    fun Month.displayText(short: Boolean = true): String {
        val style = if (short) TextStyle.SHORT else TextStyle.FULL
        return getDisplayName(style, Locale.ENGLISH)
    }

    fun DayOfWeek.displayText(uppercase: Boolean = false): String {
        return getDisplayName(TextStyle.SHORT, Locale.ENGLISH).let { value ->
            if (uppercase) value.uppercase(Locale.ENGLISH) else value
        }
    }


    fun getWeekPageTitle(week: Week): String {
        val firstDate = week.days.first().date
        val lastDate = week.days.last().date
        return when {
            firstDate.yearMonth == lastDate.yearMonth -> {
                firstDate.yearMonth.displayText()
            }
            firstDate.year == lastDate.year -> {
                "${firstDate.month.displayText(short = false)} - ${lastDate.yearMonth.displayText()}"
            }
            else -> {
                "${firstDate.yearMonth.displayText()} - ${lastDate.yearMonth.displayText()}"
            }
        }
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