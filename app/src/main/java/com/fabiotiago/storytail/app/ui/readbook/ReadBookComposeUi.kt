package com.fabiotiago.storytail.app.ui.readbook

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import com.fabiotiago.storytail.app.ui.GenericComponentsComposables.ErrorView
import com.fabiotiago.storytail.app.ui.GenericComponentsComposables.LoadingView


object ReadBookComposeUi {
    @Composable
    fun PdfViewerScreen(viewModel: ReadBookViewModel) {
        val viewState by viewModel.viewState.collectAsState(initial = ReadBookViewModel.ReadBookViewState.Loading)
        when (viewState) {
            is ReadBookViewModel.ReadBookViewState.ContentLoaded -> {
                val content = (viewState as ReadBookViewModel.ReadBookViewState.ContentLoaded)
                PdfPagesList(content.pages, viewModel::updateUserProgress)
            }
            ReadBookViewModel.ReadBookViewState.Error -> ErrorView()
            ReadBookViewModel.ReadBookViewState.Loading -> LoadingView()
        }
    }

    @Composable
    fun PdfPagesList(pages: List<Bitmap>, onProgressChange: (Int) -> Unit) {
        val pagerState = rememberPagerState(
            initialPageOffsetFraction = 0F,
            pageCount = { pages.size }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.align(Alignment.TopCenter),
            ) { pageIndex ->
                val page = pages[pageIndex]
                val progress = (((pageIndex + 1).toFloat() / pages.size) * 100).toInt()
                onProgressChange.invoke(progress)
                Image(
                    bitmap = page.asImageBitmap(),
                    contentDescription = "PDF Page",
                    modifier = Modifier
                        .fillMaxSize()

                )
            }
        }

    }

}