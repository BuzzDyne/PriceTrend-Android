package com.buzzdynegamingteam.pricetrend.common

import android.util.Log
import com.buzzdynegamingteam.pricetrend.home.HomeRepository
import com.google.firebase.auth.FirebaseAuth

object AuthServices {
    private val auth = FirebaseAuth.getInstance()

    fun getCurrUserDisplayName(): String? {
        return auth.currentUser?.displayName
    }

    fun getCurrUserUID(): String? {
        return auth.currentUser?.uid
    }

    fun isLoggedIn(): Boolean? {
        return auth.currentUser != null
    }
}