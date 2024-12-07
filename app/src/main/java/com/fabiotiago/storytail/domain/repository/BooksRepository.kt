package com.fabiotiago.storytail.domain.repository

import com.fabiotiago.storytail.app.ui.home.Book
import com.fabiotiago.storytail.data.interfaces.StoryTailService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface BooksRepository {
    suspend fun getBooks(): List<Book>?
    suspend fun getPopularBooks(): List<Book>?
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

}