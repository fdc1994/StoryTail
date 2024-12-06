package com.fabiotiago.storytail.app

import javax.inject.Inject

interface Manager {
    fun provideText(): String
    fun isUserLoggedIn(): Boolean
}

class ManagerImpl @Inject constructor(): Manager {
    override fun provideText(): String {
        return "test"
    }

    override fun isUserLoggedIn(): Boolean {
        return true
    }

}