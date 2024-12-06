package com.fabiotiago.storytail.data.interfaces

import com.fabiotiago.storytail.app.ui.home.BooksResponse
import retrofit2.http.GET

interface StoryTailService {

    @GET("/books")
    suspend fun books(): BooksResponse
}