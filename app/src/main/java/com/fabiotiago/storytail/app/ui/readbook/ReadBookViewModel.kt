package com.fabiotiago.storytail.app.ui.readbook

import android.content.Context
import android.graphics.Bitmap
import android.os.ParcelFileDescriptor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabiotiago.storytail.domain.repository.BooksRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import android.graphics.pdf.PdfRenderer
import com.fabiotiago.storytail.domain.repository.UserRepository
import com.fabiotiago.storytail.domain.repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class ReadBookViewModel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val booksRepository: BooksRepository,
    private val userRepository: UserRepository
)  : ViewModel() {

    private var bookId = 0

    private val _viewState: MutableSharedFlow<ReadBookViewState> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val viewState = _viewState.asSharedFlow()

    fun init(bookId: Int) {
        this.bookId = bookId
        loadPdfFromUrl(bookId)
    }

    private fun loadPdfFromUrl(bookId: Int) {
        viewModelScope.launch {
            val file = booksRepository.getBookPdf(bookId, context.cacheDir)
            file?.let { _viewState.emit(ReadBookViewState.ContentLoaded(renderPdfPages(it))) }
                ?: _viewState.emit(ReadBookViewState.Error)
        }
    }

    private suspend fun renderPdfPages(file: File): List<Bitmap> {
        return withContext(Dispatchers.IO) {
            val pdfRenderer = PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY))
            val bitmaps = mutableListOf<Bitmap>()

            for (pageIndex in 0 until pdfRenderer.pageCount) {
                val page = pdfRenderer.openPage(pageIndex)
                val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                bitmaps.add(bitmap)
                page.close()
            }

            pdfRenderer.close()
            bitmaps
        }
    }

    fun updateUserProgress(progress: Int) {
        viewModelScope.launch {
            userRepository.updateUserProgress(progress, bookId)
        }
    }

    sealed class ReadBookViewState {
        data class ContentLoaded(
            val pages :List<Bitmap>
        ) : ReadBookViewState()
        data object Loading : ReadBookViewState()
        data object Error : ReadBookViewState()
    }

}