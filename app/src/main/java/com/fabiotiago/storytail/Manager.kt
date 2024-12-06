package com.fabiotiago.storytail

import javax.inject.Inject

interface Manager {
    fun provideText(): String
}

class ManagerImpl @Inject constructor(): Manager {
    override fun provideText(): String {
        return "test"
    }

}