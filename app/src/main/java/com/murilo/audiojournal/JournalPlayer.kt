package com.murilo.audiojournal

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

class JournalPlayer(private val context: Context) {
    private var player: MediaPlayer? = null

    fun playFile(file: File, onPlaybackComplete: () -> Unit) {
        stop()

        player = MediaPlayer.create(context, file.toUri()).apply {
            setOnCompletionListener {
                stop()
                onPlaybackComplete()
            }
            start()
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