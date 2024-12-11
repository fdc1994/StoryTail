package com.fabiotiago.storytail.app.ui.readbook

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.fabiotiago.storytail.app.ui.home.HomeScreenComposable.ErrorView


object ReadBookComposeUi {
    @Composable
    fun PdfViewerScreen(viewModel: ReadBookViewModel) {
        val viewState by viewModel.viewState.collectAsState(initial = ReadBookViewModel.ReadBookViewState.Loading)
        when (viewState) {
            is ReadBookViewModel.ReadBookViewState.ContentLoaded -> {
                val content = (viewState as ReadBookViewModel.ReadBookViewState.ContentLoaded)
                PdfPagesList(content.pages)
            }
            ReadBookViewModel.ReadBookViewState.Error -> ErrorView()
            ReadBookViewModel.ReadBookViewState.Loading -> LoadingView()
        }
    }

    @Composable
    fun LoadingView() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    fun PdfPagesList(pages: List<Bitmap>) {
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