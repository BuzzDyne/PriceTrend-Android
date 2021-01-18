package com.buzzdynegamingteam.pricetrend.common.models

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import java.lang.Exception

data class Request(
    var url             : String? = null,
    var statusCode      : Long? = -1,
    var users           : ArrayList<String>? = null,
    var listingDocAddr  : String? = null,

    @get:Exclude
    var isExpanded          : Boolean = false
) {
    companion object {
        fun DocumentSnapshot.toRequest(): Request? {
            return try {
                val url             = getString("url")
                val statusCode      = getLong("statusCode")
                val users           = get("users") as ArrayList<String>
                val listingDocAddr  = getString("listingDocAddr") ?: ""

                Request(url, statusCode, users, listingDocAddr)
            } catch (e: Exception) {
                Log.e("Request", "Error converting request", e)
                null
            }
        }

    }
}