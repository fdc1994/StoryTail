package com.fabiotiago.storytail.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.fabiotiago.storytail.R
import com.fabiotiago.storytail.ui.home.HomeScreenComposable.BookCarousel

object HomeScreenComposable {

    @Composable
    fun BookCarousel(books: List<Book>) {
        // UI Layout
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(books.size) { index ->
                    BookCard(book = books[index])
                }
            }
        }
    }

    @Composable
    fun BookCard(book: Book) {
        Card(
            modifier = Modifier
                .width(160.dp)
                .height(250.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Book Image with fallback and placeholder
                AsyncImage(
                    model = book.imageUrl,
                    contentDescription = book.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.story_tail_logo),
                    error = painterResource(id = R.drawable.story_tail_logo)
                )
                // Book Title
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp),
                    maxLines = 2
                )
                // Button
                Button(
                    onClick = { /* Handle click */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
                ) {
                    Text(text = if (book.isLocked) "PREVIEW" else "READ")
                }
            }
        }
    }
}

// Data class for book
data class Book(
    val title: String,
    val imageUrl: String,
    val isLocked: Boolean
)

// Preview
@Preview(showBackground = true)
@Composable
fun BookCarouselPreview() {
    val books = listOf(
        Book("Charlotte's Web", "https://example.com/charlottes_web.jpg", false),
        Book("The Gruffalo", "https://example.com/the_gruffalo.jpg", true),
        Book("Flynn's Perfect Pet", "https://example.com/flynns_perfect_pet.jpg", false),
        Book("Freddie and the Fairy", "https://example.com/freddie_and_the_fairy.jpg", false)
    )
    BookCarousel(books = books)
}