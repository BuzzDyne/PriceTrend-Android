package com.buzzdynegamingteam.pricetrend.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.CommonRepository
import com.buzzdynegamingteam.pricetrend.common.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.text.NumberFormat
import java.util.*

class ProfileViewModel : ViewModel() {
    private val TAG = "ProfileViewModel"
    private val repo = CommonRepository

    private val _eventNavigateToLogin = MutableLiveData<Boolean>()
    val getEventNavigateToLogin : LiveData<Boolean>
        get() = _eventNavigateToLogin

    private val _user = MutableLiveData<User>()
    val getUser : LiveData<User>
        get() = _user

    init {
        viewModelScope.launch {
            _user.value     = repo.getUserData()
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repo.signOut()
            _eventNavigateToLogin.value = true
        }
    }

    fun onNavigateToLoginFinished() {
        _eventNavigateToLogin.value = false
    }

    fun getUserTotalSaving(): String{
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
}