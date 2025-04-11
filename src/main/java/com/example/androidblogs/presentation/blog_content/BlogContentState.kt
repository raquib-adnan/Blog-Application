package com.example.androidblogs.presentation.blog_content

import com.example.androidblogs.domain.model.Blog

data class BlogContentState(
    val blog: Blog? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class Blog(
    val id: Int,
    val title: String,
    val content: String,
    val author: String,
    val date: String
)
