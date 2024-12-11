package com.fabiotiago.storytail.app.ui.userAccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabiotiago.storytail.domain.managers.UserAuthenticationManager
import com.fabiotiago.storytail.domain.repository.UserAuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val userAuthenticationRepository: UserAuthenticationRepository
) : ViewModel() {

    private val _viewState : MutableSharedFlow<UserAccountViewState> = MutableSharedFlow(
            replay = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val viewState: SharedFlow<UserAccountViewState> = _viewState

    init {
        viewModelScope.launch {
            _viewState.emit(UserAccountViewState.Logout)
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _viewState.emit(UserAccountViewState.Loading)
            userAuthenticationRepository.authenticateUser(username, password) { success, message ->
                launch {
                    if (success) {
                        _viewState.emit(UserAccountViewState.LoginSuccess(username))
                    } else {
                        _viewState.emit(UserAccountViewState.LoginError(message))
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            UserAuthenticationManager.apply {
                isUserLoggedIn = false
                userAccessLevel = 1
            }
            _viewState.emit(UserAccountViewState.Logout)
        }
    }
}

sealed class UserAccountViewState {
    object Loading : UserAccountViewState()
    data class LoginSuccess(val username: String) : UserAccountViewState()
    data class LoginError(val message: String) : UserAccountViewState()
    object Logout : UserAccountViewState()
}