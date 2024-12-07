package com.fabiotiago.storytail.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabiotiago.storytail.domain.repository.BooksRepository
import com.fabiotiago.storytail.domain.repository.FavouritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {
    private val _viewState: MutableSharedFlow<HomeViewState> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val viewState = _viewState.asSharedFlow()

    fun init() {
        getBooks()
    }

    private fun getBooks() {
        viewModelScope.launch {
            val books = booksRepository.getBooks()
            val popularBooks = booksRepository.getPopularBooks()
            val favourites = favouritesRepository.getFavourites()
            if (books != null) {
                _viewState.emit(HomeViewState.ContentLoaded(books, popularBooks, favourites))
            } else {
                _viewState.emit(HomeViewState.Error)
            }
        }
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
            getBooks()
        }
    }


    sealed class HomeViewState {
        data class ContentLoaded(
            val books :List<Book>,
            val popularBooks :List<Book>?,
            val favourites: List<Book>?
        ) : HomeViewState()
        data object Loading : HomeViewState()
        data object Error : HomeViewState()
    }
}