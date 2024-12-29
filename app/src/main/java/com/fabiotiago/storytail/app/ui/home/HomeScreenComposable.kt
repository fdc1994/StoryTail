package com.fabiotiago.storytail.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fabiotiago.storytail.R
import com.fabiotiago.storytail.app.ui.GenericComponentsComposables.BookCarousel
import com.fabiotiago.storytail.app.ui.GenericComponentsComposables.ErrorView
import com.fabiotiago.storytail.app.ui.GenericComponentsComposables.LoadingView
import com.fabiotiago.storytail.app.ui.GenericComponentsComposables.PromotionBanner
import com.fabiotiago.storytail.app.ui.GenericComponentsComposables.SpotlightSection
import com.fabiotiago.storytail.domain.managers.UserAuthenticationManager
import com.fabiotiago.storytail.domain.repository.Book

object HomeScreenComposable {

    @Composable
    fun HomeScreen(
        viewModel: HomeViewModel,
        onBookClick: (book: Book) -> Unit,
        onLoginClick: () -> Unit
    ) {
        val viewState by viewModel.viewState.collectAsState(initial = HomeViewModel.HomeViewState.Loading)
        when (viewState) {
            is HomeViewModel.HomeViewState.ContentLoaded -> {
                val content = viewState as HomeViewModel.HomeViewState.ContentLoaded
                MainContent(
                    content.books,
                    content.popularBooks,
                    content.favourites,
                    onLoginClick,
                    onBookClick,
                    viewModel::addOrRemoveFavourite,
                    viewModel::filter
                )
            }

            HomeViewModel.HomeViewState.Error -> ErrorView()
            HomeViewModel.HomeViewState.Loading -> LoadingView()
        }
    }


    @Composable
    private fun MainContent(
        books: List<Book>,
        popularBooks: List<Book>?,
        favourites: List<Book>?,
        onLoginClick: () -> Unit,
        onBookClick: (book: Book) -> Unit,
        onFavoriteClick: (isFavourite: Boolean, bookId: Int) -> Unit,
        onFilter: (AgeGroup, String) -> Unit
    ) {
        val sections = listOf(
            "Books",
            "Suggestions",
            "Spotlight",
            "More Suggestions"
        )

        var expanded by rememberSaveable { mutableStateOf(false) }
        var selectedAgeGroup by rememberSaveable { mutableStateOf<AgeGroup>(AgeGroup.ALL) }
        var searchText by rememberSaveable { mutableStateOf("") }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Row for Search Bar and Dropdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Dropdown Menu for Age Group Selection
                    Box {
                        Button(onClick = { expanded = true }) {
                            Text(text = selectedAgeGroup?.displayName ?: "Select Age Group")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            AgeGroup.entries.forEach { ageGroup ->
                                DropdownMenuItem(
                                    text = { Text(ageGroup.displayName) },
                                    onClick = {
                                        selectedAgeGroup = ageGroup
                                        expanded = false
                                        onFilter(ageGroup, searchText) // Notify selection
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Search Bar
                    TextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            onFilter(selectedAgeGroup, it)
                        },
                        placeholder = { Text("Search by title...") },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    if (!UserAuthenticationManager.isUserLoggedIn) {
                        item {
                            PromotionBanner(
                                modifier = Modifier.fillMaxWidth(),
                                title = "Log in now!",
                                subtitle = "Logged in Users can see a lot more books and get premium!",
                                ctaText = "Go to login",
                                iconRes = R.drawable.story_tail_logo
                            ) {
                                onLoginClick.invoke()
                            }
                        }
                    }

                    itemsIndexed(sections) { _, title ->
                        if (title == "Spotlight") {
                            if (popularBooks != null) {
                                SpotlightSection(
                                    popularBooks.shuffled(),
                                    favourites,
                                    onFavoriteClick,
                                    onBookClick
                                )
                            }
                        } else {
                            BookCarousel(
                                books,
                                title,
                                favourites,
                                onBookClick,
                                onFavoriteClick
                            )
                        }
                    }
                }
            }
        }
    }


    // Public function that exposes MainContent for previews
    @Composable
    fun PreviewMainContent(
        books: List<Book>,
        popularBooks: List<Book>?,
        favourites: List<Book>?,
        onLoginClick: () -> Unit,
        onBookClick: (book: Book) -> Unit,
        onFavoriteClick: (isFavourite: Boolean, bookId: Int) -> Unit,
        onAgeGroupSelected: (AgeGroup, String) -> Unit
    ) {
        MainContent(
            books,
            popularBooks,
            favourites,
            onLoginClick,
            onBookClick,
            onFavoriteClick,
            onAgeGroupSelected
        )
    }
}