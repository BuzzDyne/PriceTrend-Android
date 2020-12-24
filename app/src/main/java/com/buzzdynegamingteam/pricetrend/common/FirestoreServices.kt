package com.buzzdynegamingteam.pricetrend.common

import android.util.Log
import com.buzzdynegamingteam.pricetrend.common.models.Listing
import com.buzzdynegamingteam.pricetrend.common.models.Listing.Companion.toListing
import com.buzzdynegamingteam.pricetrend.common.models.Tracking
import com.buzzdynegamingteam.pricetrend.common.models.Tracking.Companion.toTracking
import com.buzzdynegamingteam.pricetrend.common.models.User
import com.buzzdynegamingteam.pricetrend.common.models.User.Companion.toUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirestoreServices {
    private const val TAG = "FirestoreServices"

    private val db = FirebaseFirestore.getInstance()

    suspend fun isUserDocExist(uid: String): Boolean {
        val userDoc = db.collection("Users").document(uid).get().await()
//        Log.i(TAG, "doesUserDocExist: ${userDoc.toObject(User::class.java)}")
        return userDoc.exists()
    }

    suspend fun getUserDoc(uid: String): User? {
        val userDoc = db.collection("Users").document(uid).get().await()
        return userDoc.toUser()
    }

    suspend fun createUserDoc(uid: String, name: String) {
        val u = User(displayName = name)
        Log.e(TAG, "createUserDoc: creating user $u with uid ($uid)")
        db.collection("Users").document(uid).set(u).await()
    }

    suspend fun getUserTrackings(uid: String): MutableList<Tracking> {
        val trackingDocs = db.collection("Users")
                .document(uid)
                .collection("activeTrackings")
                .get().await()

        val listOfTracking = mutableListOf<Tracking>()

        // Get all initial tracking data
        for (doc in trackingDocs.documents) {
            listOfTracking.add( doc.toTracking() ?: Tracking() )
        }
        return  listOfTracking
    }

    suspend fun getTracking(uid: String, trackingDocID: String): Tracking {
        val trackingDoc = db.collection("Users")
            .document(uid)
            .collection("activeTrackings")
            .document(trackingDocID)
            .get().await()

        return trackingDoc.toTracking() ?: Tracking()
    }

    suspend fun getListingData(listingDocID: String) : Listing? {
        val listingDoc = db.collection("Listings").document(listingDocID).get().await()
        return listingDoc.toListing()
    }
}