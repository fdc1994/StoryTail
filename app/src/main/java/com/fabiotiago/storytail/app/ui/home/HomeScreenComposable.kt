package com.fabiotiago.storytail.app.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fabiotiago.storytail.R
import com.fabiotiago.storytail.app.ui.favorites.FavoritesViewModel
import com.fabiotiago.storytail.app.ui.home.HomeViewModel.HomeViewState.Loading
import com.fabiotiago.storytail.domain.managers.UserAuthenticationManager
import com.google.gson.annotations.SerializedName
import java.io.Serializable

object HomeScreenComposable {

    @Composable
    fun HomeScreen(
        viewModel: HomeViewModel,
        onBookClick: (book: Book) -> Unit,
        onLoginClick: () -> Unit
    ) {
        val viewState by viewModel.viewState.collectAsState(initial = Loading)
        when (viewState) {
            is HomeViewModel.HomeViewState.ContentLoaded -> {
                val content = (viewState as HomeViewModel.HomeViewState.ContentLoaded)
                MainContent(
                    content.books,
                    content.popularBooks,
                    content.favourites,
                    onLoginClick,
                    onBookClick,
                    viewModel::addOrRemoveFavourite
                )
            }

            HomeViewModel.HomeViewState.Error -> ErrorView()
            Loading -> LoadingView()
        }

    }

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
    fun ErrorView(
        modifier: Modifier = Modifier,
        errorMessage: String = "An error occurred. Please try again.",
        onRetryClick: (() -> Unit)? = null,
        textStyle: TextStyle = MaterialTheme.typography.bodySmall,
        textColor: Color = MaterialTheme.colorScheme.onBackground,
        buttonText: String = "Retry",
        buttonStyle: ButtonColors = ButtonDefaults.buttonColors()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = errorMessage,
                    style = textStyle,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                onRetryClick?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = it,
                        colors = buttonStyle
                    ) {
                        Text(text = buttonText)
                    }
                }
            }
        }
    }


    @Composable
    private fun MainContent(
        books: List<Book>,
        popularBooks: List<Book>?,
        favourites: List<Book>?,
        onLoginClick: () -> Unit,
        onBookClick: (book: Book) -> Unit,
        onFavoriteClick: (isFavourite: Boolean, bookId: Int) -> Unit
    ) {
        val sections = listOf(
            "Books",
            "Suggestions",
            "Spotlight",
            "More Suggestions"
        ) // Add a placeholder for Spotlight

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

                // Dynamically mix Spotlight and Carousels
                itemsIndexed(sections) { _, title ->
                    if(title == "Spotlight") {
                        SpotlightSection(books.shuffled(), favourites, onFavoriteClick, onBookClick)
                    } else {
                        BookCarousel(
                            books.shuffled(),
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

    @Composable
    fun LoadingView(
        modifier: Modifier = Modifier,
        loadingText: String? = null,
        textStyle: TextStyle = MaterialTheme.typography.bodySmall,
        textColor: Color = MaterialTheme.colorScheme.onBackground,
        indicatorColor: Color = MaterialTheme.colorScheme.primary,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = indicatorColor)
                loadingText?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = it, style = textStyle, color = textColor)
                }
            }
        }
    }


    @Composable
    fun BookCarousel(
        books: List<Book>,
        title: String,
        favourites: List<Book>?,
        onBookClick: (book: Book) -> Unit,
        onFavoriteClick: (isFavourite: Boolean, bookId: Int) -> Unit
    ) {
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
                        val book = books[index]
                        BookCard(
                            book = book,
                            isFavourite = favourites?.any { it.id == book.id } ?: false ,
                            onCtaClick = onBookClick,
                            onFavoriteClick = onFavoriteClick
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun BookCard(
        book: Book,
        isFavourite: Boolean,
        modifier: Modifier? = null,
        onFavoriteClick: (isFavourite: Boolean, bookId: Int) -> Unit,
        onCtaClick: (book: Book) -> Unit
    ) {
        val favouriteIcon =
            if (isFavourite) R.drawable.ic_star_filled else R.drawable.ic_star_not_filled

        Card(
            modifier = modifier ?: Modifier
                .width(200.dp)
                .height(380.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Image with Favorites Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(3f) // Adjust the weight to give more space to the image
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(book.coverName)
                            .crossfade(true)
                            .build(),
                        contentDescription = book.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds,
                        error = painterResource(id = R.drawable.story_tail_logo)
                    )

                    Image(
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .clickable {
                                onFavoriteClick.invoke(isFavourite, book.id)
                            },
                        painter = painterResource(favouriteIcon),
                        contentDescription = "Favourite Icon",
                    )
                }

                // Title
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .weight(1f), // Adjust the weight to fit the title proportionally
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // CTA Button
                Button(
                    onClick = { onCtaClick(book) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .weight(1f), // Adjust the weight to fit the button proportionally
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
                ) {
                    val text = if (book.accessLevel > UserAuthenticationManager.userAccessLevel) "GET PREMIUM" else "READ"
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
        onLoginClick: () -> Unit
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
                        onClick = { onLoginClick.invoke() },
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
    fun SpotlightSection(
        books: List<Book>,
        favourites: List<Book>?,
        onFavoriteClick: (isFavourite: Boolean, bookId: Int) -> Unit,
        onBookClick: (book: Book) -> Unit
    ) {
        if (books.size < 2) return // Ensure at least two books are available

        Column(
            modifier = Modifier
                .fillMaxSize()
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
                modifier = Modifier.height(350.dp).padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // First Book Spotlight
                BookCard(
                    book = books[0],
                    isFavourite = favourites?.contains(books[0]) == true,
                    modifier = Modifier.weight(1f),
                    onFavoriteClick = onFavoriteClick,
                    onBookClick
                )

                BookCard(
                    book = books[1],
                    isFavourite = favourites?.contains(books[0]) == true,
                    modifier = Modifier.weight(1f),
                    onFavoriteClick = onFavoriteClick,
                    onBookClick
                )
            }
        }
    }

    @Composable
    fun FavouritesSection(
        favourites: List<Book>,
        onBookClick: (book: Book) -> Unit,
        onFavoriteClick: (isFavourite: Boolean, bookId: Int) -> Unit
    ) {
        LazyVerticalStaggeredGrid (
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

    // Public function that exposes MainContent for previews
    @Composable
    fun PreviewMainContent(
        books: List<Book>,
        popularBooks: List<Book>?,
        favourites: List<Book>?,
        onLoginClick: () -> Unit,
        onBookClick: (book: Book) -> Unit,
        onFavoriteClick: (isFavourite: Boolean, bookId: Int) -> Unit
    ) {
        MainContent(books, popularBooks, favourites, onLoginClick, onBookClick, onFavoriteClick)
    }
}


data class Book(
    @SerializedName("book_id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("cover_name")
    val coverName: String?,

    @SerializedName("read_time")
    val readTime: Int,

    @SerializedName("age_group")
    val ageGroup: Int,

    @SerializedName("access_level")
    val accessLevel: Int,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("authors")
    val author: String? = "Unknown"
) : Serializable

data class BooksResponse(
    @SerializedName("books")
    val books: List<Book>
) : Serializable

data class FavouritesResponse(
    @SerializedName("favorites")
    val books: List<Book>
) : Serializable

data class ReadBookResponse(
    val books: List<ReadBook>
)

data class ReadBook(
    @SerializedName("book_id")
    val bookId: Int,

    @SerializedName("book_pdf_url")
    val bookPdfUrl: String,

    @SerializedName("audio_url")
    val audioUrl: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("page_index")
    val pageIndex: Int,

    @SerializedName("updated_at")
    val updatedAt: String
)
