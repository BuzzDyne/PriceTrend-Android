package com.buzzdynegamingteam.pricetrend.common.models

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import java.lang.Exception
import java.util.*

data class Saving(
    @DocumentId
    val documentId: String? = null,

    val listingName         : String? = null,
    val listingID           : String? = null,
    val listingURL          : String? = null,
    val listingImgURL       : String? = null,
    val listingThumbURL     : String? = null,

    val storeName           : String? = null,
    val storeArea           : String? = null,

    val startData           : StartEndData? = StartEndData(),
    val endData             : StartEndData? = StartEndData(),

    @get:Exclude
    var isExpanded          : Boolean = false
) {
    companion object {
        fun DocumentSnapshot.toSaving(): Saving? {
            return try {
                val docID = id
                val listingName         = getString("listingName")
                val listingID           = getString("listingID")
                val listingURL          = getString("listingURL")
                val listingImgURL       = getString("listingImgURL")
                val listingThumbURL     = getString("listingThumbURL")
                val storeName           = getString("storeName")
                val storeArea           = getString("storeArea")
                @Suppress("UNCHECKED_CAST")
                val startData           = StartEndData.fromFsMap(get("startData") as Map<String, *>)
                @Suppress("UNCHECKED_CAST")
                val endData             = StartEndData.fromFsMap(get("endData") as Map<String, *>)

                Saving(
                    docID, listingName, listingID, listingURL, listingImgURL, listingThumbURL,
                    storeName, storeArea, startData, endData
                )
            } catch (e: Exception) {
                Log.e("Saving", "Error converting Saving data", e)
                null
            }
        }
    }
}

data class StartEndData(
    var ts              : Date = Date(),
    var sold            : Long? = null,
    var stock           : Long? = null,
    var seen            : Long? = null,
    var reviewCount     : Long? = null,
    var reviewScore     : Long? = null,
    var price           : Long? = null
) {
    companion object {
        fun fromFsMap(data: Map<String, *>): StartEndData? {
            return try {
                val ts              = data["ts"] as Timestamp
                val tsDate          = ts.toDate()
                val sold            = data["sold"]          as Long
                val seen            = data["seen"]          as Long
                val stock           = data["stock"]         as Long
                val reviewCount     = data["reviewCount"]   as Long
                val reviewScore     = data["reviewScore"]   as Long
                val price           = data["price"]         as Long

                StartEndData(tsDate, sold, seen, stock, reviewCount, reviewScore, price)
            } catch (e: Exception) {
                Log.e("StartEndData", "fsToLatestData: error converting", e)
                null
            }
        }

        fun toFsMap(data: StartEndData): Map<String, *> {
             return hashMapOf(
                "ts"            to data.ts,
                "sold"          to data.sold,
                "stock"         to data.stock,
                "seen"          to data.seen,
                "reviewCount"   to data.reviewCount,
                "reviewScore"   to data.reviewScore,
                "price"         to data.price
            )
        }
    }
}

