package com.murilo.audiojournal

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

/**
 * A wrapper class that abstracts the Android MediaRecorder API.
 * Handles the configuration and execution of the hardware microphone to record audio.
 * @param context The application context required to initialize the MediaRecorder.
 */
class JournalRecorder(private val context: Context) {
    private var recorder : MediaRecorder? = null

    /**
     * Prepares and starts the audio recording engine, saving the output in MPEG_4 format with AAC encoding.
     * @param outputFile The precise File destination where the audio data will be saved.
     */
    fun start(outputFile: File) {
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            (MediaRecorder())
        }

        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile.absolutePath)

            prepare()
            start()
        }
    }

    /**
     * Stops the active recording and safely releases the hardware resources
     * back to the Android operating system to prevent memory leaks.
     */
    fun stop() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }
}