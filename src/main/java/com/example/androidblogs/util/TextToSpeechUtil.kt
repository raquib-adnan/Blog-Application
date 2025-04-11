package com.example.androidblogs.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TextToSpeechUtil(private val context: Context) {
    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false

    fun initialize(onInitListener: ((Boolean) -> Unit)? = null) {
        textToSpeech = TextToSpeech(context) { status ->
            isInitialized = status == TextToSpeech.SUCCESS
            if (isInitialized) {
                textToSpeech?.language = Locale.getDefault()
            }
            onInitListener?.invoke(isInitialized)
        }
    }

    fun speak(text: String, onComplete: (() -> Unit)? = null) {
        if (!isInitialized) {
            initialize { success ->
                if (success) {
                    speakText(text, onComplete)
                }
            }
        } else {
            speakText(text, onComplete)
        }
    }

    private fun speakText(text: String, onComplete: (() -> Unit)? = null) {
        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) {
                onComplete?.invoke()
            }
            override fun onError(utteranceId: String?) {}
        })

        textToSpeech?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "utteranceId"
        )
    }

    fun stop() {
        textToSpeech?.stop()
    }

    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        isInitialized = false
    }
} 