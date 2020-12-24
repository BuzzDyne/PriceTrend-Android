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

    suspend fun initCurrUserData() {
        val uid = auth.getCurrUserUID()!!
        Log.e(TAG, "initCurrUserData: uid = $uid")
        //TODO db.isUserDocExist still isnt being called
        Log.e(TAG, "initCurrUserData: isDocExist ${db.isUserDocExist(uid)}")
        if(!db.isUserDocExist(uid)) {
            Log.e(TAG, "initCurrUserData: UserDoc does NOT exist")
            val name = auth.getCurrUserDisplayName() ?: "Guest"
            Log.e(TAG, "initCurrUserData: currUserName from auth : $name")
            db.createUserDoc(uid, name)
        }
//        val name = auth.getCurrUserDisplayName() ?: "Guest"
//        db.createUserDoc(uid, name)
    }

    fun isUserLoggedIn() = auth.isLoggedIn()


    fun getCurrDisplayName(): String? {
        return auth.getCurrUserDisplayName()
    }
}