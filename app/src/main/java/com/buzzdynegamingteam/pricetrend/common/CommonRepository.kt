package com.buzzdynegamingteam.pricetrend.common

import com.buzzdynegamingteam.pricetrend.common.models.Data
import com.buzzdynegamingteam.pricetrend.common.models.Listing

object CommonRepository {
    private const val TAG = "CommonRepository"

    private val auth = AuthServices
    private val db = FirestoreServices

    suspend fun getListingDoc(listingDocID: String): Listing {
        return db.getListingDoc(listingDocID)!!
    }

    suspend fun getListingDataRows(trackingDocID: String, rows: Long): List<Data> {
        return db.getListingDataRows(trackingDocID, rows)
    }

}