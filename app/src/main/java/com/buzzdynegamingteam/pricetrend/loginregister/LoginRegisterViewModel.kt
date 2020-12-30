package com.buzzdynegamingteam.pricetrend.loginregister

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginRegisterViewModel : ViewModel(){

    private val repo = LoginRegisterRepository

    private val _currUser = MutableLiveData<FirebaseUser?>()
    val getCurrUser: LiveData<FirebaseUser?>
        get() = _currUser

    init {
        updateCurrUser()
    }

    fun initCurrUserData(uid: String) {
        runBlocking {
            repo.initCurrUserData(uid)
        }
    }

    fun updateCurrUser() {
        _currUser.value = repo.getCurrUser()
    }
}