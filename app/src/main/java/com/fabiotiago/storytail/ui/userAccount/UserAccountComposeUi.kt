package com.fabiotiago.storytail.ui.userAccount

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp


object UserAccountComposeUi {

    @Composable
    fun UserAccountScreen() {
        // State for managing login status and user data
        var isLoggedIn by rememberSaveable { mutableStateOf(false) }
        var userName by rememberSaveable { mutableStateOf("") }
        var userEmail by rememberSaveable { mutableStateOf("") }

        if (isLoggedIn) {
            // Show post-login UI
            PostLoginScreen(name = userName, email = userEmail) {
                isLoggedIn = false // Logout action
            }
        } else {
            // Show login UI
            LoginForm(
                onLogin = { name, email ->
                    userName = name
                    userEmail = email
                    isLoggedIn = true
                }
            )
        }
    }

    @Composable
    fun LoginForm(onLogin: (String, String) -> Unit) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Login", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        onLogin(email, password) // Pass user details to the parent
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
        }
    }

    @Composable
    fun PostLoginScreen(name: String, email: String, onLogout: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome, $name!", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Email: $email", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Text("Logout")
            }
        }
    }
}