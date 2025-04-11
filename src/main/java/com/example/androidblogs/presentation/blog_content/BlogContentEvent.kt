package com.example.androidblogs.presentation.blog_content

sealed class BlogContentEvent {
    object Refresh : BlogContentEvent()
    data class ToggleTextToSpeech(val isSpeaking: Boolean) : BlogContentEvent()
} 