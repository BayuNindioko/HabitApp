package com.example.habitapp.countdown

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE
import com.dicoding.habitapp.utils.NOTIFICATION_CHANNEL_ID
import com.example.habitapp.MainActivity
import com.example.habitapp.R
import com.example.habitapp.detail.DetailActivity

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val habitId = inputData.getString(HABIT_ID)
    private val habitTitle = inputData.getString(HABIT_TITLE)

    override fun doWork(): Result {

        val prefManager = androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val shouldNotify = prefManager.getBoolean(applicationContext.getString(R.string.pref_key_notify), false)

        val message = applicationContext.getString(R.string.notify_content)

        if (shouldNotify) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    applicationContext.getString(R.string.notify_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                val notificationManager =
                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            val notificationIntent = Intent(applicationContext, MainActivity::class.java).apply {
                putExtra(HABIT_ID, habitId)
            }

            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
            )

            val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(habitTitle)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notification)
        }

        return Result.success()
    }

}
