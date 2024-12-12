package com.fabiotiago.storytail.domain.repository

import com.fabiotiago.storytail.data.interfaces.StoryTailService
import com.google.gson.annotations.SerializedName
import java.io.File
import javax.inject.Inject

interface AuthorRepository {
    suspend fun getAuthor(id: Int): Author?
}

class AuthorRepositoryImpl @Inject constructor(
    private val storyTailService: StoryTailService
): AuthorRepository {
    override suspend fun getAuthor(id: Int): Author? {
        return try {
            storyTailService.getAuthor(id)
        } catch (e: Exception) {
            null
        }
    }
}


data class Author(
    val id: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    val description: String?,
    val nationality: String?
)