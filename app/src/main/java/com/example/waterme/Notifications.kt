package com.example.waterme

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.w3c.dom.Text
import java.lang.Exception
import android.app.*
import android.content.Intent
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import java.text.DateFormat
import java.util.*

class Notifications : AppCompatActivity() {
    private lateinit var mAlarmManager: AlarmManager
    private lateinit var mNotificationReceiverPendingIntent: PendingIntent

    private val filename = "progress.txt"

    private val CHANNEL_ID = "channel_id_example"
    private val notificationId = 101

    private lateinit var textEditorForSips: EditText
    private lateinit var textViewForStreak: TextView
    private lateinit var info: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        info = readFromFile(filename)
        val settingSipValue = info.split("\n")[2]
        //TODO: Update the Current Day Streak
        textEditorForSips = findViewById(R.id.viewOrChangeSipCounter)
        textEditorForSips.hint = settingSipValue

        textViewForStreak = findViewById(R.id.currStreak)
        textViewForStreak.text = info.split("\n")[1].split(" ")[1]

        val notify_button = findViewById<ImageView>(R.id.notify_button)
        notify_button.setOnClickListener() {
            commitSettings()
        }

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

    private fun commitSettings() {
        val newSettingData = info.split("\n")[0] + "\n" + info.split("\n")[1] + "\n" + textEditorForSips.text
        writeToFile(newSettingData)
    }

    private fun readFromFile(filename: String): String {
        var text = ""
        try {
            openFileInput(filename).use { stream ->
                text = stream.bufferedReader().use{
                    it.readText()
                }
                return text
            }
        } catch (ex: Exception) {
            return text
        }
    }

    private fun writeToFile(text: String) {
        openFileOutput(filename, Context.MODE_PRIVATE).use  { output ->
            output.write(text.toByteArray())
        }
    }

    companion object {
        private const val oneSecond = 1000L
        private const val delay = 1000L

    }
}