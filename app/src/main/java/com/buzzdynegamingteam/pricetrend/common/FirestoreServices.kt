package com.buzzdynegamingteam.pricetrend.common

import android.util.Log
import com.buzzdynegamingteam.pricetrend.common.models.User
import com.buzzdynegamingteam.pricetrend.common.models.User.Companion.toUser
//import com.buzzdynegamingteam.pricetrend.common.models.User.Companion.toUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

const val TAG = "FirestoreServices"

object FirestoreServices {
    private val db = FirebaseFirestore.getInstance()

    suspend fun isUserDocExist(uid: String): Boolean {
        val userDoc = db.collection("Users").document(uid).get().await()
//        Log.i(TAG, "doesUserDocExist: ${userDoc.toObject(User::class.java)}")
        return userDoc.exists()
    }

    suspend fun getUserDoc(uid: String): User? {
        val userDoc = db.collection("Users").document(uid).get().await()
//        return userDoc.toObject(User::class.java)
        //was used with custom companion converter
        return userDoc.toUser()
    }

    suspend fun createUserDoc(uid: String, name: String) {
        val u = User(displayName = name)
        db.collection("Users").document(uid).set(u).await()
    }
}