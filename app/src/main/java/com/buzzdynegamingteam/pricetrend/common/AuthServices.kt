package com.buzzdynegamingteam.pricetrend.common

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AuthServices {
    private val auth = FirebaseAuth.getInstance()

    fun getCurrUserDisplayName(): String? {
        return auth.currentUser?.displayName
    }

    fun getCurrUserUID(): String? {
        return auth.currentUser?.uid
    }

    fun getCurrUser(): FirebaseUser? {
        return auth.currentUser
    }
}