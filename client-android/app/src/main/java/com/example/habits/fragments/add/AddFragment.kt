package com.example.habits.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.habits.MainActivity
import com.example.habits.R
import com.example.habits.data.models.HabitData
import com.example.habits.data.models.HabitIntervals
import com.example.habits.data.models.HabitType
import com.example.habits.data.viewModels.DatabaseViewModel
import com.example.habits.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    private val mDatabaseViewModel: DatabaseViewModel by viewModels()

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    val titleRes: Int = R.string.add_habit_title

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

        binding.tvPlusButton.setOnClickListener {
            try {
                var num = Integer.valueOf(binding.tvNumber.text.toString())
                if (num > 6) return@setOnClickListener
                num += 1
                binding.tvNumber.text = num.toString()
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.tvMinusButton.setOnClickListener {
            try {
                var num = Integer.valueOf(binding.tvNumber.text.toString())
                if (num < 2) return@setOnClickListener
                num -= 1
                binding.tvNumber.text = num.toString()
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.buttonAdd.setOnClickListener {
            mDatabaseViewModel.insertHabit(
                HabitData(
                    binding.etTitle.text.toString(),
                    if (binding.tvNumber.text == "7") HabitIntervals.EVERYDAY.ordinal else HabitIntervals.EVEYWEEK.ordinal,
                    0,
                    HabitType.DONE_NOTDONE.ordinal,
                )
            )
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}