package com.example.habits.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.habits.R
import com.example.habits.data.models.HabitAction
import com.example.habits.data.models.HabitData
import com.example.habits.data.viewModels.DatabaseViewModel
import com.example.habits.databinding.FragmentDetailsBinding
import com.example.habits.fragments.add.HabitGoal
import com.example.habits.utils.localDateToString
import com.example.habits.utils.makeGone
import com.example.habits.utils.makeVisible
import com.example.habits.utils.stringToLocalDate
import kotlinx.coroutines.flow.collect
import java.time.LocalDate

class DetailsFragment: Fragment() {

    private val mDatabaseViewModel: DatabaseViewModel by viewModels()
    private val detailsViewModel: DetailsViewModel by viewModels()

    private val args : DetailsFragmentArgs by navArgs()

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()


    }

    override fun onStop() {
        super.onStop()

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = context?.getString(R.string.activity_main_title)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)

        mDatabaseViewModel.habit.observe(viewLifecycleOwner) {
            val (habit, habitActions) = it
            setActionBarTitle(habit)
            val action = habitActions.find { habitAction -> habitAction.selectedDate == args.selectedDate }
            setHabitProgressBar(habit, action)
        }
        mDatabaseViewModel.getHabitById(args.habitId)

        observeTrackContainer()
        return binding.root
    }

    private fun observeTrackContainer() {
        detailsViewModel.trackVisible.observe(viewLifecycleOwner) {
            if (it) {
                binding.trackContainer.makeVisible()
            } else {
                binding.trackContainer.makeGone()
            }
        }
    }

    private fun setActionBarTitle(habit: HabitData) {
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = habit.name
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setHabitProgressBar(habitData: HabitData, habitAction: HabitAction?) {
        var (type, count) = habitAction?.habitType?.split(",") ?: habitData.habitType.split(",")
        if (type.isEmpty()) {
            binding.trackCard.makeGone()
            return
        }

        binding.mainTrackButton.setOnClickListener {
            detailsViewModel.trackVisible.value?.let { track ->
                detailsViewModel.setTrackVisible(!track)
            }
        }

        if (type == HabitGoal.NONE.ordinal.toString()) {
            binding.progressIndicator.max = 1
            if(habitAction != null && habitAction.completed) {
                binding.progressText.text = "1/1"
                binding.progressIndicator.setProgress(1, true)
                binding.actionContainer.makeGone()
                binding.trackContainer.makeGone()
            } else{
                binding.progressText.text = "0/1"
            }
            binding.mainTrackButton.makeGone()
            count = "1"
        } else if (count.isNotEmpty()) {
            binding.progressIndicator.max = count.toInt()
            if(habitAction != null) {
                binding.progressIndicator.setProgress(habitAction.partialAmount, true)
                binding.progressText.text = "${habitAction.partialAmount}/$count"
                binding.actionContainer.makeGone()
                binding.trackContainer.makeGone()
            } else {
                binding.progressIndicator.setProgress(0, true)
                binding.progressText.text = "0/$count"
                binding.mainTrackButton.makeVisible()
                binding.trackActionButton.setOnClickListener {
                    val addition = binding.textFieldName.text.toString()
                    binding.progressText.text = "$addition/$count"
                    binding.progressIndicator.progress = addition.toInt()
                    binding.textFieldName.setText("")
                    binding.textFieldName.clearFocus()
                    binding.actionContainer.makeGone()
                    binding.trackContainer.makeGone()
                    mDatabaseViewModel.trackHabit(HabitAction(
                        args.habitId,
                        args.selectedDate,
                        habitData.habitType,
                        (addition.toInt() == count.toInt()),
                        addition.toInt()
                    ))
                }
                binding.progressIndicator.setProgress(0, true)
            }
        }

        binding.completeButton.setOnClickListener {
            binding.progressText.text = "$count/$count"
            binding.progressIndicator.progress = count.toInt()
            binding.textFieldName.setText("")
            binding.textFieldName.clearFocus()
            binding.actionContainer.makeGone()
            mDatabaseViewModel.trackHabit(HabitAction(
                args.habitId,
                args.selectedDate,
                habitData.habitType,
                true,
                count.toInt()
            ))
        }
    }
}