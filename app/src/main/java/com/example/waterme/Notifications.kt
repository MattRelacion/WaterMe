package com.example.waterme

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class Notifications : AppCompatActivity() {

    private val CHANNEL_ID = "channel_id_example"
    private val notificationId = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        createNotificationChannel()

        val notify_button = findViewById<Button>(R.id.notify_button)
        notify_button.setOnClickListener() {
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
}