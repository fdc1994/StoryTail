package com.fabiotiago.storytail.domain.repository

import com.fabiotiago.storytail.app.ui.home.Book
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