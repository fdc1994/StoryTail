package com.fabiotiago.storytail.domain.repository

import com.fabiotiago.storytail.data.interfaces.StoryTailService
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ContactRepository {
    suspend fun sendContactRequest(contactRequest: ContactRequest): Boolean
}

class ContactRepositoryImpl @Inject constructor(
    private val storyTailService: StoryTailService
) : ContactRepository {
    override suspend fun sendContactRequest(contactRequest: ContactRequest): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                return@withContext storyTailService.sendContactRequest(contactRequest)
                    .message() == "Contact request submitted successfully"
            } catch (e: Exception) {
                false
            }
        }
    }
}

data class ContactRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("message")
    val message: String,
)

data class ContactRequestResponse(
    @SerializedName("message")
    val message: String,
)