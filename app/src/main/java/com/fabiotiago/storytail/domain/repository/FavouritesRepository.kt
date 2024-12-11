package com.fabiotiago.storytail.domain.repository

import com.fabiotiago.storytail.data.interfaces.StoryTailService
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface FavouritesRepository {
    suspend fun getFavourites(): List<Book>?
    suspend fun removeFavourite(userId: Int, bookId: Int): Boolean
    suspend fun addFavourite(userId: Int, bookId: Int): Boolean
}

class FavouritesRepositoryImpl @Inject constructor(
    private val storyTailService: StoryTailService
) : FavouritesRepository {
    override suspend fun getFavourites(): List<Book>? {
        return withContext(Dispatchers.IO) {
            try {
                storyTailService.getFavourites(1).books
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun addFavourite(userId: Int, bookId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = storyTailService.addFavourites(FavouriteRequest(bookId, userId))
                response.success
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun removeFavourite(userId: Int, bookId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = storyTailService.deleteFavourites(FavouriteRequest(bookId, userId))
                response.success
            } catch (e: Exception) {
                false
            }
        }
    }
}

data class FavouriteRequest(
    @SerializedName("book_id")
    val bookId: Int,
    @SerializedName("user_id")
    val userId: Int,
)

data class ApiResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)
