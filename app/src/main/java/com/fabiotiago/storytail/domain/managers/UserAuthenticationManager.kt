package com.fabiotiago.storytail.domain.managers

import com.fabiotiago.storytail.domain.repository.User

object UserAuthenticationManager {

    var user : User? = null

    var isUserLoggedIn: Boolean = false

    var userAccessLevel: Int = 0
}