package com.example.habits.data.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.habits.data.models.HabitIntervals
import com.example.habits.data.models.HabitData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HabitsViewModel(application: Application): AndroidViewModel(application) {

    private var _habits = MutableLiveData<List<HabitData>>(listOf())
    val habits: LiveData<List<HabitData>>
        get() = _habits


    fun fetchHabits() {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                delay(200)
                if(_habits.value?.size == 0) {
//                    _habits.postValue(
//                        //getHabits()
//                    )
                }
            }catch (e: Exception) {
                Log.d("ERROR", e.stackTraceToString())
            }
        }
    }

    //fun getHabit(id: Int): HabitData? = _habits.value?.find { habit -> habit.id == id }

    companion object {
    }

}