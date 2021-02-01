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

        fun formatPriceToRupiah(x: Float) : String {
            return formatPriceToRupiah(x.toLong())
        }

        @SuppressLint("SimpleDateFormat")
        fun formatDateToString(x:Date?, format: String = "dd MMM yyyy HH:mm") : String {
            val formatter = SimpleDateFormat(format)
            return try {
                formatter.format(x)
            } catch (e: Exception) {
                Log.e(TAG, "formatDateToString: " , e)
                "null!"
            }
        }

        fun extractLinkFromString(input: String): String {
            val pat = Regex("((https|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)")

            val res = pat.find(input)

            return res?.value ?: ""
        }

        fun queryToTags(input: String): List<String> {
            /**
             * Will returns List of tags from given input
             * - Max Output Tags is 10 elements
             */

            val cleansedInput = filterNonAlphanumeric(input)

            val tokenizedQuery = cleansedInput.split(" ")
            val tokenLength = tokenizedQuery.size - 1


            val res = mutableListOf<String>()

            for (i in 0..tokenLength) {
                val kywd = tokenizedQuery.slice(0..tokenLength-i)

                val single = kywd.joinToString(" ")

                res.add(single)
            }


            for (x in 1..tokenLength) {
                res.add(tokenizedQuery.get(x))
            }

            return res.take(10)
        }

        private fun filterNonAlphanumeric(input: String): String {
            val pat = Regex("[^0-9a-zA-Z]+")
            return pat.replace(input," ").toLowerCase(Locale.getDefault())
        }
    }
}