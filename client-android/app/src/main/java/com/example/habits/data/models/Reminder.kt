package com.example.habits.data.models

import android.os.Parcelable
import com.example.habits.utils.millis
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime
import java.util.UUID

@Parcelize
data class Reminder(
    val habitId: String = "",

    /**
     * The unique id of the Reminder.
     */
    val id: String = UUID.randomUUID().toString(),

    /**
     * The Reminder's title.
     */
    val title: String = "",

    /**
     * The Reminder's body content.
     */
    val body: String = "",

    /**
     * A [LocalDateTime] instance representing the Reminder's time.
     */
    val time: LocalDateTime = LocalDateTime.now(),

    /**
     * The id of the [ShowNotificationJob] responsible for displaying an Android notification for this Reminder.
     */
    val externalId: Int = 0,

    /**
     * The id of the Android notfication that was displayed to the user for this Reminder. If this field
     * equals 0 a notification has not yet been shown for this Reminder.
     */
    val notificationId: Int = 0
) : Parcelable {
    fun toReminderEntity(): ReminderEntity = ReminderEntity(
        title = title,
        body = body,
        time = time.millis,
        externalId = externalId,
        notificationId = notificationId,
        habitId = habitId
    )
}