package com.example.habits.fragments.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.data.models.HabitData
import kotlinx.coroutines.launch

class AddViewModel: ViewModel(){

    private var _canSave = MutableLiveData(false)
    val canSave: LiveData<Boolean>
        get() = _canSave

    private var _habit = MutableLiveData<HabitData>()
    val habit: LiveData<HabitData>
        get() = _habit

    fun setCanSave(canSave: Boolean) {
        viewModelScope.launch {
            _canSave.postValue(canSave)
        }
    }

    fun setHabit(habit: HabitData) {
        viewModelScope.launch {
            _habit.postValue(habit)
        }
    }

}