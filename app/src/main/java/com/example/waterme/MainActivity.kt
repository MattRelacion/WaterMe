package com.example.waterme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import java.lang.Exception
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
    private lateinit var date: String

    private lateinit var waterDropButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        waterDropButton = findViewById(R.id.waterButton)

        calendar = Calendar.getInstance()
        date = dateFormat.format(calendar.time)

        val initialized = readFromFile(filename)
        if (initialized == "") {
            val info = "${date}\n0 0"
            writeToFile(info)
        }

        val info = readFromFile(filename)
        val infoToList = info.split("\n")
        if (infoToList[0].split("/")[0] > date.split("/")[0] ||
                infoToList[0].split("/")[1] > date.split("/")[1]) {

            val updatedDate = date
            var updatedDayCounter = infoToList[1].split("  ")[1]
            if (infoToList[1].split(" ")[0].toInt() >= sipInDay) {
                updatedDayCounter = "0 " + (infoToList[1].split(" ")[1].toInt() + 1)
            }

            writeToFile(updatedDate + "\n" + updatedDayCounter)
        }


        waterDropButton.setOnClickListener {
            var info = readFromFile(filename)
            val separated = info.split("\n")
            val updatedDayCounter = separated[0] + "\n" + (separated[1].split(" ")[0].toInt() + 1) + " "+ (separated[1].split(" ")[1].toInt())
            writeToFile(updatedDayCounter)
            Log.i(TAG, updatedDayCounter)
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