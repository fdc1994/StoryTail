package com.fabiotiago.storytail.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabiotiago.storytail.domain.managers.UserAuthenticationManager
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
class HomeViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {

    private val _viewState: MutableSharedFlow<HomeViewState> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val viewState = _viewState.asSharedFlow()

    private var cachedBooks: List<Book> = emptyList()
    private var ageGroup: AgeGroup = AgeGroup.ALL
    private var searchTerm: String = ""

    fun init() {
        fetchBooks()
    }

    private fun fetchBooks() {
        viewModelScope.launch {
            val books = booksRepository.getBooks()

            if (books != null) {
                cachedBooks = books // Cache the books
                emitFilteredBooks()
            } else {
                _viewState.emit(HomeViewState.Error)
            }
        }
    }

    private fun emitFilteredBooks() {
        val filteredBooks = cachedBooks.filter()
        viewModelScope.launch {
            _viewState.emit(
                HomeViewState.ContentLoaded(
                    filteredBooks,
                    booksRepository.getPopularBooks(), // Update if you need filtered popularBooks
                    favouritesRepository.getFavourites() // Update favourites if needed
                )
            )
        }
    }

    fun filter(ageGroup: AgeGroup, search: String) {
        this.ageGroup = ageGroup
        this.searchTerm = search
        emitFilteredBooks()
    }

    fun addOrRemoveFavourite(isFavourite: Boolean, bookId: Int) {
        viewModelScope.launch {
            if (isFavourite) {
                favouritesRepository.removeFavourite(
                    userId = UserAuthenticationManager.user?.id ?: 0,
                    bookId = bookId
                )
            } else {
                favouritesRepository.addFavourite(userId = UserAuthenticationManager.user?.id ?: 0, bookId = bookId)
            }
            emitFilteredBooks() // Update favourites without re-fetching books
        }
    }

    private fun List<Book>.filter(): List<Book> {
        return this.filter { book ->
            val matchesAgeGroup = ageGroup == AgeGroup.ALL || book.ageGroup == ageGroup.ordinal
            val matchesSearchTerm = searchTerm.isEmpty() || book.title.contains(searchTerm, ignoreCase = true)
            matchesAgeGroup && matchesSearchTerm
        }
    }

    sealed class HomeViewState {
        data class ContentLoaded(
            val books: List<Book>,
            val popularBooks: List<Book>?,
            val favourites: List<Book>?
        ) : HomeViewState()
        data object Loading : HomeViewState()
        data object Error : HomeViewState()
    }
}

enum class AgeGroup(val displayName: String) {
    ALL("All Ages"),
    KIDS("Kids"),
    TEENS("Teens"),
    ADULTS("Adults")
}