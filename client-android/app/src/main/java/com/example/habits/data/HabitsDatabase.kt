package com.example.habits.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.habits.data.models.HabitAction
import com.example.habits.data.models.HabitData
import com.example.habits.data.models.ReminderEntity

@Database(entities = [HabitData::class, HabitAction::class, ReminderEntity::class], version = 1, exportSchema = false)
abstract class HabitsDatabase: RoomDatabase() {

    abstract fun habitDao(): HabitDao

    companion object {
        @Volatile
        private var INSTANCE: HabitsDatabase? = null

        fun getDatabase(context: Context): HabitsDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                HabitsDatabase::class.java, "habits_database"
            ).build()
    }

}