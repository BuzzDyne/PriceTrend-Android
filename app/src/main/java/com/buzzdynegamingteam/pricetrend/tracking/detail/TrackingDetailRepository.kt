package com.buzzdynegamingteam.pricetrend.tracking.detail

import android.util.Log
import com.buzzdynegamingteam.pricetrend.common.AuthServices
import com.buzzdynegamingteam.pricetrend.common.FirestoreServices
import com.buzzdynegamingteam.pricetrend.common.models.Data
import com.buzzdynegamingteam.pricetrend.common.models.Tracking

object TrackingDetailRepository {
    private const val TAG = "TrackingDetailRepo"

    private val auth = AuthServices
    private val db = FirestoreServices

    suspend fun getTracking(trackingDocID: String): Tracking {
        // TODO Handle if a tracking item inside list is null
        // TODO Receive selected Tracking item to pass its DocID

        val tracking = db.getTracking(auth.getCurrUserUID()!!, trackingDocID)
        tracking.listingDocID?.let {
            tracking.listing = db.getListingDoc(tracking.listingDocID)
        }

        return tracking
    }

    suspend fun getListingDataRows(trackingDocID: String, rows: Long): List<Data> {
        return db.getListingDataRows(trackingDocID, rows)
    }
}