package com.fabiotiago.storytail.domain.repository

import com.fabiotiago.storytail.data.interfaces.StoryTailService
import com.fabiotiago.storytail.domain.managers.UserAuthenticationManager
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface UserAuthenticationRepository {
    suspend fun authenticateUser(
        username: String,
        password: String,
        callback: (Boolean, String) -> Unit
    )
}

class UserAuthenticationRepositoryImpl @Inject constructor(
    private val storyTailService: StoryTailService
) : UserAuthenticationRepository {

    override suspend fun authenticateUser(
        username: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        return withContext(Dispatchers.IO) {
            try {
                // Make the authentication request
                val response =
                    storyTailService.authenticate(AuthenticationRequest(username, password))

                // Check if the response is successful
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse != null) {
                        // Authentication was successful
                        callback(true, "Authentication successful: ${authResponse.message}")
                    } else {
                        // Null response body
                        callback(false, "Authentication failed: Empty response")
                    }
                    UserAuthenticationManager.apply {
                        isUserLoggedIn = true
                        userAccessLevel = authResponse?.user?.userTypeId ?: 2
                    }
                } else {
                    // Authentication failed (e.g., invalid credentials)
                    callback(false, "Authentication failed: ${response.code()}")
                }
            } catch (e: Exception) {
                // Handle any errors during the API call
                callback(false, "Authentication error: ${e.localizedMessage}")
            }
        }
    }
}

// Request data class for authentication
data class AuthenticationRequest(
    val username: String,
    val password: String
)

// Response data class for authentication
data class AuthenticationResponse(
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("error")
    val error: String? = null,

    @SerializedName("user")
    val user: User? = null
)

data class User(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_type_id")
    val userTypeId: Int = 2,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("user_name")
    val userName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("user_photo_url")
    val userPhotoUrl: String? = null,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)