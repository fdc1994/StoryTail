package com.fabiotiago.storytail.app.ui.about

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fabiotiago.storytail.R

object AboutComposeUi {

    @Composable
    fun AboutPage(aboutViewModel: AboutViewModel) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 45.dp)
            ) {
                item {
                    LogoSection()
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    // App Description
                    Text(
                        text = "Welcome to StoryTail",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Justify
                    )
                    Text(
                        text = "StoryTail is an innovative platform that allows you to explore and enjoy books through reading, audio narration, and video storytelling. Whether you're a casual reader, a student, or a lifelong learner, StoryTail has something for everyone!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    // Key Features Section
                    FeaturesSection()

                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {
                    // GDPR Compliance
                    GDPRSection()

                    Spacer(modifier = Modifier.height(24.dp))

                    // Contact Section
                    ContactForm(aboutViewModel::onContactFormSubmitted)
                }
            }
        }
    }

    @Composable
    fun LogoSection() {
        // Replace with your app's logo
        Image(
            painter = painterResource(id = R.drawable.story_tail_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(300.dp)
                .padding(8.dp),
            contentScale = ContentScale.Crop
        )
    }

    @Composable
    fun FeaturesSection() {
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Key Features",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Justify
            )
            Text(
                text = "• Explore books in various formats: text, audio, and video.",
                fontSize = 16.sp,
                textAlign = TextAlign.Justify
            )
            Text(
                text = "• Personalized recommendations based on your preferences.",
                fontSize = 16.sp,
                textAlign = TextAlign.Justify
            )
            Text(text = "• Create and manage favorites and track your progress.", fontSize = 16.sp)
            Text(
                text = "• Stay connected with the latest book releases and updates.",
                fontSize = 16.sp,
                textAlign = TextAlign.Justify
            )
        }
    }

    @Composable
    fun GDPRSection() {
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "GDPR Compliance",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Your privacy is important to us. StoryTail is fully compliant with GDPR regulations. We ensure that your data is securely stored and handled responsibly. You have full control over your data and can request its deletion at any time.",
                fontSize = 16.sp,
                textAlign = TextAlign.Justify
            )
        }
    }

    @Composable
    fun ContactForm(
        onSubmit: (name: String, email: String, message: String) -> Unit
    ) {
        var name by rememberSaveable { mutableStateOf("") }
        var email by rememberSaveable { mutableStateOf("") }
        var message by rememberSaveable { mutableStateOf("") }
        var context = LocalContext.current

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Contact Us",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )

            // Message Field
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Message") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                maxLines = 4
            )

            // Submit Button
            Button(
                onClick = {
                    if (name.isNotBlank() && email.isNotBlank() && message.isNotBlank()) {
                        onSubmit(name, email, message)
                        name = ""
                        email = ""
                        message = ""
                        Toast.makeText(context, "Message Submitted successfully!", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(context, "You need to fill all fields", Toast.LENGTH_LONG)
                            .show()
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Submit")
            }
        }
    }

}