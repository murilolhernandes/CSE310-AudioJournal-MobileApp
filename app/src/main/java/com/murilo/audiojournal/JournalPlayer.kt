package com.murilo.audiojournal

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

/**
 * A wrapper class that abstracts the Android MediaPlayer API.
 * Handles safely loading, playing, and stopping audio files from the device.
 * @param context The application context required to initialize the MediaPlayer.
 */
class JournalPlayer(private val context: Context) {
    private var player: MediaPlayer? = null

    /**
     * Initializes the media player with a specific file and begins playback.
     * Safely aborts if the file is corrupted or unreadable.
     * @param file The audio File to be played.
     * @param onPlaybackComplete Callback triggered automatically when the audio reaches the end.
     */
    fun playFile(file: File, onPlaybackComplete: () -> Unit) {
        stop()

        try {
            player = MediaPlayer.create(context, file.toUri())

            if (player == null) {
                return
            }

            player?.apply {
                setOnCompletionListener {
                    stop()
                    onPlaybackComplete()
                }
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stop()
        }
    }

    /**
     * Stops current audio playback and safely releases the hardware resources
     * back to the Android operating system.
     */
    fun stop() {
        player?.apply {
            stop()
            release()
        }
        player = null
    }
}