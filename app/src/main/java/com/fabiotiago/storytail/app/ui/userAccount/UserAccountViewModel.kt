package com.fabiotiago.storytail.app.ui.userAccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabiotiago.storytail.domain.managers.UserAuthenticationManager
import com.fabiotiago.storytail.domain.repository.Book
import com.fabiotiago.storytail.domain.repository.BooksRepository
import com.fabiotiago.storytail.domain.repository.FavouritesRepository
import com.fabiotiago.storytail.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val booksRepository: BooksRepository,
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {

    private val _viewState: MutableSharedFlow<UserAccountViewState> = MutableSharedFlow(
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
            userRepository.authenticateUser(username, password) { success, message ->
                launch {
                    if (success) {
                        _viewState.emit(
                            UserAccountViewState.LoginSuccess(
                                username,
                                filteredBooks(booksRepository.getBooks()),
                                favouritesRepository.getFavourites(),

                            )
                        )
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
                user = null
            }
            _viewState.emit(UserAccountViewState.Logout)
        }
    }

    fun becomePremium(userId: Int) {
        viewModelScope.launch {
            _viewState.emit(UserAccountViewState.Loading)
            userRepository.upgradeUser(userId) { success, message ->
                launch {
                    if (success) {
                        getLoggedInScreen()
                    } else {
                        _viewState.emit(UserAccountViewState.LoginError(message))
                    }
                }
            }
        }
    }

    private suspend fun getLoggedInScreen() {
        _viewState.emit(
            UserAccountViewState.LoginSuccess(
                UserAuthenticationManager.user?.userName ?: "",
                booksRepository.getBooks(),
                favouritesRepository.getFavourites()
            )
        )
    }

    private fun filteredBooks(books: List<Book>?): List<Book>? {
        return books?.filter { it.progress > 0 }
    }

    fun addOrRemoveFavourite(isFavourite: Boolean, bookId: Int) {
        viewModelScope.launch{
            if(isFavourite) {
                favouritesRepository.removeFavourite(
                    userId = 1,
                    bookId = bookId
                )
            } else {
                favouritesRepository.addFavourite(
                    userId = 1,
                    bookId = bookId
                )
            }
            getLoggedInScreen()
        }
    }
}

sealed class UserAccountViewState {
    object Loading : UserAccountViewState()
    data class LoginSuccess(
        val username: String,
        val books: List<Book>?,
        val favourites: List<Book>?
    ) : UserAccountViewState()
    data class LoginError(val message: String) : UserAccountViewState()
    object Logout : UserAccountViewState()
}