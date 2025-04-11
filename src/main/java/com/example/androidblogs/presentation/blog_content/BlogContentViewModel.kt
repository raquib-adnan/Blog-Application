package com.example.androidblogs.presentation.blog_content

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.androidblogs.domain.repository.BlogRepository
import com.example.androidblogs.domain.util.Result
import com.example.androidblogs.presentation.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BlogContentViewModel(
    savedStateHandle: SavedStateHandle,
    private val blogRepository: BlogRepository
): ViewModel() {

    private val blogId = savedStateHandle.toRoute<Route.BlogContentScreen>().blogId

    private val _state = MutableStateFlow(BlogContentState())
    val state = _state
        .onStart {
            getBlogById()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = _state.value
        )

    fun onAction(event: BlogContentEvent) {
        when(event) {
            BlogContentEvent.Refresh -> getBlogById()
            is BlogContentEvent.ToggleTextToSpeech -> {
                // Handle text-to-speech toggle if needed
            }
        }
    }

    private fun getBlogById() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when(val result = blogRepository.getBlogById(blogId)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            errorMessage = null,
                            blog = result.data,
                            isLoading = false
                        )
                    }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            errorMessage = result.message,
                            blog = result.data,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}