package com.fabiotiago.storytail.app.ui.author

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fabiotiago.storytail.app.ui.GenericComponentsComposables.BookCarousel
import com.fabiotiago.storytail.app.ui.GenericComponentsComposables.ErrorView
import com.fabiotiago.storytail.app.ui.GenericComponentsComposables.LoadingView
import com.fabiotiago.storytail.domain.repository.Author
import com.fabiotiago.storytail.domain.repository.Book


object AuthorComposeUi {

    @Composable
    fun AuthorDetailsScreen(
        viewModel: AuthorViewModel,
        authorId: Int,
        onBookClick: (book: Book) -> Unit
    ) {
        val viewState by viewModel.viewState.collectAsState(initial = AuthorViewModel.AuthorViewState.Loading)

        when (viewState) {
            is AuthorViewModel.AuthorViewState.ContentLoaded -> {
                val state = (viewState as AuthorViewModel.AuthorViewState.ContentLoaded)
                state.author?.let {
                    AuthorDetailsView(
                        author = it,
                        books = state.authorBooks,
                        favorites = state.favorites,
                        onBookClick = onBookClick,
                        onFavoritesClick = viewModel::addOrRemoveFavourite
                    )
                } ?: ErrorView()
            }

            AuthorViewModel.AuthorViewState.Error -> ErrorView()
            AuthorViewModel.AuthorViewState.Loading -> LoadingView()
        }

        // Fetch author data when the screen is loaded
        LaunchedEffect(authorId) {
            viewModel.init(authorId)
        }
    }

    @Composable
    fun AuthorDetailsView(
        author: Author,
        books: List<Book>? = emptyList(),
        favorites: List<Book>? = emptyList(),
        onBookClick: (book: Book) -> Unit,
        onFavoritesClick: (isFavourite: Boolean, bookId: Int) -> Unit
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Author Section
            item {
                // Author Info Container with Card for elevated effect
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        // Author's Name
                        Text(
                            text = "${author.firstName} ${author.lastName}",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // Author's Description
                        Text(
                            text = author.description ?: "No description available",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // Author's Nationality
                        Text(
                            text = "Nationality: ${author.nationality ?: "Unknown"}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Books Section
            item {
                if (books != null) {
                    BookCarousel(
                        books,
                        "Author Related Books",
                        favourites = favorites,
                        onBookClick = onBookClick,
                        onFavoriteClick = onFavoritesClick
                    )
                }
            }
        }
    }
}