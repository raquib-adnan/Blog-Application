package com.example.androidblogs.presentation.blog_content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidblogs.presentation.common_component.ShimmerEffect
import com.example.androidblogs.util.TextToSpeechUtil
import dev.jeziellago.compose.markdowntext.MarkdownText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogContentScreen(
    state: BlogContentState,
    onBackClick: () -> Unit,
    onAction: (BlogContentEvent) -> Unit
) {
    val context = LocalContext.current
    val ttsUtil = remember { TextToSpeechUtil(context) }
    var isSpeaking by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    DisposableEffect(Unit) {
        onDispose {
            ttsUtil.shutdown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.blog?.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isSpeaking) {
                                ttsUtil.stop()
                                isSpeaking = false
                            } else {
                                state.blog?.content?.let { content ->
                                    isSpeaking = true
                                    ttsUtil.speak(content) {
                                        isSpeaking = false
                                    }
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = if (isSpeaking) "Stop Reading" else "Read Aloud"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                state.blog?.let { blog ->
                    Text(
                        text = blog.title,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    MarkdownText(
                        markdown = blog.content ?: "",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier,
    shimmerBackgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(shimmerBackgroundColor)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                ShimmerEffect(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(shimmerBackgroundColor)
                )
                Spacer(modifier = Modifier.height(10.dp))
                ShimmerEffect(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth(fraction = 0.5f)
                        .height(20.dp)
                        .background(shimmerBackgroundColor)
                )
            }
        }
        repeat(times = 15) {
            ShimmerEffect(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(shimmerBackgroundColor)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLoadingContent() {
    LoadingContent()
}