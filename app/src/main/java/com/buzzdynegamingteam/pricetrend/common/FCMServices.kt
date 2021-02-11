package com.buzzdynegamingteam.pricetrend.common

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

object FCMServices {
    private const val TAG = "FCMService"

    private val fcm = FirebaseMessaging.getInstance()

    suspend fun getFCMToken() : String {
        val token = fcm.token.await()

        return if (token.isEmpty()) {
            "-"
        } else {
            token
        }
    }
}