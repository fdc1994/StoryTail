package com.fabiotiago.storytail.app.ui.book

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fabiotiago.storytail.R
import com.fabiotiago.storytail.domain.repository.Book

object BookComposeUi {
    @Composable
    fun BookProductPage(
        viewModel: BookViewModel,
        book: Book,
        onActionClick: () -> Unit,
        onAuthorClick: (Int) -> Unit
    ) {
        val context = LocalContext.current

        // Mutable state for the selected rating (if user is rating the book)
        val selectedRating = remember { mutableStateOf(book.rating ?: 0) }

        // Function to handle rating selection
        val onRatingSelected: (Int) -> Unit = { newRating ->
            if (book.canRate()) {
                selectedRating.value = newRating
                // You can call the viewModel to save the rating, e.g.:
                viewModel.rateBook(newRating, book)
            } else {
                Toast.makeText(
                    context,
                    "You cannot rate this book before finishing reading it.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                item {
                    // Book Cover
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(book.coverName)
                            .crossfade(true)
                            .build(),
                        contentDescription = book.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(id = R.drawable.story_tail_logo),
                        error = painterResource(id = R.drawable.story_tail_logo)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    // Title and Author
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "By ${book.author}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                onAuthorClick.invoke(book.id)
                            },
                        textAlign = TextAlign.Center
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    Text(
                        text = book.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Justify,
                        maxLines = 6,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    // Progress Section
                    Text(
                        text = "Reading Progress:",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Progress Bar
                    LinearProgressIndicator(
                        progress = {
                            book.progress / 100f // Ensure percentage is divided by 100
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp),
                        color = MaterialTheme.colorScheme.primary,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Percentage Text
                    Text(
                        text = "${book.progress}% complete",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    // Rating section
                    Text(
                        text = "Rate this book:",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 5-Star Rating UI
                    StarRating(
                        rating = selectedRating.value,
                        onRatingSelected = onRatingSelected
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    // Read button
                    Button(
                        onClick = { onActionClick() },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
                    ) {
                        Text(text = "READ")
                    }
                }
            }
        }
    }

    @Composable
    fun StarRating(
        rating: Int,
        onRatingSelected: (Int) -> Unit
    ) {
        // Define the star icons for empty and filled
        val filledStar = painterResource(id = R.drawable.ic_star_filled) // Replace with actual filled star icon resource
        val emptyStar = painterResource(id = R.drawable.ic_star_not_filled)   // Replace with actual empty star icon resource

        // Create a row of 5 stars
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            // Loop through 5 stars and display them as filled or empty based on the rating
            for (i in 1..5) {
                IconButton(
                    onClick = { onRatingSelected(i) }, // Set rating when the user taps on a star
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(
                        painter = if (i <= rating) filledStar else emptyStar,
                        contentDescription = "Star $i",
                        tint = if (i <= rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }

    private fun Book.canRate(): Boolean {
        return progress == 100 || rating != 0
    }
}