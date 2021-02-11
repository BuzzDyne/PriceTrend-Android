package com.buzzdynegamingteam.pricetrend.common.models

import android.util.Log
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude


data class User(
    var displayName: String?= null,
    var profilePicUrl: String? = null,
    var totalSaving: Long? = 0,
    var trxCount: Long? = 0,
    var activeTrackingMetadata: List<String>? = listOf(),
    var fcmToken: String? = null
//    val documentId: String? = null
) {
    companion object {
        val TAG = "User"
        fun DocumentSnapshot.toUser(): User? {
            return try {
//                val docID   = id
                val name    = getString("displayName")
                val picUrl  = getString("profilePicUrl")
                val tS      = getLong("totalSaving")
                val tC      = getLong("trxCount")
                @Suppress("UNCHECKED_CAST")
                val aTM     = get("activeTrackingMetadata") as List<String>?
                val fT      = getString("fcmToken")

//                Log.i(TAG, "_userValue: User.toString: ${User(name,picUrl,tS,tC,aTM,docID).toString()}")
                User(name,picUrl,tS,tC,aTM,fT)
//                User(docID, name, picUrl, tS, tC, aTM)
            } catch (e: Exception) {
                Log.e("User", "Error converting user profile", e)
                null
            }
        }
    }
}