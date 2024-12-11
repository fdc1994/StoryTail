package com.fabiotiago.storytail.domain.di

import com.fabiotiago.storytail.domain.repository.BooksRepository
import com.fabiotiago.storytail.domain.repository.BooksRepositoryImpl
import com.fabiotiago.storytail.domain.repository.FavouritesRepository
import com.fabiotiago.storytail.domain.repository.FavouritesRepositoryImpl
import com.fabiotiago.storytail.domain.repository.UserAuthenticationRepository
import com.fabiotiago.storytail.domain.repository.UserAuthenticationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    @Binds
    fun bindBooksRepository(booksRepositoryImpl: BooksRepositoryImpl): BooksRepository

    @Binds
    fun bindFavouritesRepository(favouritesRepositoryImpl: FavouritesRepositoryImpl): FavouritesRepository

    @Binds
    fun bindUserAuthenticationRepository(userAuthenticationRepository: UserAuthenticationRepositoryImpl): UserAuthenticationRepository
}