package com.buzzdynegamingteam.pricetrend.tracking.list

import com.buzzdynegamingteam.pricetrend.common.AuthServices
import com.buzzdynegamingteam.pricetrend.common.FirestoreServices
import com.buzzdynegamingteam.pricetrend.common.models.Tracking

object TrackingListRepository {
    private const val TAG = "TrackingRepository"

    private val auth = AuthServices
    private val db = FirestoreServices

    suspend fun getListOfUserTrackings(): MutableList<Tracking> {
        // TODO Handle if a tracking item inside list is null
        val listOfTracking = db.getUserTrackings(auth.getCurrUserUID()!!)
        for (x in listOfTracking) {
            x.listingDocID?.let {
                x.listing = db.getListingData(x.listingDocID)
            }
        }
        return listOfTracking
    }
}