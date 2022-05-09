package com.example.waterme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import java.lang.Exception
import java.sql.DataTruncation
import android.widget.ImageView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    //TEXT FILE WILL LOOK LIKE THIS :
    // 05/03/2022 (Note: This is the date)
    // 0 0        (Note: Sips taken today, days completed
    private val filename = "progress.txt"

    //hardcoded. can be based on setting

    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)

    private lateinit var calendar: Calendar
    private lateinit var date: String

    private lateinit var waterDropButton: ImageButton
    private lateinit var goToNotifyButton: ImageView
    private lateinit var weekProgressButton: ImageView
    private lateinit var displayImage: ImageView

    private var switch = false

    private val receiver = object: DateChangedBroadcastReceiver() {
        override fun dateChanged(onFileDate: String, curDate: String) {
            val info = readFromFile(filename)
            switch = false
            val infoToList = info.split("\n")
            if (onFileDate.split("/")[0] < curDate.split("/")[0] ||
                onFileDate.split("/")[1] < curDate.split("/")[1] ||
                onFileDate.split("/")[2] < curDate.split("/")[2]) {
                val updatedDate = curDate
                lateinit var updatedDayCounter: String

                if (infoToList[1].split(" ")[0].toInt() >= infoToList[2].toInt()) {
                    updatedDayCounter = "0 " + (infoToList[1].split(" ")[1].toInt() + 1) +  "\n" + infoToList[2]
                } else  {
                    updatedDayCounter = "0 0\n" + infoToList[2]
                }

                Log.i(TAG, "UPDATE TO FILE: $updatedDate , $updatedDayCounter")
                writeToFile(updatedDate + "\n" + updatedDayCounter)
            }

        }
    }

    override fun onStart() {
        super.onStart()
        val info = readFromFile(filename)
        val infoToList = info.split("\n")
        val dateInList = infoToList[0]
        receiver.registerOnStart(this, dateInList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        waterDropButton = findViewById(R.id.waterButton)
        goToNotifyButton = findViewById(R.id.goToNotifyButton)
        weekProgressButton = findViewById(R.id.week_progress)
        displayImage = findViewById(R.id.plant)

        calendar = Calendar.getInstance()
        date = dateFormat.format(calendar.time)
        var day = 0
        var info = readFromFile(filename)
        if (info.length != 0) {
            val separated = info.split("\n")
            day = separated[1].split(" ")[1].toInt()
        }
        Log.i(TAG, "$day")
        if(day == 0){
            displayImage.setImageResource(R.drawable.plant1)
        } else if (day == 1){
            displayImage.setImageResource(R.drawable.plant2)
        } else if (day == 2){
            displayImage.setImageResource(R.drawable.plant3)
        }  else {
            displayImage.setImageResource(R.drawable.plant4)
        }

        val initialized = readFromFile(filename)
        if (initialized == "") {
            val info = "${date}\n0 0\n3"
            writeToFile(info)
        }

        waterDropButton.setOnClickListener {
            var info = readFromFile(filename)
            val separated = info.split("\n")
            val sips = separated[1].split(" ")[0].toInt()
            val day = separated[1].split(" ")[1].toInt()
            val sipsInADay = separated[2].toInt()
            val updatedDayCounter = separated[0] + "\n" + (separated[1].split(" ")[0].toInt() + 1) + " " + (separated[1].split(" ")[1].toInt()) + "\n" + separated[2]
            writeToFile(updatedDayCounter)
            if (sips >= sipsInADay && switch == false){
                if(day == 0){
                    displayImage.setImageResource(R.drawable.plant2)
                } else if (day == 1){
                    displayImage.setImageResource(R.drawable.plant3)
                } else if (day >= 2){
                    displayImage.setImageResource(R.drawable.plant4)
                }
                switch = true
                Log.i(TAG, "$day")
            }
            Log.i(TAG, updatedDayCounter)
        }

        //go to notifications page
        goToNotifyButton.setOnClickListener {
            val intent = Intent(this@MainActivity, Notifications::class.java)
            startActivity(intent)
        }

        weekProgressButton.setOnClickListener {
            val intent = Intent(this@MainActivity, WaterPlantActivity::class.java)
            startActivity(intent)
        }
    }

    /*
        The file in internal storage will be kept once the app is running.
        In order to clear everything, you'll have to access the emulator's file
        explorer and delete the file specifically and start the app again.

     */
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