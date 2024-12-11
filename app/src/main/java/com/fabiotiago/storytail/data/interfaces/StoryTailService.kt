package com.fabiotiago.storytail.data.interfaces

import com.fabiotiago.storytail.domain.repository.ApiResponse
import com.fabiotiago.storytail.domain.repository.AuthenticationRequest
import com.fabiotiago.storytail.domain.repository.AuthenticationResponse
import com.fabiotiago.storytail.domain.repository.BooksResponse
import com.fabiotiago.storytail.domain.repository.ChangeUserTypeRequest
import com.fabiotiago.storytail.domain.repository.ChangeUserTypeResponse
import com.fabiotiago.storytail.domain.repository.FavouriteRequest
import com.fabiotiago.storytail.domain.repository.FavouritesResponse
import com.fabiotiago.storytail.domain.repository.ReadBookResponse
import com.fabiotiago.storytail.domain.repository.UserAuthenticationRepositoryImpl
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryTailService {

    @GET("/books")
    suspend fun books(): BooksResponse

    @GET("/popular-books")
    suspend fun popularBooks(): BooksResponse

    @GET("/favourites/{userId}")
    suspend fun getFavourites(@Path("userId") userId: Int): FavouritesResponse

    @Headers("Content-Type: application/json")
    @POST("/favourites")
    suspend fun addFavourites(@Body favouriteRequest: FavouriteRequest): ApiResponse

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = "/favourites", hasBody = true)
    suspend fun deleteFavourites(@Body favouriteRequest: FavouriteRequest): ApiResponse

    @GET("/bookUrl")
    suspend fun getBookPdfUrl(@Query("id") id: Int): ReadBookResponse

    @GET("/livros/{fileName}")
    suspend fun getBookPdf(@Path("fileName") fileName: String): ResponseBody

    @POST("/authenticate")
    suspend fun authenticate(@Body request: AuthenticationRequest): Response<AuthenticationResponse>

    @POST("/upgrade")
    suspend fun upgradeUser(@Body request: ChangeUserTypeRequest): Response<ChangeUserTypeResponse>
}
