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

class Notifications : AppCompatActivity() {

    private val filename = "progress.txt"

    private val CHANNEL_ID = "channel_id_example"
    private val notificationId = 101

    private lateinit var textEditorForSips: EditText
    private lateinit var textViewForStreak: TextView
    private lateinit var info: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        createNotificationChannel()

        info = readFromFile(filename)
        Log.i(TAG, "$info")
        val settingSipValue = info.split("\n")[2]
        //TODO: Update the Current Day Streak
        textEditorForSips = findViewById(R.id.viewOrChangeSipCounter)
        textEditorForSips.hint = settingSipValue

        textViewForStreak = findViewById(R.id.currStreak)
        textViewForStreak.text = info.split("\n")[1].split(" ")[1]

        val notify_button = findViewById<ImageView>(R.id.notify_button)
        notify_button.setOnClickListener() {
            Log.i(TAG, "save settings button has been clicked")
            commitSettings()
            sendNotification()
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description= descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
//        val intent = Intent(this, Notifications::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
//
//        //Change androidTwo to the appropriate icons
//        val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.androidtwo)
//        val bitMapLargeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.androidtwo)



        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
//        set this to logo later
            .setContentTitle("Example Title")
            .setContentText("Example Text")
//            .setLargeIcon(bitMapLargeIcon)
//            .setStyle(NotificationCompat.BigPictureStyle().bigLargeIcon(bitmap))
//            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
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
        private const val TAG = "Water-Me"
    }
}