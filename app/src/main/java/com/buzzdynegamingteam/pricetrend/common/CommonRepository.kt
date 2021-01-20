package com.buzzdynegamingteam.pricetrend.common

import android.util.Log
import com.buzzdynegamingteam.pricetrend.common.models.*

object CommonRepository {
    private const val TAG = "CommonRepository"

    private val auth = AuthServices
    private val db = FirestoreServices

    /** Auth **/
    fun getCurrUser() = auth.getCurrUser()

    fun getCurrDisplayName(): String? {
        return auth.getCurrUserDisplayName()?: "Guest"
    }

    /** Listing **/
    suspend fun getListingDoc(listingDocID: String): Listing {
        return db.getListingDoc(listingDocID)!!
    }

    suspend fun getListingsByTag(tag: String): List<Listing> {
        return db.getListingDocByTag(tag)
    }

    suspend fun getAllListings(): List<Listing> {
        return db.getAllListings()
    }

    suspend fun getListingDataRows(trackingDocID: String, rows: Long): List<Data> {
        return db.getListingDataRows(trackingDocID, rows)
    }

    /** UserDoc **/
    suspend fun initCurrUserData(uid: String, uName: String = "Guest") {
        Log.e(TAG, "initCurrUserData: uid = $uid, displayName = $uName")
        //TODO db.isUserDocExist still isnt being called
        val isUserDocExist = db.isUserDocExist(uid)
        Log.e(TAG, "initCurrUserData: isDocExist $isUserDocExist")
        if(!isUserDocExist) {
            Log.e(TAG, "initCurrUserData: UserDoc does NOT exist")
            db.createUserDoc(uid, uName)
        }
    }

    suspend fun getUserData(): User? {
        return try {
            Log.i(TAG, "_userValue: before, db.getUsr: ${db.getUserDoc(auth.getCurrUserUID()!!)!!}")
            db.getUserDoc(auth.getCurrUserUID()!!)!!
        } catch (e: Exception) {
            Log.e(TAG, "getUserData: Error in getting data", e)
            null
        }
    }

    suspend fun getUserActiveTrackingIDs(): List<String>? {
        return try {
            Log.i(TAG, "_userValue: before, db.getUsr: ${db.getUserDoc(auth.getCurrUserUID()!!)!!}")
            db.getUserDoc(auth.getCurrUserUID()!!)!!.activeTrackingMetadata
        } catch (e: Exception) {
            Log.e(TAG, "getUserData: Error in getting data", e)
            null
        }
    }

    suspend fun getListOfUserTrackings(): MutableList<Tracking> {
        val listOfTracking = db.getUserTrackings(auth.getCurrUserUID()!!)
        for (x in listOfTracking) {
            x.listingDocID?.let {
                x.listing = db.getListingDoc(x.listingDocID)
            }
        }
        return listOfTracking
    }

    suspend fun getTracking(trackingDocID: String): Tracking {
        val tracking = db.getTracking(auth.getCurrUserUID()!!, trackingDocID)
        tracking.listingDocID?.let {
            tracking.listing = db.getListingDoc(tracking.listingDocID)
        }

        return tracking
    }

    suspend fun createTracking(tracking: Tracking) {
        db.createTracking(auth.getCurrUserUID()!!, tracking)
    }

    suspend fun deleteTracking(trackingDocID: String, listingID: String?) {
        db.deleteTracking(auth.getCurrUserUID()!!, trackingDocID, listingID)
    }

    suspend fun getListOfUserSavings(): MutableList<Saving> {
        return db.getUserSavings(auth.getCurrUserUID()!!)
    }

    suspend fun getSaving(savingDocID: String): Saving {
        return db.getSaving(auth.getCurrUserUID()!!, savingDocID)
    }

    suspend fun createSavingHistory(tracking: Tracking) {
        db.createSavingHistory(auth.getCurrUserUID()!!, tracking)
    }

    /** Scraper **/
    suspend fun getUserRequests(): List<Request> {
        return db.getRequests(auth.getCurrUserUID()!!).sortedWith(compareBy {it.statusCode})
    }

    suspend fun createRequest(url: String) {
        val req = Request(url, 0, arrayListOf(auth.getCurrUserUID()!!))
        db.createRequest(req)
    }
}