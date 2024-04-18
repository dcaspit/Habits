package com.example.habits.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.habits.data.models.HabitAction
import com.example.habits.data.models.HabitData

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits_table ORDER BY habit_id ASC")
    fun getAllHabits(): List<HabitData>

    @Query("SELECT * FROM habits_table WHERE habit_id = :habitId")
    fun getHabit(habitId: Int): HabitData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHabit(habitData: HabitData)

    @Update
    fun updateHabit(habitData: HabitData)

    @Delete
    fun deleteHabit(habitData: HabitData)

    @Query("SELECT * FROM habits_actions_table WHERE habit_id = :habitId ")
    fun getHabitActions(habitId: Int): List<HabitAction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHabitAction(habit: HabitAction)

    @Update
    fun updateHabitAction(habit: HabitAction)

    @Delete
    fun deleteHabit(habit: HabitAction)

}