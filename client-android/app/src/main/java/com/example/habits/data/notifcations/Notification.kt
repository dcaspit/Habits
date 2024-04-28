package com.example.habits.data.notifcations

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.example.habits.MainActivity
import com.example.habits.R

// Constants for notification
const val notificationID = 121
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

// BroadcastReceiver for handling notifications
class Notification : BroadcastReceiver() {

    // Method called when the broadcast is received
    override fun onReceive(context: Context, intent: Intent) {


        //val intent = ReminderActivity.intent(context, id)
        val pi = TaskStackBuilder.create(context).addNextIntentWithParentStack(intent)
            .getPendingIntent(NotifyWorker.NOTIFICATION_ID, PendingIntent.FLAG_MUTABLE)

        // Build the notification using NotificationCompat.Builder
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.home_icon)
            .setContentIntent(pi)
            .setContentTitle("Try Out").setColor(ContextCompat.getColor(context, R.color.blue_800))
            .setContentText("Texting text out to texts")
            .setStyle(NotificationCompat.BigTextStyle().setSummaryText("Texting text out to texts"))

        // Get the NotificationManager service
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Show the notification using the manager
        manager.notify(notificationID, notification.build())
    }
}