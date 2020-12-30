package com.buzzdynegamingteam.pricetrend.loginregister

import android.util.Log
import com.buzzdynegamingteam.pricetrend.common.AuthServices
import com.buzzdynegamingteam.pricetrend.common.FirestoreServices
import com.buzzdynegamingteam.pricetrend.common.models.User

object LoginRegisterRepository {
    private const val TAG = "LoginRegisterRepository"

    private val auth = AuthServices
    val db = FirestoreServices

    suspend fun initUserData(uid: String) {
        if(!db.isUserDocExist(uid)) {
            db.createUserDoc(uid, auth.getCurrUserDisplayName()!!)
        }
    }

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

    fun getCurrUser() = auth.getCurrUser()

    fun getCurrDisplayName(): String? {
        return auth.getCurrUserDisplayName()?: "Guest"
    }
}