package com.example.habits.data.models

import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Entity(tableName = "habits_table")
@Parcelize
data class HabitData(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "intervals") val interval: Int,
    @ColumnInfo(name = "color") val color: Int,
    @ColumnInfo(name = "type") val type: Int,
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