package com.murilo.audiojournal

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

class JournalPlayer(private val context: Context) {
    private var player: MediaPlayer? = null

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

    fun stop() {
        player?.apply {
            stop()
            release()
        }
        player = null
    }
}