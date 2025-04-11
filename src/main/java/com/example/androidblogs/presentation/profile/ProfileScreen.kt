package com.example.androidblogs.presentation.profile

// Compose imports for UI components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

// Navigation for handling screen transitions
import androidx.navigation.NavController

// Coil for image loading
import coil.compose.AsyncImage

// Firebase for authentication
import com.google.firebase.auth.FirebaseAuth

/**
 * ProfileScreen is a composable function that displays the user's profile information.
 * It shows the user's email and unique ID (UID) from Firebase Authentication.
 * 
 * @param navController Used for navigation between screens
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController
) {
    // Get the current authenticated user from Firebase
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // Scaffold provides the basic material design layout structure
    Scaffold(
        // TopAppBar contains the screen title and back button
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Column layout for arranging items vertically
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Display user's email with a fallback text if not available
            Text(
                text = "Email: ${currentUser?.email ?: "Not Available"}",
                style = MaterialTheme.typography.bodyLarge
            )
            // Display user's unique ID (UID) with a fallback text if not available
            Text(
                text = "UID: ${currentUser?.uid ?: "Not Available"}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}