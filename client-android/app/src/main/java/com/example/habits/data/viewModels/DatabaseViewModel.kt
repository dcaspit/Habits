package com.example.habits.data.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.habits.data.HabitsDatabase
import com.example.habits.data.models.HabitData
import com.example.habits.data.repository.HabitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseViewModel(application: Application) : AndroidViewModel(application) {

    private val habitDao = HabitsDatabase.getDatabase(application).habitDao()
    private val repository: HabitRepository = HabitRepository(habitDao)

    val getAllHabits: LiveData<List<HabitData>> = repository.getAllHabits

    fun insertHabit(habitData: HabitData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertHabit(habitData)
        }
    }

    fun getHabitById(id: Int): LiveData<HabitData> {
        return repository.getHabitById(id)
    }

    fun updataHabit(habitData: HabitData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateHabit(habitData)
        }
    }

    fun deleteHabit(habitData: HabitData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteHabit(habitData)
        }
    }


}