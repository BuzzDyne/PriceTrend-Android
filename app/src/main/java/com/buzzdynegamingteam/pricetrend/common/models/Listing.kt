package com.buzzdynegamingteam.pricetrend.common.models

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.lang.Exception
import java.util.*

data class Listing (
    val documentId          : String? = null,

    val statActiveTracking  : Long? = null,
    val statBought          : Long? = null,

    val listingName         : String? = null,
    val listingID           : String? = null,
    val listingURL          : String? = null,
    val listingImgURL       : String? = null,

    val storeName           : String? = null,
    val storeArea           : String? = null,

    val latestData          : LatestData? = LatestData()
) {
    val data                : Data = Data()

    companion object {
        fun DocumentSnapshot.toListing() : Listing? {
            return try {
                val docID = id
                val statActiveTracking  = getLong("statActiveTracking")
                val statBought          = getLong("statBought")
                val listingName         = getString("listingName")
                val listingID           = getLong("listingID").toString()
                val listingURL          = getString("listingURL")
                val listingImgURL       = getString("listingImgURL")
                val storeName           = getString("storeName")
                val storeArea           = getString("storeArea")
                @Suppress("UNCHECKED_CAST")
                val latestData          = LatestData.fsToLatestData(get("latestData") as Map<String, *>)

                Listing(
                    docID, statActiveTracking, statBought, listingName, listingID,
                        listingURL, listingImgURL, storeName, storeArea, latestData
                )
            } catch (e: Exception) {
                Log.e("Tracking", "Error converting tracking data", e)
                null
            }
        }
    }
}

data class LatestData(
    var ts              : Date = Date(),
    var sold            : Long? = null,
    var seen            : Long? = null,
    var stock           : Long? = null,
    var reviewCount     : Long? = null,
    var reviewScore     : Long? = null,
    var price           : Long? = null,
    var prevPrice       : Long? = null
) {
    companion object {
        fun fsToLatestData(data: Map<String, *>): LatestData? {
            return try {
                val ts              = data["ts"] as Timestamp
                val tsDate          = ts.toDate()
                val sold            = data["sold"]          as Long
                val seen            = data["seen"]          as Long
                val stock           = data["stock"]         as Long
                val reviewCount     = data["reviewCount"]   as Long
                val reviewScore     = data["reviewScore"]   as Long
                val price           = data["price"]         as Long
                val prevPrice       = data["prevPrice"]     as Long

                LatestData(tsDate, sold, seen, stock, reviewCount, reviewScore, price, prevPrice)
            } catch (e: Exception) {
                Log.e("LatestData", "fsToLatestData: error converting", e)
                null
            }
        }
    }
}

data class Data(
        var ts              : Date = Date(),
        var sold            : Long? = null,
        var seen            : Long? = null,
        var stock           : Long? = null,
        var reviewCount     : Long? = null,
        var reviewScore     : Long? = null,
        var price           : Long? = null
)