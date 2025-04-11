package com.example.androidblogs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.androidblogs.presentation.navigation.NavGraph
import com.example.androidblogs.presentation.theme.AndroidBlogsTheme
import com.example.androidblogs.data.preferences.ThemePreferences
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import com.example.androidblogs.presentation.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import android.speech.tts.TextToSpeech
import java.util.Locale

/**
 * MainActivity is the entry point of the application.
 * It sets up the main UI and navigation structure.
 */
class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private lateinit var themePreferences: ThemePreferences
    private lateinit var tts: TextToSpeech

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themePreferences = ThemePreferences(this)
        installSplashScreen()
        enableEdgeToEdge()
        tts = TextToSpeech(this, this)
        setContent {
            val isDarkTheme by themePreferences.isDarkTheme.collectAsState(initial = false)
            val navController = rememberNavController()
            var showMenu by remember { mutableStateOf(false) }
            val currentUser = FirebaseAuth.getInstance().currentUser
            
            AndroidBlogsTheme(darkTheme = isDarkTheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Qwerty Blog") },
                            actions = {
                                Box {
                                    IconButton(
                                        onClick = { showMenu = !showMenu }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AccountCircle,
                                            contentDescription = "Profile"
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = showMenu,
                                        onDismissRequest = { showMenu = false }
                                    ) {
                                        // User Info Header
                                        Column(
                                            modifier = Modifier
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                                .width(200.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.AccountCircle,
                                                contentDescription = null,
                                                modifier = Modifier.size(48.dp),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = currentUser?.email ?: "Email",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }

                                        HorizontalDivider(
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )

                                        // Profile Option
                                        DropdownMenuItem(
                                            text = { Text("Profile") },
                                            onClick = {
                                                navController.navigate("profile")
                                                showMenu = false
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Default.Person,
                                                    contentDescription = null
                                                )
                                            }
                                        )

                                        // Settings Option
                                        DropdownMenuItem(
                                            text = { Text("Settings") },
                                            onClick = {
                                                navController.navigate("settings")
                                                showMenu = false
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Default.Settings,
                                                    contentDescription = null
                                                )
                                            }
                                        )

                                        HorizontalDivider(
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )

                                        // Logout Option
                                        DropdownMenuItem(
                                            text = { 
                                                Text(
                                                    "Logout",
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                            },
                                            onClick = {
                                                FirebaseAuth.getInstance().signOut()
                                                navController.navigate("welcome") {
                                                    popUpTo(0)
                                                }
                                                showMenu = false
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.AutoMirrored.Filled.ExitToApp,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavGraph(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        isDarkTheme = isDarkTheme,
                        onThemeChanged = { newValue ->
                            lifecycleScope.launch {
                                themePreferences.setDarkTheme(newValue)
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
        }
    }

    private fun readBlogAloud(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}