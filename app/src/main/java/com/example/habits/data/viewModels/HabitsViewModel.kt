package com.example.habits.data.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.fragments.home.components.HabitIntervals
import com.example.habits.models.Habit
import com.example.habits.models.HabitDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HabitsViewModel(application: Application): AndroidViewModel(application) {

    private var _habits = MutableLiveData<List<Habit>>(listOf())
    val habits: LiveData<List<Habit>>
        get() = _habits


    fun fetchHabits() {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                delay(200)
                if(_habits.value?.size == 0) {
                    _habits.postValue(
                        getHabits()
                    )
                }
            }catch (e: Exception) {
                Log.d("ERROR", e.stackTraceToString())
            }
        }
    }

    companion object {
        fun getHabits(): List<Habit> {
            return listOf(
                Habit(1, "Meditation", HabitIntervals.EVERYDAY, HabitDate("SUN", "12")),
                Habit(1, "Meditation", HabitIntervals.EVERYDAY, HabitDate("SUN", "12")),
                Habit(1, "Meditation", HabitIntervals.EVERYDAY, HabitDate("SUN", "12")),
                Habit(1, "Meditation", HabitIntervals.EVERYDAY, HabitDate("SUN", "12")),
                Habit(1, "Meditation", HabitIntervals.EVERYDAY, HabitDate("SUN", "12")),
            )
        }
    }

}