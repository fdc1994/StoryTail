package com.fabiotiago.storytail.app.di

import com.fabiotiago.storytail.app.Manager
import com.fabiotiago.storytail.app.ManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    abstract fun bindManager(
        managerImpl: ManagerImpl
    ): Manager
}