package com.example.habits.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.habits.databinding.FragmentDetailsBinding

class HabitDetails: Fragment() {

    private val args : HabitDetailsArgs by navArgs()

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        try {
            _binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
            binding.args = args.habitData

            binding.executePendingBindings()
            return binding.root
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null;
    }
}