package com.example.androidblogs.presentation.auth

// Android imports for activity and UI components
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * WelcomeActivity is the first screen users see when opening the app.
 * It provides options to either log in or register a new account.
 */
class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set up the Compose UI
        setContent {
            WelcomeScreen(
                onLoginClick = {
                    // Navigate to LoginActivity
                    startActivity(Intent(this, LoginActivity::class.java))
                },
                onRegisterClick = {
                    // Navigate to RegisterActivity
                    startActivity(Intent(this, RegisterActivity::class.java))
                }
            )
        }
    }
}

/**
 * WelcomeScreen composable that displays the welcome UI
 * @param onLoginClick Callback for login button click
 * @param onRegisterClick Callback for register button click
 */
@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    // Column layout for the welcome screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Welcome message
        Text(
            text = "Welcome to Android Blogs",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Login button
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Register button
        Button(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
} 