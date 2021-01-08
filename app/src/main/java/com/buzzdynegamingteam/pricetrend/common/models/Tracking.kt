package com.buzzdynegamingteam.pricetrend.common.models

import android.util.Log
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import java.lang.Exception
import java.util.*

/***
 * Represents a single tracking of a User
 */
data class Tracking (
    @DocumentId
    val documentID: String? = null,
    val listingDocID: String? = null,
    val listingID: String? = null,
    val onProcess: Boolean? = null,
    val startPrice: Long? = null,
    val startSoldCount: Long? = null,
    val startSeenCount: Long? = null,
    val startStockCount: Long? = null,
    val startReviewCount: Long? = null,
    val startReviewScore: Long? = null,
    val startDate: Date? = Date(),
    @get:Exclude
    var listing: Listing? = null
) {
    companion object {
        fun DocumentSnapshot.toTracking() : Tracking? {
            return try {
                val docID = id
                val listingDocID    = getString("listingDocID")
                val listingID       = getString("listingID")
                val onProcess       = getBoolean("onProcess")
                val startPrice      = getLong("startPrice")
                val startSoldCount  = getLong("startSoldCount")
                val startSeenCount  = getLong("startSeenCount")
                val startStockCount = getLong("startStockCount")
                val startReviewCount= getLong("startReviewCount")
                val startReviewScore= getLong("startReviewScore")
                val startDate       = getDate("startDate")

                Tracking(
                        docID, listingDocID, listingID, onProcess, startPrice,
                        startSoldCount, startSeenCount, startStockCount,
                        startReviewCount, startReviewScore, startDate
                )
            } catch (e: Exception) {
                Log.e("Tracking", "Error converting tracking data", e)
                null
            }
        }
    }
}