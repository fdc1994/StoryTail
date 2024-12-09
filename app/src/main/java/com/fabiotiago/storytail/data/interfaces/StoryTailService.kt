package com.fabiotiago.storytail.data.interfaces

import com.fabiotiago.storytail.app.ui.home.BooksResponse
import com.fabiotiago.storytail.app.ui.home.FavouritesResponse
import com.fabiotiago.storytail.app.ui.home.ReadBookResponse
import com.fabiotiago.storytail.domain.repository.ApiResponse
import com.fabiotiago.storytail.domain.repository.FavouriteRequest
import okhttp3.ResponseBody
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
}
