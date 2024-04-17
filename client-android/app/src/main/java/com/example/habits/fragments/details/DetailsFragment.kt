package com.example.habits.fragments.details

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.habits.R
import com.example.habits.data.viewModels.DatabaseViewModel
import com.example.habits.databinding.FragmentDetailsBinding
import com.example.habits.fragments.add.HabitGoal
import com.example.habits.utils.makeGone
import com.example.habits.utils.makeVisible

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

        val habit = mDatabaseViewModel.getHabitById(args.habitId)

        detailsViewModel.trackVisible.observe(viewLifecycleOwner) {
            if(it) {
                binding.trackContainer.makeVisible()
            } else {
                binding.trackContainer.makeGone()
            }
        }

        habit.observe(viewLifecycleOwner) {
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.title = it.name
            actionBar?.setDisplayHomeAsUpEnabled(true)

            setHabitProgressBar(it.habitGoal)
        }

        return binding.root
    }

    private fun setHabitProgressBar(habitGoal: String) {
        var (type, count) = habitGoal.split(",")
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
            binding.progressText.text = "0/1"
            binding.mainTrackButton.makeGone()
            count = "1"
        } else if (count.isNotEmpty()) {
            binding.progressIndicator.max = count.toInt()
            binding.progressText.text = "0/$count"
            binding.mainTrackButton.makeVisible()
            binding.trackActionButton.setOnClickListener {
                val addition = binding.textFieldName.text.toString()
                binding.progressText.text = "$addition/$count"
                binding.progressIndicator.progress = addition.toInt()
                binding.textFieldName.setText("")
                binding.textFieldName.clearFocus()
                //mDatabaseViewModel.trackHabit()
            }
        }

        binding.completeButton.setOnClickListener {
            binding.progressText.text = "$count/$count"
            binding.progressIndicator.progress = count.toInt()
            binding.textFieldName.setText("")
            binding.textFieldName.clearFocus()
            //mDatabaseViewModel.trackHabit()
        }

        binding.progressIndicator.setProgress(0, true)
    }
}