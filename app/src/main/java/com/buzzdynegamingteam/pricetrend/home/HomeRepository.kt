package com.buzzdynegamingteam.pricetrend.home

import android.util.Log
import com.buzzdynegamingteam.pricetrend.common.AuthServices
import com.buzzdynegamingteam.pricetrend.common.FirestoreServices
import com.buzzdynegamingteam.pricetrend.common.models.Tracking
import com.buzzdynegamingteam.pricetrend.common.models.User
import com.buzzdynegamingteam.pricetrend.loginregister.LoginRegisterRepository

object HomeRepository {
    private const val TAG = "HomeRepository"

    private val auth = AuthServices
    private val db = FirestoreServices

    suspend fun getUserData(): User? {
        return try {
            Log.i(TAG, "_userValue: before, db.getUsr: ${db.getUserDoc(auth.getCurrUserUID()!!)!!}")
            db.getUserDoc(auth.getCurrUserUID()!!)!!
        } catch (e: Exception) {
            Log.e(TAG, "getUserData: Error in getting data", e)
            null
        }
    }

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

    fun getCurrUser() = auth.getCurrUser()
}