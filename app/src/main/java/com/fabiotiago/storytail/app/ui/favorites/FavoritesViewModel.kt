package com.fabiotiago.storytail.app.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabiotiago.storytail.domain.managers.UserAuthenticationManager
import com.fabiotiago.storytail.domain.repository.Book
import com.fabiotiago.storytail.domain.repository.FavouritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {

    private val _viewState: MutableSharedFlow<FavoritesViewState> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val viewState = _viewState.asSharedFlow()

    fun init() {
        getBooks()
    }

    private fun getBooks() {
        viewModelScope.launch {
            val books = favouritesRepository.getFavourites()
            if (books != null) {
                if (books.isEmpty()) {
                    _viewState.emit(FavoritesViewState.Empty)
                } else {
                    _viewState.emit(FavoritesViewState.ContentLoaded(books))
                }
            } else {
                _viewState.emit(FavoritesViewState.Error)
            }
        }
    }

    fun addOrRemoveFavourite(isFavourite: Boolean, bookId: Int) {
        viewModelScope.launch{
            if(isFavourite) {
                favouritesRepository.removeFavourite(
                    userId = UserAuthenticationManager.user?.id ?: 0,
                    bookId = bookId
                )
            } else {
                favouritesRepository.addFavourite(
                    userId = UserAuthenticationManager.user?.id ?: 0,
                    bookId = bookId
                )
            }
            getBooks()
        }
    }

    sealed class FavoritesViewState {
        data class ContentLoaded(
            val books :List<Book>
        ) : FavoritesViewState()
        data object Loading : FavoritesViewState()
        data object Empty : FavoritesViewState()
        data object Error : FavoritesViewState()
    }
}