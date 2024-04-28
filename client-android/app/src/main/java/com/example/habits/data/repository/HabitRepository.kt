package com.example.habits.data.repository

import androidx.lifecycle.LiveData
import com.example.habits.data.HabitDao
import com.example.habits.data.models.HabitData

class HabitRepository(private val habitDao: HabitDao) {

    suspend fun insertHabit(habitData: HabitData) {
        habitDao.insertHabit(habitData)
    }

    fun getHabitById(id: String) = habitDao.getHabit(id)

    fun updateHabit(habitData: HabitData) {
        habitDao.updateHabit(habitData)
    }

    fun deleteHabit(habitData: HabitData){
        habitDao.deleteHabit(habitData)
    }

}