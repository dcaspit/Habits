package com.example.habits.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.habits.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        try{
            _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner

            return binding.root
        } catch (e: Exception){
            e.printStackTrace()
        }
        return null;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}