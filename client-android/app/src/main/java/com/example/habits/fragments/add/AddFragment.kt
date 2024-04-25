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
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.habits.R
import com.example.habits.data.models.HabitData
import com.example.habits.data.notifcations.NotifyWorker
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
    MORNING, AFTERNOON, EVENING, ANYTIME
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

        val colorList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_activated),  // Disabled
                intArrayOf(android.R.attr.state_focused)    // Enabled
            ),
            intArrayOf(
                getPrimaryColor(context, R.attr.colorOnPrimary),     // The color for the Disabled state
                getPrimaryColor(context, R.attr.colorSecondary)        // The color for the Enabled state
            )
        )

        val hintColorList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_activated),  // Disabled
                intArrayOf(android.R.attr.state_focused)    // Enabled
            ),
            intArrayOf(
                getPrimaryColor(context, R.attr.colorOnPrimary),     // The color for the Disabled state
                getPrimaryColor(context, R.attr.colorOnPrimary)        // The color for the Enabled state
            )
        )
//
        binding.textFieldLayout.setBoxStrokeColorStateList(colorList)
        binding.textFieldLayout.counterTextColor = hintColorList
        binding.textFieldLayout.hintTextColor = colorList

        observeHabitGoal()
        addHabitGoalsClickListeners()

        observeDays()
        addDaysClickListeners()

        observeRepeatDailys()
        addRepeatDailysClickListeners()

        binding.tvNotification.setOnClickListener {
            val notificationRequest: WorkRequest = OneTimeWorkRequestBuilder<NotifyWorker>().build()
            WorkManager.getInstance(binding.root.context).enqueue(notificationRequest)
        }

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
                    binding.goalNumberButton.turnOn()
                    binding.textFieldGoalForHabit.hint = "0 times"
                }

                HabitGoal.DURATION -> {
                    binding.llGoalForHabit.makeVisible()
                    binding.goalNumberButton.turnOff()
                    binding.goalTimeButton.turnOn()
                    binding.textFieldGoalForHabit.hint = "0 minutes"
                }
            }
        }
    }

    private fun addRepeatDailysClickListeners() {
        binding.morning.addRepeatDailyClickListener(RepeatDailyIn.MORNING)
        binding.afternoon.addRepeatDailyClickListener(RepeatDailyIn.AFTERNOON)
        binding.evening.addRepeatDailyClickListener(RepeatDailyIn.EVENING)
        binding.repeatDailyInCheckbox.addRepeatDailyDayCheckboxClickListener()
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
                        binding.monday.turnOn()
                    }

                    DayOfWeek.TUESDAY -> {
                        binding.thesday.turnOn()
                    }

                    DayOfWeek.WEDNESDAY -> {
                        binding.wednesday.turnOn()
                    }

                    DayOfWeek.THURSDAY -> {
                        binding.thursday.turnOn()
                    }

                    DayOfWeek.FRIDAY -> {
                        binding.friday.turnOn()
                    }

                    DayOfWeek.SATURDAY -> {
                        binding.saturday.turnOn()
                    }

                    DayOfWeek.SUNDAY -> {
                        binding.sunday.turnOn()
                    }
                }
            }
        }
    }

    private fun observeRepeatDailys() {
        mAddViewModel.repeatDailyIn.observe(viewLifecycleOwner) {
            if (it.isEmpty()) return@observe

            binding.morning.turnOff()
            binding.afternoon.turnOff()
            binding.evening.turnOff()

            it.forEach { repeatDaily ->
                when (repeatDaily) {
                    RepeatDailyIn.MORNING -> {
                        binding.morning.turnOn()
                    }

                    RepeatDailyIn.AFTERNOON -> {
                        binding.afternoon.turnOn()
                    }

                    RepeatDailyIn.EVENING -> {
                        binding.evening.turnOn()
                    }
                    RepeatDailyIn.ANYTIME -> {
                        binding.morning.turnOn()
                        binding.afternoon.turnOn()
                        binding.evening.turnOn()
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

    private fun MaterialCheckBox.addRepeatDailyDayCheckboxClickListener() {
        addOnCheckedStateChangedListener { _, state ->
            if (state == MaterialCheckBox.STATE_CHECKED) {
                mAddViewModel.setRepeatDailys(
                    mutableSetOf(
                        RepeatDailyIn.ANYTIME
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
        setOnClickListener {
            toggleButton(dayOfWeek)
        }
    }

    private fun MaterialButton.addRepeatDailyClickListener(repeatDailyIn: RepeatDailyIn) {
        setOnClickListener {
            toggleRepeatDailyButton(repeatDailyIn)
        }
    }

    private fun MaterialButton.toggleRepeatDailyButton(repeatDailyIn: RepeatDailyIn) {
        if (backgroundTintList == ColorStateList.valueOf(getPrimaryColor(context))) {
            turnOn()
            mAddViewModel.addRepeatDaily(repeatDailyIn)
        } else {
            turnOff()
            mAddViewModel.removeRepeatDaily(repeatDailyIn)
        }
    }

    private fun MaterialButton.toggleButton(dayOfWeek: DayOfWeek) {
        if (backgroundTintList == ColorStateList.valueOf(getPrimaryColor(context))) {
            turnOn()
            mAddViewModel.addDay(dayOfWeek)
        } else {
            turnOff()
            mAddViewModel.removeDay(dayOfWeek)
        }
    }

    private fun MaterialButton.turnOff() {
        backgroundTintList = ColorStateList.valueOf(getPrimaryColor(context))
    }

    private fun MaterialButton.turnOn() {
        backgroundTintList = ColorStateList.valueOf(getPrimaryColor(context, R.attr.colorSecondary))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}