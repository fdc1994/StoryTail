package com.fabiotiago.storytail.app.ui.home

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabiotiago.storytail.app.Manager
import com.fabiotiago.storytail.domain.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val booksRepository: BooksRepository
) : ViewModel() {
    private val _viewState: MutableSharedFlow<HomeViewState> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val viewState = _viewState.asSharedFlow()

    fun init() {
        viewModelScope.launch{
            val books = booksRepository.getBooks()
            if (books != null) {
                _viewState.emit(HomeViewState.ContentLoaded(books))
            } else {
                _viewState.emit(HomeViewState.Error)
            }
        }
    }


    sealed class HomeViewState {
        data class ContentLoaded(
            val books :List<Book>
        ) : HomeViewState()
        data object Loading : HomeViewState()
        data object Error : HomeViewState()
    }
}