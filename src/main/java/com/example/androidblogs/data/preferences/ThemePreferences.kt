package com.example.androidblogs.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create a DataStore instance at the top level
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

// This class manages theme preferences for the application.
class ThemePreferences(private val context: Context) {
    private val isDarkThemeKey = booleanPreferencesKey("is_dark_theme")

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[isDarkThemeKey] ?: false
        }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[isDarkThemeKey] = isDark
        }
    }

    companion object {
        private const val PREFS_NAME = "theme_prefs" // Name of the SharedPreferences file.
        private const val KEY_THEME = "key_theme"   // Key for storing the theme setting.
    }
}

object Constant {
    const val GITHUB_URL = "https://raw.githubusercontent.com/raquib-adnan/MyBlogs/main/blogs.json"
}