package com.example.habits.fragments.add

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.habits.R
import com.example.habits.data.models.HabitData
import com.example.habits.data.viewModels.DatabaseViewModel
import com.example.habits.databinding.FragmentAddBinding
import com.example.habits.utils.getPrimaryColor
import com.example.habits.utils.localDateToString
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import java.time.DayOfWeek
import java.time.LocalDate


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

        mAddViewModel.days.observe(viewLifecycleOwner) {
            if(it.isEmpty()) return@observe

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

        with(binding) {
            monday.addClickListener(DayOfWeek.MONDAY)
            thesday.addClickListener(DayOfWeek.TUESDAY)
            wednesday.addClickListener(DayOfWeek.WEDNESDAY)
            thursday.addClickListener(DayOfWeek.THURSDAY)
            friday.addClickListener(DayOfWeek.FRIDAY)
            saturday.addClickListener(DayOfWeek.SATURDAY)
            sunday.addClickListener(DayOfWeek.SUNDAY)
//            morning.addClickListener(DayOfWeek.MONDAY)
//            afternoon.addClickListener(DayOfWeek.MONDAY)
//            evening.addClickListener(DayOfWeek.MONDAY)

            repeatEveryDayCheckbox.addClickListener()
            //repeatDailyInCheckbox.addClickListener(morning, afternoon, evening)
        }

        binding.buttonAdd.setOnClickListener {
            val stringBuilder = StringBuilder()
            mAddViewModel.days.value?.forEach {
                stringBuilder.append(it)
                stringBuilder.append(",")
            }

            mDatabaseViewModel.insertHabit(
                HabitData(
                    binding.textFieldName.text.toString(),
                    "",
                    "daily",
                    localDateToString(LocalDate.now()),
                    null,
                    stringBuilder.toString()
                )
            )
            findNavController().popBackStack()
        }
        return binding.root
    }

    private fun MaterialCheckBox.addClickListener() {
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
            }
            else {
                mAddViewModel.setDays(
                    mutableSetOf(
                        DayOfWeek.MONDAY,
                    )
                )
            }
        }
    }

    private fun MaterialButton.addClickListener(dayOfWeek: DayOfWeek) {
        val color = getPrimaryColor(context)
        setOnClickListener {
            toggleButton(color, dayOfWeek)
        }
    }

    private fun MaterialButton.toggleButton(color: Int, dayOfWeek: DayOfWeek) {
        if (backgroundTintList == null) {
            turnOn(color)
        } else {
            turnOff()
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