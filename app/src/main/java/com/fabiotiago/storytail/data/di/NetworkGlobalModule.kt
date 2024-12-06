package com.fabiotiago.storytail.data.di

import com.fabiotiago.storytail.data.interfaces.StoryTailService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkGlobalModule {
    private val gson: Gson
        get() = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideStoryTailService(): StoryTailService {
        return Retrofit.Builder()
            .baseUrl("http://192.168.0.101:5002")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(StoryTailService::class.java)
    }
}

