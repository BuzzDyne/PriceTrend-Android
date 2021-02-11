package com.buzzdynegamingteam.pricetrend.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.CommonRepository
import com.buzzdynegamingteam.pricetrend.common.models.Tracking
import com.buzzdynegamingteam.pricetrend.common.models.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.text.NumberFormat
import java.util.*

class HomeViewModel : ViewModel() {
    private val TAG = "HomeViewModel"
    private val repo = CommonRepository

    private val _user = MutableLiveData<User>()
    val user : LiveData<User>
        get() = _user

    //https://stackoverflow.com/questions/63512783/data-binding-how-to-solve-could-not-find-accessor-issue
    private val _totalSaving = MutableLiveData<String>()
    val getTotalSaving : LiveData<String>
        get() = _totalSaving

    private val _trxCount = MutableLiveData<String>()
    val getTrxCount : LiveData<String>
        get() = _trxCount

    private val _trackingList = MutableLiveData<List<Tracking>>()
    val getTrackingList : LiveData<List<Tracking>>
        get() = _trackingList

    private val _currUser = MutableLiveData<FirebaseUser?>()
    val getCurrUser: LiveData<FirebaseUser?>
        get() = _currUser

    var intentConsumed = false

    init {
        Log.e("HomeViewModel", "repo.getCurrUser: ${repo.getCurrUser()}")
        _currUser.value = repo.getCurrUser()

        if(_currUser.value != null) {
            viewModelScope.launch {
                _user.value         = repo.getUserData()
                _totalSaving.value  = getUserTotalSaving()
                _trxCount.value     = getTrxCount()
                _trackingList.value = repo.getListOfUserTrackings().toList()

                val oldFcmToken     = _user.value?.fcmToken
                val newFcmToken     = repo.getFCMToken()

                Log.e(TAG, "Saved FCM Token: $oldFcmToken")

                if(!(oldFcmToken.equals(newFcmToken))) {
                    Log.e(TAG, "found new token ($newFcmToken)")
                    repo.updateUserFCMToken(newFcmToken)
                }
            }
        }
    }

    private fun getUserTotalSaving(): String{
        val tSaving = _user.value?.totalSaving
        val format = NumberFormat.getCurrencyInstance()
        return try {
            format.currency = Currency.getInstance("IDR")
            format.format(tSaving)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "getUserTotalSaving: tSaving is null", e)
            "----"
        } catch (e: Exception) {
            Log.e(TAG, "getUserTotalSaving: Unknown Error", e)
            "ERROR"
        }
    }

    private fun getTrxCount(): String{
        return try {
            _user.value?.trxCount.toString()
        } catch (e: Exception) {
            Log.e(TAG, "getTrxCount: unkown error", e)
            "x"
        }
    }
}