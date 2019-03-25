package com.quadriyanney.hrmonitor.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.quadriyanney.hrmonitor.R
import com.quadriyanney.hrmonitor.ui.main.MainActivity

import java.util.concurrent.TimeUnit

class NotificationHandler(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): ListenableWorker.Result {
        sendNotification()
        return ListenableWorker.Result.success()
    }

    private fun sendNotification() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "default")
            .setContentTitle("Hello, it's time to take your pulse today.")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_cardiogram)
            .setAutoCancel(true)

        notificationManager.notify(0, notification.build())
    }

    companion object {

        fun scheduleReminder(duration: Long, timeUnit: TimeUnit, tag: String) {
            val reminderWork = PeriodicWorkRequest.Builder(
                NotificationHandler::class.java, duration, timeUnit).addTag(tag).build()

            val instance = WorkManager.getInstance()
            instance.enqueue(reminderWork)
        }

        fun cancelReminder(tag: String) {
            val instance = WorkManager.getInstance()
            instance.cancelAllWorkByTag(tag)
        }
    }

}
