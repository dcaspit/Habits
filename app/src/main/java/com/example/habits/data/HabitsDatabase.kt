package com.example.habits.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.habits.data.models.HabitData

@Database(entities = [HabitData::class], version = 1, exportSchema = false)
abstract class HabitsDatabase: RoomDatabase() {

    abstract fun habitDao(): HabitDao

//    companion object {
//        @Volatile
//        private var INSTANCE: HabitsDatabase? = null
//
//        fun getDatabase(context: Context): HabitsDatabase {
//            val temp = INSTANCE
//
//            if(temp != null) {
//                return temp
//            }
//
//            synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    HabitsDatabase::class.java,
//                    "habits_database"
//                ).build()
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }

    companion object {
        @Volatile
        private var INSTANCE: HabitsDatabase? = null

        fun getDatabase(context: Context): HabitsDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                HabitsDatabase::class.java, "habits_database"
            ).build()
    }

}