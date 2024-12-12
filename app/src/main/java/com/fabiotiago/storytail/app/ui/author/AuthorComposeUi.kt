package com.fabiotiago.storytail.app.ui.author
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fabiotiago.storytail.domain.repository.Author


object AuthorComposeUi {

    @Composable
    fun AuthorDetailsScreen(
        viewModel: AuthorViewModel,
        authorId: Int
    ) {
        val viewState by viewModel.viewState.collectAsState(initial = AuthorViewModel.AuthorViewState.Loading)

        when (viewState) {
            is AuthorViewModel.AuthorViewState.ContentLoaded -> {
                val author = (viewState as AuthorViewModel.AuthorViewState.ContentLoaded).author
                if (author != null) {
                    AuthorDetailsView(author = author)
                } else ErrorView()
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
    fun AuthorDetailsView(author: Author) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "${author.firstName} ${author.lastName}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Description: ${author.description ?: "No description available"}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Nationality: ${author.nationality ?: "Unknown"}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }

    @Composable
    fun LoadingView() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    @Composable
    fun ErrorView() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error loading author details", color = Color.Red)
        }
    }


}