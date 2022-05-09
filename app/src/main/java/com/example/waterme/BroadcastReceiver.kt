package com.example.waterme

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.app.PendingIntent
import android.content.Intent
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import java.text.DateFormat
import java.util.*

class BroadcastReceiver: BroadcastReceiver() {
    companion object {
        // Notification ID to allow for future updates
        private const val MY_NOTIFICATION_ID = 0
        private const val TAG = "NotificationTimer"
    }

    private lateinit var mNotificationManager: NotificationManager

    // Notification Text Elements
    private val tickerText = "Drink Water!"
    private val contentTitle = "Drink Some Water!"
    private val contentText = "Take a sip to stay hydrated throughout the day :)"

    private lateinit var mContext: Context
    private lateinit var mChannelID: String

    override fun onReceive(context: Context, intent: Intent) {



        mContext = context
        mChannelID = "channel"

        mNotificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()

        val mNotificationIntent = Intent(context, MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val mContentIntent = PendingIntent.getActivity(
            context, 0,
            mNotificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val bitMapLargeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.button)

        // Build the Notification
        val notificationBuilder = Notification.Builder(
            mContext, mChannelID
        ).setTicker(tickerText)
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setAutoCancel(true).setContentTitle(contentTitle)
            .setLargeIcon(bitMapLargeIcon)
            .setContentText(contentText).setContentIntent(mContentIntent)

        // Pass the Notification to the NotificationManager:
        mNotificationManager.notify(
            MY_NOTIFICATION_ID,
            notificationBuilder.build()
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "WaterMe"
            val descriptionText = "Drink some water!"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(mChannelID, name, importance).apply {
                description = descriptionText
            }
            mNotificationManager.createNotificationChannel(mChannel)
        }
    }
}