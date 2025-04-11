package com.example.androidblogs.presentation.auth

// Android imports for activity and UI components
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

// Firebase imports for authentication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * RegisterActivity handles user registration using Firebase Authentication.
 * It provides a form for users to create a new account with email and password.
 */
class RegisterActivity : ComponentActivity() {
    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set up the Compose UI
        setContent {
            // State variables for email and password input
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var confirmPassword by remember { mutableStateOf("") }

            // Column layout for the registration form
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Email input field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Password input field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Confirm password input field
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Register button
                Button(
                    onClick = {
                        // Validate passwords match
                        if (password != confirmPassword) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Passwords do not match",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        // Create user with email and password
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this@RegisterActivity) { task ->
                                if (task.isSuccessful) {
                                    // Registration successful
                                    val user = auth.currentUser
                                    updateUI(user)
                                } else {
                                    // Registration failed
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Registration failed: ${task.exception?.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
                }
            }
        }
    }

    /**
     * Updates the UI after successful registration
     * @param user The Firebase user object
     */
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Registration successful, you should navigate to the main screen
            finish()
        }
    }
} 