package com.example.habits.data.models

import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Date

@Entity(tableName = "habits_table")
@Parcelize
data class HabitData(
    @ColumnInfo(name = "title") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "frequency") val frequency: String, // Can be "daily", "weekly", "monthly", or "custom"
    @ColumnInfo(name = "start_date") val startDate: String,
    @ColumnInfo(name = "end_date") val endDate: String?,
    @ColumnInfo(name = "track_days") val trackDays: String,
    @ColumnInfo(name = "habit_goal") val habitGoal: String, // String will in format of: "type,count". examples: "0" "1,50" "2,20". types: "0"=NONE, "1"=NUMERIC, "2"=DURATION
    @ColumnInfo(name = "repeat_daily_in") val repeatDailyIn: String,
    @ColumnInfo(name = "reminder") val reminder: String? = null,
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "habit_id") val id: Int? = null,
): Parcelable


@Entity(tableName = "habits_actions_table")
@Parcelize
data class HabitAction(
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "reversals") val reversals: Int,
    @ColumnInfo(name = "total_reversals") val totalReversals: Int,
    @ColumnInfo(name = "habit_id") val habitId: Int,
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "habit_action_id") val id: Int? = null,
): Parcelable {

}