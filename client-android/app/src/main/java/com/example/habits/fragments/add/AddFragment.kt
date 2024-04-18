package com.example.habits.fragments.add

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.habits.R
import com.example.habits.data.models.HabitData
import com.example.habits.data.viewModels.DatabaseViewModel
import com.example.habits.databinding.FragmentAddBinding
import com.example.habits.utils.getPrimaryColor
import com.example.habits.utils.localDateToString
import com.example.habits.utils.makeGone
import com.example.habits.utils.makeVisible
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import java.time.DayOfWeek
import java.time.LocalDate

enum class HabitGoal {
    NONE, NUMERIC, DURATION
}

enum class RepeatDailyIn {
    MORNING, AFTERNOON, EVENING, DOANYTIME
}

class AddFragment : Fragment() {
    private val mDatabaseViewModel: DatabaseViewModel by viewModels()
    private val mAddViewModel: AddViewModel by viewModels()

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val titleRes: Int = R.string.add_habit_title

    override fun onStart() {
        super.onStart()
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = context?.getString(titleRes)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStop() {
        super.onStop()
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = context?.getString(R.string.activity_main_title)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)
        observeHabitGoal()
        addHabitGoalsClickListeners()

        observeDays()
        addDaysClickListeners()

        observeRepeatDailys()
        addRepeatDailysClickListeners()

        setAddButtonClickListener()
        return binding.root
    }

    private fun addHabitGoalsClickListeners() {
        binding.goalTimeButton.setOnClickListener {
            mAddViewModel.setHabitGoal(HabitGoal.DURATION)
        }
        binding.goalNumberButton.setOnClickListener {
            mAddViewModel.setHabitGoal(HabitGoal.NUMERIC)
        }
        binding.goalForHabitCheckbox.addOnCheckedStateChangedListener { _, state ->
            if (state == MaterialCheckBox.STATE_CHECKED) {
                mAddViewModel.setHabitGoal(HabitGoal.NUMERIC)
            } else {
                mAddViewModel.setHabitGoal(HabitGoal.NONE)
            }
        }
    }

