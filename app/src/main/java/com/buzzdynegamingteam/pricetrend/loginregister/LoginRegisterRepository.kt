package com.buzzdynegamingteam.pricetrend.loginregister

import com.buzzdynegamingteam.pricetrend.common.AuthServices
import com.buzzdynegamingteam.pricetrend.common.FirestoreServices
import com.buzzdynegamingteam.pricetrend.common.models.User

object LoginRegisterRepository {
    private const val TAG = "LoginRegisterRepository"

    private val auth = AuthServices
    private val db = FirestoreServices

    suspend fun initUserData(uid: String) {
        if(!db.isUserDocExist(uid)) {
            db.createUserDoc(uid, auth.getCurrUserDisplayName()!!)
        }
    }

    suspend fun initCurrUserData() {
        val uid = auth.getCurrUserUID()!!

        if(!db.isUserDocExist(uid)) {
            val name = auth.getCurrUserDisplayName() ?: "Guest"
            db.createUserDoc(uid, name)
        }
    }

    fun isUserLoggedIn() = auth.isLoggedIn()


    fun getCurrDisplayName(): String? {
        return auth.getCurrUserDisplayName()
    }
}