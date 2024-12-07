package com.fabiotiago.storytail.data.interfaces

import com.fabiotiago.storytail.app.ui.home.BooksResponse
import com.fabiotiago.storytail.app.ui.home.FavouritesResponse
import com.fabiotiago.storytail.domain.repository.ApiResponse
import com.fabiotiago.storytail.domain.repository.FavouriteRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

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
}
