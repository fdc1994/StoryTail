package com.fabiotiago.storytail.domain.di

import com.fabiotiago.storytail.domain.repository.AuthorRepository
import com.fabiotiago.storytail.domain.repository.AuthorRepositoryImpl
import com.fabiotiago.storytail.domain.repository.BooksRepository
import com.fabiotiago.storytail.domain.repository.BooksRepositoryImpl
import com.fabiotiago.storytail.domain.repository.ContactRepository
import com.fabiotiago.storytail.domain.repository.ContactRepositoryImpl
import com.fabiotiago.storytail.domain.repository.FavouritesRepository
import com.fabiotiago.storytail.domain.repository.FavouritesRepositoryImpl
import com.fabiotiago.storytail.domain.repository.UserRepository
import com.fabiotiago.storytail.domain.repository.UserRepositoryImpl
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
    fun bindUserAuthenticationRepository(userAuthenticationRepository: UserRepositoryImpl): UserRepository

    @Binds
    fun bindContactRepository(contactRepositoryImpl: ContactRepositoryImpl): ContactRepository

    @Binds
    fun bindAuthorRepository(authorRepositoryImpl: AuthorRepositoryImpl): AuthorRepository
}