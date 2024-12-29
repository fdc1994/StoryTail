package com.fabiotiago.storytail.domain.repository

import com.fabiotiago.storytail.data.interfaces.StoryTailService
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.Serializable
import javax.inject.Inject

interface BooksRepository {
    suspend fun getBooks(): List<Book>?
    suspend fun getPopularBooks(): List<Book>?
    suspend fun getAuthorBooks(authorId: Int): List<Book>?
    suspend fun getBookPdf(bookId: Int, cacheDir: File): File?
}

class BooksRepositoryImpl @Inject constructor(
    private val storyTailService: StoryTailService
) : BooksRepository {
    override suspend fun getBooks(): List<Book>? {
        return withContext(Dispatchers.IO) {
            try {
                storyTailService.books().books
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun getPopularBooks(): List<Book>? {
        return withContext(Dispatchers.IO) {
            try {
                storyTailService.popularBooks().books
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun getAuthorBooks(authorId: Int): List<Book>? {
        return withContext(Dispatchers.IO) {
            try {
                storyTailService.authorBooks(authorId).books
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun getBookPdf(bookId: Int, cacheDir: File): File? {
        return try {
            val bookUrl = storyTailService.getBookPdfUrl(bookId).books.first().bookPdfUrl
            val responseBody = storyTailService.getBookPdf(bookUrl)
            val file = File(cacheDir, "downloaded.pdf")
            file.outputStream().use { outputStream ->
                responseBody.byteStream().use { inputStream ->
                    outputStream.write(inputStream.readBytes())
                }
            }
            return file
        } catch (e: Exception) {
            null
        }
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