package com.example.waterme

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import java.text.DateFormat
import java.util.*

class Notifications : AppCompatActivity() {

    private lateinit var mAlarmManager: AlarmManager
    private lateinit var mNotificationReceiverPendingIntent: PendingIntent

    companion object {
        private const val oneSecond = 1000L
        private const val delay = 1000L

    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        mAlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Create an Intent to broadcast to the AlarmNotificationReceiver
        val mNotificationReceiverIntent = Intent(
            this,
            BroadcastReceiver::class.java
        )

        // Create an PendingIntent that holds the NotificationReceiverIntent
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
            this, 0, mNotificationReceiverIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun onClickSendNotification(v: View) {
        // Set single alarm
        mAlarmManager.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            mNotificationReceiverPendingIntent
        )


        // Show Toast message
        Toast.makeText(
            applicationContext, "Notification Processed",
            Toast.LENGTH_LONG
        ).show()
    }

    fun onClickSendRepeatNotification(v: View) {
        // Set repeating alarm (Currently set at 1 min, change delay to longer, minimum is 1 min)
        mAlarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime(),
            delay,
            mNotificationReceiverPendingIntent
        )

        // Show Toast message
        Toast.makeText(
            applicationContext, "Repeating Notification Set",
            Toast.LENGTH_LONG
        ).show()
    }

    fun onClickCancelRepeat(v: View) {
        // Cancel all alarms using mNotificationReceiverPendingIntent
        mAlarmManager.cancel(mNotificationReceiverPendingIntent)

        // Show Toast message
        Toast.makeText(
            applicationContext,
            "No Longer Repeating", Toast.LENGTH_LONG
        ).show()
    }
}