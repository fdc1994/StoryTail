package com.fabiotiago.storytail.app.ui.author

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabiotiago.storytail.domain.repository.Author
import com.fabiotiago.storytail.domain.repository.AuthorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorViewModel @Inject constructor(
    private val authorRepository: AuthorRepository
) : ViewModel() {

    private val _viewState: MutableSharedFlow<AuthorViewState> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val viewState = _viewState.asSharedFlow()

    fun init(authorId: Int) {
        viewModelScope.launch {
            authorRepository.getAuthor(authorId)?.let {
                _viewState.emit(AuthorViewState.ContentLoaded(it))
            } ?: _viewState.emit(AuthorViewState.Error)

        }
    }

    sealed class AuthorViewState {
        data class ContentLoaded(
            val author :Author?
        ) : AuthorViewState()
        data object Loading : AuthorViewState()
        data object Error : AuthorViewState()
    }
}