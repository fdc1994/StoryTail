package com.fabiotiago.storytail.app.ui.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabiotiago.storytail.domain.repository.Book
import com.fabiotiago.storytail.domain.repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl
): ViewModel() {

    fun rateBook(rating: Int, book: Book) {
        viewModelScope.launch {
            userRepositoryImpl.updateUserRating(rating, book.id)
        }

    }
}