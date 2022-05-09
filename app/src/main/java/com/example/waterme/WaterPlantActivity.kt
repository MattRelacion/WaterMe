package com.example.waterme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.View
import android.widget.TextView
import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable
import android.view.WindowManager
import androidx.lifecycle.ViewModel
import org.w3c.dom.Text
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import java.sql.DataTruncation
import java.text.SimpleDateFormat
import java.util.*
import java.lang.Exception

class WaterPlantActivity: AppCompatActivity() {

    private val filename = "progress.txt"

    private val plantImages = arrayOf("plant1", "plant2", "plant3", "plant4")

    private lateinit var dayOne: ImageView
    private lateinit var dayTwo: ImageView
    private lateinit var dayThree: ImageView
    private lateinit var dayFour: ImageView
    private lateinit var dayFive: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waterplant_activity)

        dayOne = findViewById(R.id.dayOne)
        dayTwo = findViewById(R.id.dayTwo)
        dayThree = findViewById(R.id.dayThree)
        dayFour = findViewById(R.id.dayFour)
        dayFive = findViewById(R.id.dayFive)

        var info = readFromFile(filename)
        val separated = info.split("\n")
        val day = separated[1].split(" ")[1].toInt()

        if (day == 1){
            dayOne.visibility = View.VISIBLE
            dayTwo.visibility = View.INVISIBLE
            dayThree.visibility = View.INVISIBLE
            dayFour.visibility = View.INVISIBLE
            dayFive.visibility = View.INVISIBLE
        } else if (day == 2){
            dayOne.visibility = View.VISIBLE
            dayTwo.visibility = View.VISIBLE
            dayThree.visibility = View.INVISIBLE
            dayFour.visibility = View.INVISIBLE
            dayFive.visibility = View.INVISIBLE
        } else if (day == 3){
            dayOne.visibility = View.VISIBLE
            dayTwo.visibility = View.VISIBLE
            dayThree.visibility = View.VISIBLE
            dayFour.visibility = View.INVISIBLE
            dayFive.visibility = View.INVISIBLE
        } else if (day == 4) {
            dayOne.visibility = View.VISIBLE
            dayTwo.visibility = View.VISIBLE
            dayThree.visibility = View.VISIBLE
            dayFour.visibility = View.VISIBLE
            dayFive.visibility = View.INVISIBLE
        } else {
            dayOne.visibility = View.VISIBLE
            dayTwo.visibility = View.VISIBLE
            dayThree.visibility = View.VISIBLE
            dayFour.visibility = View.VISIBLE
            dayFive.visibility = View.VISIBLE
        }

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