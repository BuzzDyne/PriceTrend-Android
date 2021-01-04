package com.buzzdynegamingteam.pricetrend.search

import com.buzzdynegamingteam.pricetrend.common.AuthServices
import com.buzzdynegamingteam.pricetrend.common.FirestoreServices
import com.buzzdynegamingteam.pricetrend.common.models.Listing

object SearchRepository {
    private const val TAG = "SearchRepository"

    private val auth = AuthServices
    private val db = FirestoreServices

    suspend fun getListingsByTag(tag: String): List<Listing> {
        return db.getListingDocByTag(tag)
    }
}