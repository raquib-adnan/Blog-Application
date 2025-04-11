package com.example.androidblogs.presentation.settings

// Compose imports for UI components
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Navigation imports
import androidx.navigation.NavController
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore

/**
 * SettingsScreen is a composable function that displays app settings and preferences.
 * It allows users to customize their app experience and manage app-related settings.
 * 
 * @param navController Used for navigation between screens
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {
    // Scaffold provides the basic material design layout structure
    Scaffold(
        // TopAppBar contains the screen title and back button
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
        // Column layout for arranging settings items vertically
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Theme Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Theme Mode"
                    )
                    Text(if (isDarkTheme) "Dark Mode" else "Light Mode")
                }
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = onThemeChanged
                )
            }
            
            HorizontalDivider()
            
            // Additional settings sections
            Text(
                text = "App Version",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "1.0.0",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
} 