package com.fabiotiago.storytail.ui.userAccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserAccountViewModel : ViewModel() {

    private val _userState = MutableStateFlow(UserAccountState())
    val userState: StateFlow<UserAccountState> = _userState

    fun onLogin(email: String, password:String) {
        _userState.value = _userState.value.copy(isLoggedIn = true, email = email)
    }

    fun onLogout() {
        _userState.value = _userState.value.copy(isLoggedIn = false, email = "")
    }
}

data class UserAccountState(
    val isLoggedIn: Boolean = false,
    val email: String = ""
)