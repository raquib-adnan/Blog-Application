package com.example.androidblogs.navigation

// Navigation imports
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

// Screen imports
import com.example.androidblogs.presentation.blog_list.BlogListScreen
import com.example.androidblogs.presentation.blog_list.BlogListViewModel
import com.example.androidblogs.presentation.blog_content.BlogContentScreen
import com.example.androidblogs.presentation.blog_content.BlogContentViewModel
import com.example.androidblogs.presentation.profile.ProfileScreen
import com.example.androidblogs.presentation.settings.SettingsScreen

/**
 * NavGraph defines the navigation structure of the app.
 * It maps routes to their corresponding screens and handles navigation between them.
 * 
 * @param navController The NavController that manages app navigation
 * @param isDarkTheme Current theme state
 * @param onThemeChanged Callback for theme changes
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {
    // NavHost is the container for the navigation graph
    NavHost(
        navController = navController,
        // Start destination is the first screen shown when the app launches
        startDestination = "blog_list"
    ) {
        // Blog List Screen
        composable("blog_list") {
            val viewModel: BlogListViewModel = viewModel()
            val state by viewModel.state.collectAsState()
            
            BlogListScreen(
                state = state,
                event = viewModel.events,
                onBlogCardClick = { blogId ->
                    navController.navigate("blog_content/$blogId")
                }
            )
        }

        // Blog Content Screen
        composable(
            route = "blog_content/{blogId}",
            arguments = listOf(
                navArgument("blogId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val viewModel: BlogContentViewModel = viewModel()
            val state by viewModel.state.collectAsState()
            
            BlogContentScreen(
                state = state,
                onBackClick = { navController.navigateUp() },
                onAction = viewModel::onAction
            )
        }

        // Profile Screen
        composable("profile") {
            ProfileScreen(navController)
        }

        // Settings Screen
        composable("settings") {
            SettingsScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onThemeChanged = onThemeChanged
            )
        }
    }
} 