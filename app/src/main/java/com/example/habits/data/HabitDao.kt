package com.example.habits.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.habits.data.models.HabitData

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits_table ORDER BY habit_id ASC")
    fun getAllHabits(): LiveData<List<HabitData>>

    @Query("SELECT * FROM habits_table WHERE habit_id = :habitId")
    fun getHabit(habitId: Int): LiveData<HabitData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHabit(habitData: HabitData)

    @Update
    fun updateHabit(habitData: HabitData)

    @Delete
    fun deleteHabit(habitData: HabitData)

}