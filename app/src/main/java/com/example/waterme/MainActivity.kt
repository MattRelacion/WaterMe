package com.example.waterme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import java.sql.DataTruncation
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    //TEXT FILE WILL LOOK LIKE THIS :
    // 05/03/2022 (Note: This is the date)
    // 0 0        (Note: Sips taken today, days completed
    private val filename = "progress.txt"

    //hardcoded. can be based on setting
    private val sipInDay = 3

    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)

    private lateinit var calendar: Calendar
    private lateinit var today: String
    private lateinit var tomorrow: String
    var isDayStart = true
    var initialized = false

    private lateinit var waterDropButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        waterDropButton = findViewById(R.id.waterButton)



        if (isDayStart) {
            calendar = Calendar.getInstance()
            today = dateFormat.format(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            tomorrow = dateFormat.format(calendar.time)
            isDayStart = false
        }


        if (!initialized) {
            val info = "${today}\n0 0"
            writeToFile(info)
            initialized = true
        }

        if (today.split("/")[0].toInt() > tomorrow.split("/")[0].toInt() ||
                today.split("/")[1].toInt() > tomorrow.split("/")[1].toInt()) {

            isDayStart = true
            val info = readFromFile(filename)
            if (info != "") {
                val separated = info.split("\n")
                if (separated[1].split(" ")[0].toInt() >= sipInDay) {
                    val updatedDayCounter = separated[0] + "\n" + "0 " + (separated[1].split(" ")[1].toInt() + 1)
                    writeToFile(updatedDayCounter)
                }
            }
        }


        waterDropButton.setOnClickListener {
            var info = readFromFile(filename)
            Log.i(TAG, info)
            val separated = info.split("\n")
            val updatedDayCounter = separated[0] + "\n" + (separated[1].split(" ")[0].toInt() + 1) + " "+ (separated[1].split(" ")[1].toInt())
            writeToFile(updatedDayCounter)
            Log.i(TAG, updatedDayCounter)
        }

    }

    private fun readFromFile(filename: String): String {
        openFileInput(filename).use { stream ->
            var text = stream.bufferedReader().use{
                it.readText()
            }
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