package com.buzzdynegamingteam.pricetrend.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzzdynegamingteam.pricetrend.common.CommonRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val TAG = "ProfileViewModel"
    private val repo = CommonRepository

    private val _eventNavigateToLogin = MutableLiveData<Boolean>()
    val getEventNavigateToLogin : LiveData<Boolean>
        get() = _eventNavigateToLogin

    fun signOut() {
        viewModelScope.launch {
            repo.signOut()
            _eventNavigateToLogin.value = true
        }
    }

    fun onNavigateToLoginFinished() {
        _eventNavigateToLogin.value = false
    }
}