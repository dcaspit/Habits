package com.example.habits.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.R
import com.example.habits.data.baseObjects.BaseItem
import com.example.habits.data.baseObjects.BaseRecyclerAdapter
import com.example.habits.data.models.HabitData
import com.example.habits.data.viewModels.DatabaseViewModel
import com.example.habits.databinding.CalendarDayBinding
import com.example.habits.databinding.FragmentHomeBinding
import com.example.habits.fragments.add.RepeatDailyIn
import com.example.habits.fragments.home.components.EmptyItem
import com.example.habits.fragments.home.components.HabitItem
import com.example.habits.fragments.home.components.HomeTitle
import com.example.habits.utils.displayText
import com.example.habits.utils.getPrimaryColor
import com.example.habits.utils.stringToLocalDate
import com.google.android.material.snackbar.Snackbar
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekDayBinder
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

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

            binding.customBottomBar.inflateMenu(R.menu.bottom_nav_menu)

            setupRecyclerView()
            observeHabits()
            mDatabaseViewModel.getAllHabits()

            binding.floatingActionButton.setOnClickListener {
                val action = HomeFragmentDirections.actionHomePageToAddFragment("")
                binding.root.findNavController().navigate(action)
            }

            return binding.root
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null;
    }

    private fun observeHabits() {
        mDatabaseViewModel.habits.observe(viewLifecycleOwner) {
            if (it.isEmpty()) return@observe
            val todayHabits = it.filter { tupple -> shouldTrackHabitToday(tupple.key) }
            if (todayHabits.isEmpty()) return@observe

            val itemsToDoAnyTime = todayHabits.filter { tuple ->
                isHabitDoAnytime(tuple.key.repeatDailyIn)
            }.map { entry ->
                HabitItem(entry, selectedDate, RepeatDailyIn.ANYTIME)
            }

            val itemsWithoutToDoAnytime = todayHabits.filter { tuple ->
                !isHabitDoAnytime(tuple.key.repeatDailyIn)
            }

            val itemsInMorning = itemsWithoutToDoAnytime.filter { tuple ->
                isHabitInMorning(tuple.key.repeatDailyIn)
            }.map { entry ->
                HabitItem(entry, selectedDate, RepeatDailyIn.MORNING)
            }

            val itemsInAfternoon = itemsWithoutToDoAnytime.filter { tuple ->
                isHabitInAfternoon(tuple.key.repeatDailyIn)
            }.map { entry ->
                HabitItem(entry, selectedDate, RepeatDailyIn.AFTERNOON)
            }

            val itemsInEvening = itemsWithoutToDoAnytime.filter { tuple ->
                isHabitINEvening(tuple.key.repeatDailyIn)
            }.map { entry ->
                HabitItem(entry, selectedDate, RepeatDailyIn.EVENING)
            }

            val items = arrayListOf<BaseItem>().apply {
                if (itemsToDoAnyTime.isNotEmpty()) {
                    add(HomeTitle("DO ANYTIME"))
                    addAll(itemsToDoAnyTime)
                }
                if (itemsInMorning.isNotEmpty()) {
                    add(HomeTitle("IN MORNING"))
                    addAll(itemsInMorning)
                }
                if (itemsInAfternoon.isNotEmpty()) {
                    add(HomeTitle("IN AFTERNOON"))
                    addAll(itemsInAfternoon)
                }
                if (itemsInEvening.isNotEmpty()) {
                    add(HomeTitle("IN EVENING"))
                    addAll(itemsInEvening)
                }
            }
            baseRecyclerAdapter.setData(items)
            binding.recyclerView.scheduleLayoutAnimation()
        }
    }

    private fun isHabitDoAnytime(repeatDailyIn: String): Boolean {
        if (repeatDailyIn.isEmpty()) return true
        val list = repeatDailyIn.split(",")

        list.forEach { repeat ->
            if (repeat == RepeatDailyIn.ANYTIME.ordinal.toString()) {
                return true
            }
        }
        return false
    }


    private fun isHabitInMorning(repeatDailyIn: String): Boolean {
        if (repeatDailyIn.isEmpty()) return false
        val list = repeatDailyIn.split(",")

        list.forEach { repeat ->
            if (repeat == RepeatDailyIn.MORNING.ordinal.toString()) {
                return true
            }
        }

        return false
    }


    private fun isHabitInAfternoon(repeatDailyIn: String): Boolean {
        if (repeatDailyIn.isEmpty()) return false
        val list = repeatDailyIn.split(",")

        list.forEach { repeat ->
            if (repeat == RepeatDailyIn.AFTERNOON.ordinal.toString()) {
                return true
            }
        }

        return false
    }


    private fun isHabitINEvening(repeatDailyIn: String): Boolean {
        if (repeatDailyIn.isEmpty()) return false
        val list = repeatDailyIn.split(",")

        list.forEach { repeat ->
            if (repeat == RepeatDailyIn.EVENING.ordinal.toString()) {
                return true
            }
        }

        return false
    }

    private fun shouldTrackHabitToday(habit: HabitData): Boolean {
        val startDate = stringToLocalDate(habit.startDate)

        // Check if the current date is within the habit's start and end dates
        if (selectedDate.isBefore(startDate)) {
            return false
        }

        val days = habit.trackDays.split(",")

        days.forEach { day ->
            if (day == selectedDate.dayOfWeek.toString()) {
                return true
            }
        }
        return false

        // Determine if the habit should be tracked based on the frequency
//        return when (habit.frequency) {
//            "daily" -> true
//            "weekly" -> (daysSinceStart % 7).toInt() == 0 // Track on every 7th day
//            "monthly" -> today.dayOfMonth == startDate.dayOfMonth // Track on the same day of the month
//            else -> false // Unknown frequency
//        }
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
                        emptyMainList()
                        mDatabaseViewModel.getAllHabits()
                    }
                }
            }

            fun bind(day: WeekDay) {
                this.day = day
                bind.dateText.text = dateFormatter.format(day.date)
                bind.dayText.text = day.date.dayOfWeek.displayText()

                if (day.date == selectedDate) {
                    bind.dateText.setTextColor(getPrimaryColor(context, R.attr.colorSecondary))
                } else {
                    bind.dateText.setTextColor(getPrimaryColor(context, R.attr.colorOnPrimary))
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
        emptyMainList()
    }

    private fun emptyMainList() {
        baseRecyclerAdapter.setData(listOf(EmptyItem()))
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = baseRecyclerAdapter.list[viewHolder.adapterPosition]
                if (deletedItem is HabitItem) {
                    mDatabaseViewModel.deleteHabit(deletedItem.tupple.key)
                    baseRecyclerAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                    // Restore Deleted Item
                    restoreDeletedData(viewHolder.itemView, deletedItem.tupple.key)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: HabitData) {
        val snackBar = Snackbar.make(
            view, "Deleted '${deletedItem.name}'", Snackbar.LENGTH_LONG
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