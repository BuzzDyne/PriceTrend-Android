package com.buzzdynegamingteam.pricetrend.tracking.detail

import com.buzzdynegamingteam.pricetrend.common.AuthServices
import com.buzzdynegamingteam.pricetrend.common.FirestoreServices
import com.buzzdynegamingteam.pricetrend.common.models.Tracking

object TrackingDetailRepository {
    private const val TAG = "TrackingDetailRepository"

    private val auth = AuthServices
    private val db = FirestoreServices

    suspend fun getTracking(): Tracking {
        // TODO Handle if a tracking item inside list is null

        // TODO Receive selected Tracking item to pass its DocID
        val trackingDocID = "RcjE8v3pnLaQkvF7UnOq"

        val tracking = db.getTracking(auth.getCurrUserUID()!!, trackingDocID)
        tracking.listingDocID?.let {
            tracking.listing = db.getListingData(tracking.listingDocID)
        }

        return tracking
    }
}