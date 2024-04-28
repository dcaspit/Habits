package com.example.habits.data.notifcations

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.habits.CHANNEL_NOTIFICATIONS
import com.example.habits.MainActivity
import com.example.habits.R
import kotlinx.coroutines.delay

class NotifyWorker(
    val context: Context, val params: WorkerParameters
) : CoroutineWorker(context, params) {

    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    //val intent = ReminderActivity.intent(context, id)
    val pi = TaskStackBuilder.create(context).addNextIntentWithParentStack(intent)
        .getPendingIntent(NOTIFICATION_ID, PendingIntent.FLAG_MUTABLE)

    val notificationManager = context.getSystemService(NotificationManager::class.java)

    val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        .setSmallIcon(R.drawable.home_icon)
        .setContentIntent(pi)
        .setContentTitle("Try Out").setColor(ContextCompat.getColor(context, R.color.blue_800))
        .setContentText("Texting text out to texts")
        .setStyle(NotificationCompat.BigTextStyle().setSummaryText("Texting text out to texts"))


    override suspend fun doWork(): Result {
        Log.d(TAG, "Start job")

        createNotificationChannel()

        val notification = notificationBuilder.build()

        notificationManager.notify(NOTIFICATION_ID, notification)

//        val foregroundInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            ForegroundInfo(NOTIFICATION_ID, notification,
//                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
//            )
//        } else {
//            ForegroundInfo(NOTIFICATION_ID, notification)
//        }
//        setForegroundAsync(foregroundInfo)
//
//        //setForeground(foregroundInfo)
//
//       // notificationManager.notify(NOTIFICATION_ID, notification)
//        for (i in 0..100) {
//            setProgress(workDataOf(ARG_PROGRESS to i))
//            // we need it to get progress in UI
//            // update the notification progress
//            showProgress(i)
//            delay(DELAY_DURATION)
//        }

        Log.d(TAG, "Finish job")
        return Result.success()
    }

    private suspend fun showProgress(progress: Int) {
        val notification = notificationBuilder
            .setProgress(100, progress, false)
            .build()
        val foregroundInfo = ForegroundInfo(NOTIFICATION_ID, notification)
        setForeground(foregroundInfo)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = notificationManager?.getNotificationChannel(CHANNEL_ID)
            if (notificationChannel == null) {
                notificationManager?.createNotificationChannel(
                    NotificationChannel(
                        CHANNEL_ID, TAG, NotificationManager.IMPORTANCE_LOW
                    )
                )
            }
        }
    }

    companion object {

        const val TAG = "ForegroundWorker"
        const val NOTIFICATION_ID = 42
        const val CHANNEL_ID = "Job progress"
        const val ARG_PROGRESS = "Progress"
        private const val DELAY_DURATION = 100L // ms
    }
}