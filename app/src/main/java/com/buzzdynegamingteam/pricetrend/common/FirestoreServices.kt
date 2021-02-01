package com.buzzdynegamingteam.pricetrend.common

import android.util.Log
import com.buzzdynegamingteam.pricetrend.common.models.*
import com.buzzdynegamingteam.pricetrend.common.models.Listing.Companion.toListing
import com.buzzdynegamingteam.pricetrend.common.models.Request.Companion.toRequest
import com.buzzdynegamingteam.pricetrend.common.models.Saving.Companion.toSaving
import com.buzzdynegamingteam.pricetrend.common.models.Tracking.Companion.toTracking
import com.buzzdynegamingteam.pricetrend.common.models.User.Companion.toUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

object FirestoreServices {
    private const val TAG = "FirestoreServices"

    private val db = FirebaseFirestore.getInstance()

    suspend fun isUserDocExist(uid: String): Boolean {
        val userDoc = db.collection("Users").document(uid).get().await()
//        Log.i(TAG, "doesUserDocExist: ${userDoc.toObject(User::class.java)}")
        return userDoc.exists()
    }

    suspend fun createUserDoc(uid: String, name: String) {
        val u = User(displayName = name)
        Log.e(TAG, "createUserDoc: creating user $u with uid ($uid)")
        db.collection("Users").document(uid).set(u).await()
    }

    suspend fun getUserDoc(uid: String): User? {
        val userDoc = db.collection("Users").document(uid).get().await()
        return userDoc.toUser()
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

    suspend fun createTracking(uid: String, tracking: Tracking) {
        val userRef = db.collection("Users")
                        .document(uid)

        // Create TrackingDoc to 'activeTrackings' col
        userRef.collection("activeTrackings")
                .add(tracking).await()

        // Add ListingID to 'activeTrackingMetadata' arr
        userRef.update("activeTrackingMetadata", FieldValue.arrayUnion(tracking.listingID))
    }

    suspend fun getTracking(uid: String, trackingDocID: String): Tracking {
        val trackingDoc = db.collection("Users")
            .document(uid)
            .collection("activeTrackings")
            .document(trackingDocID)
            .get().await()

        return trackingDoc.toTracking() ?: Tracking()
    }

    suspend fun deleteTracking(uid: String, trackingDocID: String, listingID: String?) {
        val userDocRef = db.collection("Users")
                            .document(uid)
        userDocRef.collection("activeTrackings")
            .document(trackingDocID)
            .delete().await()

        userDocRef.update("activeTrackingMetadata", FieldValue.arrayRemove(listingID))

    }

    suspend fun getUserSavings(uid: String) : MutableList<Saving> {
        val savingDocs = db.collection("Users")
            .document(uid)
            .collection("savingsHistory")
            .get().await()

        val listOfSaving = mutableListOf<Saving>()

        for (doc in savingDocs.documents) {
            listOfSaving.add( doc.toSaving() ?: Saving())
        }

        return listOfSaving
    }

    suspend fun getSaving(uid: String, savingDocID: String): Saving {
        val savingDoc = db.collection("Users")
            .document(uid)
            .collection("savingsHistory")
            .document(savingDocID)
            .get().await()

        return savingDoc.toSaving() ?: Saving()
    }

    suspend fun createSavingHistory(uid: String, tracking: Tracking) {
        val listingName         = tracking.listing!!.listingName
        val listingID           = tracking.listing!!.listingID
        val listingURL          = tracking.listing!!.listingURL
        val listingImgURL       = tracking.listing!!.listingImgURL
        val listingThumbUrl     = tracking.listing!!.listingThumbUrl
        val storeName           = tracking.listing!!.storeName
        val storeArea           = tracking.listing!!.storeArea
        val startData           = StartEndData(
            tracking.startDate!!, tracking.startSoldCount, tracking.startStockCount,
            tracking.startSeenCount, tracking.startReviewCount, tracking.startReviewScore,
            tracking.startPrice
        )
        val latestData = tracking.listing!!.latestData!!
        val endData             = StartEndData(
            latestData.ts, latestData.sold, latestData.stock,
            latestData.seen, latestData.reviewCount, latestData.reviewScore,
            latestData.price
        )

        val data = Saving(
            null, listingName, listingID, listingURL,
            listingImgURL, listingThumbUrl, storeName,
            storeArea, startData, endData
        )

        db.collection("Users")
            .document(uid)
            .collection("savingsHistory")
            .add(data).await()
    }

    suspend fun getListingDoc(listingDocID: String) : Listing? {
        val listingDoc = db.collection("Listings").document(listingDocID).get().await()
        return listingDoc.toListing()
    }

    suspend fun getListingDocByTag(tag: String) : List<Listing> {
        val resultDocs = db.collection("Listings")
            .whereArrayContains("tags", tag)
            .get().await()

        val listOfResults = mutableListOf<Listing>()

        for (doc in resultDocs.documents) {
            listOfResults.add(doc.toListing() ?: Listing())
        }

        return listOfResults.toList()
    }

    suspend fun getListingDocByQuery(query: List<String>) : List<Listing> {
        Log.e(TAG, "getListingDocByQuery: query = $query")
        val resultDocs = db.collection("Listings")
                .whereArrayContainsAny("tags", query)
                .get().await()

        val listOfResults = mutableListOf<Listing>()

        for (doc in resultDocs.documents) {
            listOfResults.add(doc.toListing() ?: Listing())
        }

        return listOfResults.toList()
    }

    suspend fun getAllListings(): List<Listing> {
        val resultDocs = db.collection("Listings")
                .get().await()

        val listOfResults = mutableListOf<Listing>()

        for (doc in resultDocs.documents) {
            listOfResults.add(doc.toListing() ?: Listing())
        }

        return listOfResults.toList()
    }

    suspend fun getListingDataRows(listingDocID: String, rows: Long) : List<Data> {
        val listOfListingData = mutableListOf<Data>()

        val listingDataDocs = db.collection("Listings/$listingDocID/data")
                .orderBy("ts", Query.Direction.DESCENDING)
                .limit(rows)
                .get().await()

        for (doc in listingDataDocs.documents) {
            listOfListingData.add( doc.toObject(Data::class.java)!! )
        }

        return listOfListingData
    }

    suspend fun getRequests(uid: String): List<Request> {
        val reqDocs = db.collection("Scraper/newListing/UrlList")
            .whereArrayContains("users", uid)
            .get().await()

        val listOfReq = mutableListOf<Request>()

        for (doc in reqDocs.documents) {
            listOfReq.add( doc.toRequest() ?: Request())
        }

        return listOfReq
    }

    suspend fun createRequest(request: Request) {
        val colRef = db.collection("Scraper/newListing/UrlList")

        val data = hashMapOf(
            "url"            to request.url,
            "statusCode"     to request.statusCode,
            "requestTs"      to FieldValue.serverTimestamp(),
            "responseTs"     to null,
            "listingDocAddr" to null,
            "users"          to request.users
        )
        colRef.add(data).await()
    }
}