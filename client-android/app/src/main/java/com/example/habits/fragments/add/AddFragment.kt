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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)

        with(binding) {
            monday.addClickListener()
            thesday.addClickListener()
            wednesday.addClickListener()
            thursday.addClickListener()
            friday.addClickListener()
            saturday.addClickListener()
            sunday.addClickListener()
            morning.addClickListener()
            afternoon.addClickListener()
            evening.addClickListener()

            repeatEveryDayCheckbox.addClickListener(monday, thesday, wednesday, thursday, friday, saturday, sunday)
            repeatDailyInCheckbox.addClickListener(morning, afternoon, evening)
        }

        binding.buttonAdd.setOnClickListener {
            val setOfDays = setOf(DayOfWeek.WEDNESDAY, DayOfWeek.MONDAY)
            val stringBuilder = StringBuilder()
            setOfDays.forEach {
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

    private fun MaterialCheckBox.addClickListener(vararg buttons: MaterialButton) {
        val color = getPrimaryColor(context)
        addOnCheckedStateChangedListener { _, state ->
            buttons.forEachIndexed { i, button ->
                if(i == 0) {
                    if (state == MaterialCheckBox.STATE_CHECKED) {
                        button.backgroundTintList = ColorStateList.valueOf(color)
                    }
                } else {
                    if (state == MaterialCheckBox.STATE_CHECKED) {
                        button.backgroundTintList = ColorStateList.valueOf(color)
                    } else {
                        button.backgroundTintList = null
                    }
                }
            }
        }
    }

    private fun MaterialButton.addClickListener() {
         val color = getPrimaryColor(context)
        setOnClickListener {
            toggleButtonBGColor(color)
        }
    }

    private fun MaterialButton.toggleButtonBGColor(color: Int) {
        backgroundTintList = if (backgroundTintList == null) {
            ColorStateList.valueOf(color)
        } else {
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}