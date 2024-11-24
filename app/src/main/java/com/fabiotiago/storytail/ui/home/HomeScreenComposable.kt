package com.fabiotiago.storytail.ui.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.fabiotiago.storytail.R
import com.fabiotiago.storytail.ui.home.HomeScreenComposable.HomeScreen

object HomeScreenComposable {

    @Composable
    fun HomeScreen(books: List<Book>, onCtaClick: () -> Unit) {
        val sections = listOf("Books", "Suggestions", "Spotlight", "More Suggestions") // Add a placeholder for Spotlight

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // Promotion Banner
                item {
                    PromotionBanner(
                        modifier = Modifier.fillMaxWidth(),
                        title = "Log in now!",
                        subtitle = "Logged in Users can see a lot more books and get premium!",
                        ctaText = "Go to login",
                        iconRes = R.drawable.story_tail_logo
                    ) {
                        onCtaClick.invoke()
                    }
                }

                // Dynamically mix Spotlight and Carousels
                itemsIndexed(sections) { _, title ->
                    when (title) {
                        "Spotlight" -> SpotlightSection(books.take(2)) // Show Spotlight section
                        else -> BookCarousel(books, title, onCtaClick)            // Show BookCarousel for other sections
                    }
                }
            }
        }
    }


    @Composable
    fun BookCarousel(books: List<Book>, title: String, onCtaClick: () -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 16.dp)
                .background(
                    color = colorResource(R.color.orange_secondary),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = colorResource(R.color.white)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(books.size) { index ->
                        BookCard(book = books[index], onCtaClick = onCtaClick)
                    }
                }
            }
        }
    }

    @Composable
    fun BookCard(book: Book, modifier: Modifier? = null, onCtaClick: () -> Unit) {
        Card(
            modifier = modifier ?: Modifier
                .width(160.dp)
                .height(250.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                AsyncImage(
                    model = book.imageUrl,
                    contentDescription = book.title,
                    modifier = Modifier
                        .fillMaxWidth().weight(2f),
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(id = R.drawable.story_tail_logo),
                    error = painterResource(id = R.drawable.story_tail_logo)
                )
                // Book Title
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp).weight(1f),
                    maxLines = 2
                )
                // Button
                Button(
                    onClick = { onCtaClick.invoke() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
                ) {
                    val text = if (book.isLocked) "PREVIEW" else "READ"
                    Text(text)
                }
            }
        }
    }

    @Composable
    fun PromotionBanner(
        modifier: Modifier = Modifier,
        title: String,
        subtitle: String,
        ctaText: String,
        iconRes: Int,
        onCtaClick: () -> Unit
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.orange_secondary))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Text Content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = colorResource(R.color.white)
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.white).copy(alpha = 0.8f)
                    )
                    // CTA Button
                    Button(
                        onClick = onCtaClick,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
                    ) {
                        Text(text = ctaText)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                // Icon
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            color = colorResource(R.color.white),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(8.dp),
                    tint = colorResource(R.color.orange_secondary)
                )
            }
        }
    }

    @Composable
    fun SpotlightSection(books: List<Book>) {
        if (books.size < 2) return // Ensure at least two books are available

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    colorResource(R.color.orange_secondary),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            // Section Title
            Text(
                text = "Today's Spotlight",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)
            )

            // Divider
            HorizontalDivider(
                color = MaterialTheme.colorScheme.primaryContainer,
                thickness = 2.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Section Title
            Text(
                text = "Immerse yourself in handpicked stories that captivate the imagination. Whether you're seeking thrilling adventures, heartfelt romances, or profound life lessons, our spotlight selection brings the best reads right to your fingertips.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)
            )

            // Spotlight Row
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .width(160.dp)
                    .height(250.dp)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // First Book Spotlight
                BookCard(book = books[0], modifier = Modifier.weight(1f), {})

                // Second Book Spotlight
                BookCard(book = books[1], modifier = Modifier.weight(1f), {})
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
        Book("Charlotte's Web", "https://example.com/charlottes_web.jpg", true),
        Book("The Gruffalo", "https://example.com/the_gruffalo.jpg", true),
        Book("Flynn's Perfect Pet", "https://example.com/flynns_perfect_pet.jpg", false),
        Book("Freddie and the Fairy", "https://example.com/freddie_and_the_fairy.jpg", false)
    )
    HomeScreen(books = books, {})
}