package com.buzzdynegamingteam.pricetrend.common

import android.annotation.SuppressLint
import android.util.Log
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.math.RoundingMode
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "StringFormatter"

class StringFormatter {

    companion object {
        fun formatPriceToRupiah(x:Long?) : String {
            val format = NumberFormat.getCurrencyInstance()
            format.currency = Currency.getInstance("IDR")
            format.roundingMode = RoundingMode.FLOOR
            format.maximumFractionDigits = 0

            return try {
                format.format(x)
            } catch (e: IllegalArgumentException) {
                Log.e(TAG, "formatPriceToRupiah: x is null", e)
                "null!"
            } catch (e: Exception) {
                Log.e(TAG, "formatPriceToRupiah: Unknown Error", e)
                "ERROR"
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun formatDateToString(x:Date?) : String {
            val formatter = SimpleDateFormat("dd MMM yyyy HH:mm")
            return try {
                formatter.format(x)
            } catch (e: Exception) {
                Log.e(TAG, "formatDateToString: " , e)
                "null!"
            }
        }
    }
}