package com.example.habits.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.habits.data.viewModels.DatabaseViewModel
import com.example.habits.databinding.FragmentDetailsBinding

class DetailsFragment: Fragment() {

    private val mDatabaseViewModel: DatabaseViewModel by viewModels()

    private val args : DetailsFragmentArgs by navArgs()

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        try {
            _binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)

            val habit = mDatabaseViewModel.getHabitById(args.habitId)

            habit.observe(viewLifecycleOwner) {
                binding.text.text = it.title
            }

            return binding.root
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null;
    }
}