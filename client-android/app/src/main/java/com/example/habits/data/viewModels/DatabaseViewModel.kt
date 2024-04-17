package com.example.habits.data.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.habits.data.HabitsDatabase
import com.example.habits.data.models.HabitAction
import com.example.habits.data.models.HabitData
import com.example.habits.data.repository.HabitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class DatabaseViewModel(application: Application) : AndroidViewModel(application) {

    private val habitDao = HabitsDatabase.getDatabase(application).habitDao()
    private val repository: HabitRepository = HabitRepository(habitDao)

    private val _habits = MutableLiveData<HashMap<HabitData, List<HabitAction>>>()
    val habits: LiveData<HashMap<HabitData, List<HabitAction>>>
        get() = _habits

    fun getAllHabits() {
        viewModelScope.launch(Dispatchers.IO) {
            val hashMap = hashMapOf<HabitData, List<HabitAction>>()
            val allHabits = habitDao.getAllHabits()
            allHabits.forEach {
                hashMap[it] = getHabitActions(it.id!!)
            }
            _habits.postValue(hashMap)
        }
    }

    fun insertHabit(habitData: HabitData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertHabit(habitData)
        }
    }

    fun getHabitById(id: Int): LiveData<HabitData> {
        return repository.getHabitById(id)
    }

    fun trackHabit(habitAction: HabitAction) {
        viewModelScope.launch(Dispatchers.IO) {
            habitDao.insertHabitAction(habitAction)
        }
    }

    fun getHabitActions(habitId: Int): List<HabitAction> {
        return habitDao.getHabitActions(habitId)
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