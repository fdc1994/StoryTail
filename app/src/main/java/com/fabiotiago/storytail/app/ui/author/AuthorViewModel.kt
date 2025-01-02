package com.fabiotiago.storytail.app.ui.author

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabiotiago.storytail.domain.managers.UserAuthenticationManager
import com.fabiotiago.storytail.domain.repository.Author
import com.fabiotiago.storytail.domain.repository.AuthorRepository
import com.fabiotiago.storytail.domain.repository.Book
import com.fabiotiago.storytail.domain.repository.BooksRepository
import com.fabiotiago.storytail.domain.repository.FavouritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorViewModel @Inject constructor(
    private val authorRepository: AuthorRepository,
    private val favouritesRepository: FavouritesRepository,
    private val bookRepository: BooksRepository
) : ViewModel() {

    private val _viewState: MutableSharedFlow<AuthorViewState> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val viewState = _viewState.asSharedFlow()

    private var cachedBooks: List<Book>? = null
    private lateinit var cachedAuthor: Author

    fun init(authorId: Int) {
        viewModelScope.launch {
            cachedBooks = bookRepository.getAuthorBooks(authorId)
            authorRepository.getAuthor(authorId)?.let {
                cachedAuthor = it
                emitFilteredBooks()
            } ?: _viewState.emit(AuthorViewState.Error)
        }
    }


    fun addOrRemoveFavourite(isFavourite: Boolean, bookId: Int) {
        viewModelScope.launch {
            if (isFavourite) {
                favouritesRepository.removeFavourite(userId = UserAuthenticationManager.user?.id ?: 0, bookId = bookId)
            } else {
                favouritesRepository.addFavourite(userId = UserAuthenticationManager.user?.id ?: 0, bookId = bookId)
            }
            emitFilteredBooks()
        }
    }

    private fun emitFilteredBooks() {
        viewModelScope.launch {
            _viewState.emit(
                AuthorViewState.ContentLoaded(
                    cachedAuthor,
                    cachedBooks,
                    favouritesRepository.getFavourites() // Update favourites if needed
                )
            )
        }
    }
    sealed class AuthorViewState {
        data class ContentLoaded(
            val author :Author?,
            val authorBooks: List<Book>?,
            val favorites: List<Book>?,
        ) : AuthorViewState()
        data object Loading : AuthorViewState()
        data object Error : AuthorViewState()
    }
}