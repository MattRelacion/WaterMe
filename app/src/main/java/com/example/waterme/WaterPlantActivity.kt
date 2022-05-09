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
import java.sql.DataTruncation
import java.text.SimpleDateFormat
import java.util.*
import java.lang.Exception

class WaterPlantActivity: AppCompatActivity() {

    private val filename = "progress.txt"

    private val plantImages = arrayOf("plant1", "plant2", "plant3", "plant4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waterplant_activity)
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