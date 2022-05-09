package com.example.waterme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import java.text.SimpleDateFormat
import java.util.*

abstract class DateChangedBroadcastReceiver: BroadcastReceiver() {

    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    private lateinit var curDate: String
    private var dateOnFile: String = "00/00/0000"

    abstract fun dateChanged(onFileDate: String, curDate: String)

    private fun register(context: Context, dateOnFile: String) {
        curDate = dateFormat.format(Calendar.getInstance().time)
        this.dateOnFile = dateOnFile
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_TIME_CHANGED)
        filter.addAction(Intent.ACTION_DATE_CHANGED)
        filter.addAction(Intent.ACTION_TIME_TICK)
        context.registerReceiver(this, filter)
        if (dateOnFile.split("/")[0] < curDate.split("/")[0] ||
            dateOnFile.split("/")[1] < curDate.split("/")[1] ||
            dateOnFile.split("/")[2] < curDate.split("/")[2]) {

            dateChanged(dateOnFile, curDate)
        }
    }

    fun registerOnStart(activity: AppCompatActivity, date: String, fragment: Fragment? = null) {
        register(activity, date)
        val lifecycle = fragment?.lifecycle?: activity.lifecycle
        lifecycle.addObserver(object: LifecycleObserver {
            fun onStop(){
                lifecycle.removeObserver(this)
                activity.unregisterReceiver(this@DateChangedBroadcastReceiver)
            }
        })
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val curDate =  dateFormat.format(Calendar.getInstance().time)
        Log.i(TAG, "onReceive: dateOnFile= $dateOnFile , curDate=$curDate")
        if (dateOnFile.split("/")[0] < curDate.split("/")[0] ||
            dateOnFile.split("/")[1] < curDate.split("/")[1] ||
            dateOnFile.split("/")[2] < curDate.split("/")[2]) {
            dateChanged(dateOnFile, curDate)
        }

    }

    companion object {
        private const val TAG = "Water-Me"
    }
}