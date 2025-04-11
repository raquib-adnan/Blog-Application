package com.example.androidblogs.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.androidblogs.presentation.blog_list.BlogListScreen
import com.example.androidblogs.presentation.blog_list.BlogListViewModel
import com.example.androidblogs.presentation.blog_content.BlogContentScreen
import com.example.androidblogs.presentation.blog_content.BlogContentViewModel
import com.example.androidblogs.presentation.settings.SettingsScreen
import com.example.androidblogs.presentation.profile.ProfileScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import androidx.core.os.bundleOf

sealed class Screen(val route: String) {
    object BlogList : Screen("blog_list_screen")
    object BlogContent : Screen("blog_content_screen/{blogId}") {
        fun createRoute(blogId: Int) = "blog_content_screen/$blogId"
    }
    object Settings : Screen("settings")
    object Profile : Screen("profile")
}

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.BlogList.route
    ) {
        composable(Screen.BlogList.route) {
            val viewModel = koinViewModel<BlogListViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            BlogListScreen(
                state = state,
                event = viewModel.events,
                onBlogCardClick = { id ->
                    navController.navigate(Screen.BlogContent.createRoute(id))
                }
            )
        }

        composable(
            route = Screen.BlogContent.route,
            arguments = listOf(
                navArgument("blogId") {
                    type = NavType.IntType
                }
            )
        ) {
            val viewModel = koinViewModel<BlogContentViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            BlogContentScreen(
                state = state,
                onBackClick = { navController.navigateUp() },
                onAction = viewModel::onAction
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onThemeChanged = onThemeChanged
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

    }
}
