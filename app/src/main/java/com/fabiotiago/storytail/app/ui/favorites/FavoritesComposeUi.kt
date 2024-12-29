package com.fabiotiago.storytail.app.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fabiotiago.storytail.app.ui.GenericComponentsComposables.BookCard
import com.fabiotiago.storytail.app.ui.GenericComponentsComposables.ErrorView
import com.fabiotiago.storytail.app.ui.GenericComponentsComposables.LoadingView
import com.fabiotiago.storytail.domain.repository.Book

object FavoritesComposeUi {
    @Composable
    fun FavoritesScreen(
        viewModel: FavoritesViewModel,
        onBookClick: (book: Book) -> Unit,
    ) {
        val viewState by viewModel.viewState.collectAsState(initial = FavoritesViewModel.FavoritesViewState.Loading)
        when (viewState) {
            is FavoritesViewModel.FavoritesViewState.ContentLoaded -> {
                val content = (viewState as FavoritesViewModel.FavoritesViewState.ContentLoaded)
                FavouritesSection(
                    content.books,
                    onBookClick,
                    viewModel::addOrRemoveFavourite
                )
            }

            FavoritesViewModel.FavoritesViewState.Empty -> EmptyFavoritesView()
            FavoritesViewModel.FavoritesViewState.Error -> ErrorView()
            FavoritesViewModel.FavoritesViewState.Loading -> LoadingView()
        }

    }


    @Composable
    fun FavouritesSection(
        favourites: List<Book>,
        onBookClick: (book: Book) -> Unit,
        onFavoriteClick: (isFavourite: Boolean, bookId: Int) -> Unit
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalItemSpacing = 20.dp,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            itemsIndexed(favourites) { _, book ->
                BookCard(
                    book = book,
                    isFavourite = true,
                    onCtaClick = { onBookClick(book) },
                    onFavoriteClick = onFavoriteClick
                )
            }
        }
    }

    @Composable
    fun EmptyFavoritesView() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No Favorites yet. Start adding some!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }

}