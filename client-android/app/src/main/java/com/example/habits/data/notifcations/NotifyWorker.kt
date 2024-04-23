package com.example.habits.data.notifcations

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.habits.CHANNEL_NOTIFICATIONS
import com.example.habits.MainActivity
import com.example.habits.R

class NotifyWorker(
    val context: Context, val params: WorkerParameters
) : Worker(context, params) {
    override fun doWork(): Result {
        triggerNotification()

        return Result.success()
    }

    private fun triggerNotification() {
        val notificationId = 222222
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        //val intent = ReminderActivity.intent(context, id)
        val pi = TaskStackBuilder.create(context).addNextIntentWithParentStack(intent)
            .getPendingIntent(notificationId, PendingIntent.FLAG_MUTABLE)

        // Show notification to user
        val builder = NotificationCompat.Builder(context, CHANNEL_NOTIFICATIONS)
            .setSmallIcon(R.drawable.home_icon).setContentIntent(pi).setContentTitle("Try Out")
            .setColor(ContextCompat.getColor(context, R.color.blue_800))
            .setContentText("Texting text out to texts")
            .setStyle(NotificationCompat.BigTextStyle().setSummaryText("Texting text out to texts"))

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        nm.notify(notificationId, builder.build())



    }
}