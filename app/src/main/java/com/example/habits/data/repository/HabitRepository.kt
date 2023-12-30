package com.example.habits.data.repository

import androidx.lifecycle.LiveData
import com.example.habits.data.HabitDao
import com.example.habits.data.models.HabitData

class HabitRepository(private val habitDao: HabitDao) {

    val getAllHabits: LiveData<List<HabitData>> = habitDao.getAllHabits()

    fun insertHabit(habitData: HabitData) {
        habitDao.insertHabit(habitData)
    }

    fun getHabitById(id: Int) = habitDao.getHabit(id)

}