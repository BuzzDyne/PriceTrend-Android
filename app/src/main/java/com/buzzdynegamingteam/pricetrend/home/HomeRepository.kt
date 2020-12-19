package com.buzzdynegamingteam.pricetrend.home

import android.util.Log
import com.buzzdynegamingteam.pricetrend.common.AuthServices
import com.buzzdynegamingteam.pricetrend.common.FirestoreServices
import com.buzzdynegamingteam.pricetrend.common.models.User
import com.buzzdynegamingteam.pricetrend.loginregister.LoginRegisterRepository

object HomeRepository {
    private const val TAG = "HomeRepository"

    private val auth = AuthServices
    private val db = FirestoreServices

    suspend fun getUserData(): User? {
        return try {
            Log.i(TAG, "_userValue: before, db.getUsr: ${db.getUserDoc(auth.getCurrUserUID()!!)!!}")
            db.getUserDoc(auth.getCurrUserUID()!!)!!
        } catch (e: Exception) {
            Log.e(TAG, "getUserData: Error in getting data", e)
            null
        }
    }


}