    private fun observeHabitGoal() {
        mAddViewModel.habitGoal.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            when (it) {
                HabitGoal.NONE -> {
                    binding.llGoalForHabit.makeGone()
                    binding.goalTimeButton.turnOff()
                    binding.goalNumberButton.turnOff()
                }

                HabitGoal.NUMERIC -> {
                    binding.llGoalForHabit.makeVisible()
                    binding.goalTimeButton.turnOff()
                    binding.goalNumberButton.turnOn(getPrimaryColor(context))
                    binding.textFieldGoalForHabit.hint = "0 times"
                }

                HabitGoal.DURATION -> {
                    binding.llGoalForHabit.makeVisible()
                    binding.goalNumberButton.turnOff()
                    binding.goalTimeButton.turnOn(getPrimaryColor(context))
                    binding.textFieldGoalForHabit.hint = "0 minutes"
                }
            }
        }
    }

    private fun addRepeatDailysClickListeners() {
        binding.morning.addRepeatDailyClickListener(RepeatDailyIn.MORNING)
        binding.afternoon.addRepeatDailyClickListener(RepeatDailyIn.AFTERNOON)
        binding.evening.addRepeatDailyClickListener(RepeatDailyIn.EVENING)
        binding.repeatDailyInCheckbox.addRepeatDailtDayCheckboxClickListener()
    }

    private fun setAddButtonClickListener() {
        binding.buttonAdd.setOnClickListener {
            val days = StringBuilder()
            mAddViewModel.days.value?.forEach {
                days.append(it)
                days.append(",")
            }

            val habitGoal = StringBuilder()
            when(mAddViewModel.habitGoal.value) {
                HabitGoal.NONE -> {
                    habitGoal.append(HabitGoal.NONE.ordinal.toString())
                    habitGoal.append(",")
                }
                HabitGoal.NUMERIC -> {
                    habitGoal.append(HabitGoal.NUMERIC.ordinal.toString())
                    habitGoal.append(",")
                    habitGoal.append(binding.textFieldGoalForHabit.text)
                }
                HabitGoal.DURATION -> {
                    habitGoal.append(HabitGoal.DURATION.ordinal.toString())
                    habitGoal.append(",")
                    habitGoal.append(binding.textFieldGoalForHabit.text)
                }
                null -> {}
            }

            val repeatDailyIn = StringBuilder()
            mAddViewModel.repeatDailyIn.value?.forEach { dailyIn ->
                repeatDailyIn.append(dailyIn.ordinal)
                repeatDailyIn.append(",")
            }

            mDatabaseViewModel.insertHabit(
                HabitData(
                    binding.textFieldName.text.toString(),
                    "",
                    "daily",
                    localDateToString(LocalDate.now()),
                    null,
                    days.toString(),
                    habitGoal.toString(),
                    repeatDailyIn.toString()
                )
            )
            findNavController().popBackStack()
        }
    }

    private fun addDaysClickListeners() {
        binding.monday.addDayClickListener(DayOfWeek.MONDAY)
        binding.thesday.addDayClickListener(DayOfWeek.TUESDAY)
        binding.wednesday.addDayClickListener(DayOfWeek.WEDNESDAY)
        binding.thursday.addDayClickListener(DayOfWeek.THURSDAY)
        binding.friday.addDayClickListener(DayOfWeek.FRIDAY)
        binding.saturday.addDayClickListener(DayOfWeek.SATURDAY)
        binding.sunday.addDayClickListener(DayOfWeek.SUNDAY)
        binding.repeatEveryDayCheckbox.addDayCheckboxClickListener()
    }

    private fun observeDays() {
        mAddViewModel.days.observe(viewLifecycleOwner) {
            if (it.isEmpty()) return@observe

            val color = getPrimaryColor(context)
            binding.monday.turnOff()
            binding.thesday.turnOff()
            binding.wednesday.turnOff()
            binding.thursday.turnOff()
            binding.friday.turnOff()
            binding.saturday.turnOff()
            binding.sunday.turnOff()

            it.forEach { day ->
                when (day) {
                    DayOfWeek.MONDAY -> {
                        binding.monday.turnOn(color)
                    }

                    DayOfWeek.TUESDAY -> {
                        binding.thesday.turnOn(color)
                    }

                    DayOfWeek.WEDNESDAY -> {
                        binding.wednesday.turnOn(color)
                    }

                    DayOfWeek.THURSDAY -> {
                        binding.thursday.turnOn(color)
                    }

                    DayOfWeek.FRIDAY -> {
                        binding.friday.turnOn(color)
                    }

                    DayOfWeek.SATURDAY -> {
                        binding.saturday.turnOn(color)
                    }

                    DayOfWeek.SUNDAY -> {
                        binding.sunday.turnOn(color)
                    }
                }
            }
        }
    }

    private fun observeRepeatDailys() {
        mAddViewModel.repeatDailyIn.observe(viewLifecycleOwner) {
            if (it.isEmpty()) return@observe

            val color = getPrimaryColor(context)
            binding.morning.turnOff()
            binding.afternoon.turnOff()
            binding.evening.turnOff()

            it.forEach { repeatDaily ->
                when (repeatDaily) {
                    RepeatDailyIn.MORNING -> {
                        binding.morning.turnOn(color)
                    }

                    RepeatDailyIn.AFTERNOON -> {
                        binding.afternoon.turnOn(color)
                    }

                    RepeatDailyIn.EVENING -> {
                        binding.evening.turnOn(color)
                    }
                    RepeatDailyIn.DOANYTIME -> {
                        binding.morning.turnOn(color)
                        binding.afternoon.turnOn(color)
                        binding.evening.turnOn(color)
                    }
                }
            }
        }
    }

    private fun MaterialCheckBox.addDayCheckboxClickListener() {
        addOnCheckedStateChangedListener { _, state ->
            if (state == MaterialCheckBox.STATE_CHECKED) {
                mAddViewModel.setDays(
                    mutableSetOf(
                        DayOfWeek.MONDAY,
                        DayOfWeek.TUESDAY,
                        DayOfWeek.WEDNESDAY,
                        DayOfWeek.THURSDAY,
                        DayOfWeek.FRIDAY,
                        DayOfWeek.SATURDAY,
                        DayOfWeek.SUNDAY
                    )
                )
            } else {
                mAddViewModel.setDays(
                    mutableSetOf(
                        DayOfWeek.MONDAY,
                    )
                )
            }
        }
    }

    private fun MaterialCheckBox.addRepeatDailtDayCheckboxClickListener() {
        addOnCheckedStateChangedListener { _, state ->
            if (state == MaterialCheckBox.STATE_CHECKED) {
                mAddViewModel.setRepeatDailys(
                    mutableSetOf(
                        RepeatDailyIn.MORNING,
                        RepeatDailyIn.AFTERNOON,
                        RepeatDailyIn.EVENING,
                    )
                )
            } else {
                mAddViewModel.setRepeatDailys(
                    mutableSetOf(
                        RepeatDailyIn.MORNING,
                    )
                )
            }
        }
    }

    private fun MaterialButton.addDayClickListener(dayOfWeek: DayOfWeek) {
        val color = getPrimaryColor(context)
        setOnClickListener {
            toggleButton(color, dayOfWeek)
        }
    }

    private fun MaterialButton.addRepeatDailyClickListener(repeatDailyIn: RepeatDailyIn) {
        val color = getPrimaryColor(context)
        setOnClickListener {
            toggleRepeatDailyButton(color, repeatDailyIn)
        }
    }

    private fun MaterialButton.toggleRepeatDailyButton(color: Int, repeatDailyIn: RepeatDailyIn) {
        if (backgroundTintList == null) {
            turnOn(color)
            mAddViewModel.addRepeatDaily(repeatDailyIn)
        } else {
            turnOff()
            mAddViewModel.removeRepeatDaily(repeatDailyIn)
        }
    }

    private fun MaterialButton.toggleButton(color: Int, dayOfWeek: DayOfWeek) {
        if (backgroundTintList == null) {
            turnOn(color)
            mAddViewModel.addDay(dayOfWeek)
        } else {
            turnOff()
            mAddViewModel.removeDay(dayOfWeek)
        }
    }

    private fun MaterialButton.turnOff() {
        backgroundTintList = null
    }

    private fun MaterialButton.turnOn(color: Int) {
        backgroundTintList = ColorStateList.valueOf(color)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}