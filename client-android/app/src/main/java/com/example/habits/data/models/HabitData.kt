package com.example.habits.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "habits_table")
@Parcelize
data class HabitData(
    @ColumnInfo(name = "title") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "frequency") val frequency: String, // Can be "daily", "weekly", "monthly", or "custom"
    @ColumnInfo(name = "start_date") val startDate: String,
    @ColumnInfo(name = "end_date") val endDate: String?,
    @ColumnInfo(name = "track_days") val trackDays: String,
    @ColumnInfo(name = "habit_type") val habitType: String, // String will in format of: "type,count". examples: "0" "1,50" "2,20". types: "0"=NONE, "1"=NUMERIC, "2"=DURATION
    @ColumnInfo(name = "repeat_daily_in") val repeatDailyIn: String,
    @ColumnInfo(name = "reminder") val reminder: String? = null,
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "habit_id") val id: Int? = null,
): Parcelable


@Entity(tableName = "habits_actions_table")
@Parcelize
data class HabitAction(
    @ColumnInfo(name = "habit_id") val habitId: Int,
    @ColumnInfo(name = "selected_date") val selectedDate: String,
    @ColumnInfo(name = "habit_type") val habitType: String,
    @ColumnInfo(name = "repeat_daily_in") val repeatDailyIn: String,
    @ColumnInfo(name = "completed") val completed: Boolean,
    @ColumnInfo(name = "partial_amount") val partialAmount: Int,
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "habit_action_id") val id: Int? = null,
): Parcelable

@Entity(tableName = "habits_reminder")
@Parcelize
data class ReminderEntity(
    /**
     * The unique id of the Reminder.
     */
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "reminder_id") val id: Int? = null,

    /**
     * The Reminder's title.
     */
    @ColumnInfo(name = "reminder_title") var title: String,

    /**
     * The Reminder's body content.
     */
    @ColumnInfo(name = "reminder_body_content") var body: String,
    /**
     * The Reminder's time in millis.
     */
    @ColumnInfo(name = "reminder_time") var time: Long,

    /**
     * The id of the [ShowNotificationJob] responsible for displaying an Android notification for this Reminder.
     */
    @ColumnInfo(name = "reminder_external_id") var externalId: Int,

    /**
     * The id of the Android notification that was displayed to the user for this Reminder. If this field
     * equals 0 a notification has not yet been shown for this Reminder.
     */
    @ColumnInfo(name = "notification_id") var notificationId: Int
): Parcelable