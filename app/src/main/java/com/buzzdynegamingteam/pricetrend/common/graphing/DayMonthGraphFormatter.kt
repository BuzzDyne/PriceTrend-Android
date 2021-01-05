package com.buzzdynegamingteam.pricetrend.common.graphing

import android.annotation.SuppressLint
import android.util.Log
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class DayMonthGraphFormatter(val refTimestamp: Long): ValueFormatter() {
    private val TAG = "DayMonthFormatter"

    private val mDate = Date()
    @SuppressLint("SimpleDateFormat")
    val mDataFormat = SimpleDateFormat("dd MM")

    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        val convTimestamp = value.toLong()

        val originalTs = refTimestamp + convTimestamp

        Log.e(TAG, "getFormattedValue: output: ($originalTs) (${getDayMonth(originalTs)})")

        return getDayMonth(originalTs)
    }

    private fun getDayMonth(timestamp: Long): String {
        return try {
            mDate.time = timestamp
            mDataFormat.format(mDate)
        } catch (e: Exception) {
            Log.e(TAG, "getDayMonth: ", e)
            "xx"
        }
    }
}