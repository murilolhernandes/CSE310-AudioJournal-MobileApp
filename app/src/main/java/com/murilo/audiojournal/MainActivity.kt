package com.murilo.audiojournal

import android.content.Context
import android.media.MediaMetadataRetriever
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import com.murilo.audiojournal.JournalRecorder
import kotlinx.coroutines.delay
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.animation.core.animateDpAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()

        }
    }
}

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFF1D1D1D)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Status Bar Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF4F4F4F))
                .statusBarsPadding()
        )
        AudioJournalScreen()
    }
}

@Composable
fun AudioJournalScreen() {
    var isRecording by remember { mutableStateOf(false) }

    var currentlyPlayingFile by remember { mutableStateOf<File?>(null) }

    val context = LocalContext.current
    val audioPlayer = remember { JournalPlayer(context) }

    Column(
        modifier = Modifier.fillMaxSize()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Greeting()

        Spacer(modifier = Modifier.height(30.dp))

        RecordingContainer(
            isRecording = isRecording,
            onRecordingChange = {
                isRecording = it
                if (it) {
                    audioPlayer.stop()
                    currentlyPlayingFile = null
                }
            },
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        AudioLogs(
            isRecording = isRecording,
            currentlyPlayingFile = currentlyPlayingFile,
            onPlayClick = { fileToPlay ->
                currentlyPlayingFile = fileToPlay
                audioPlayer.playFile(
                    file = fileToPlay,
                    onPlaybackComplete = {
                        currentlyPlayingFile = null
                    }
                )
            },
            onStopClick = {
                audioPlayer.stop()
                currentlyPlayingFile = null
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Text(
        text = "AudioJournal",
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF424B54))
            .padding(
                start = 24.dp,
                top = 28.dp,
                bottom = 28.dp
            )
    )
}

@Composable
fun RecordingContainer(
    isRecording: Boolean,
    onRecordingChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val audioRecorder = remember { JournalRecorder(context) }
    val containerShape = RoundedCornerShape(12.dp)
    var timeInMillis by remember { mutableStateOf(0L) }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            timeInMillis = 0L
            val startTime = System.currentTimeMillis()
            while (true) {
                timeInMillis = System.currentTimeMillis() - startTime
                delay(30L)
            }
        }
    }

    Column(
        modifier
            .background(Color(0xFF4F4F4F), shape = containerShape)
            .clip(containerShape),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RecordingTimer(timeInMillis = timeInMillis)
        Box(
            modifier = Modifier
                .background(Color(0xFF252525))
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            RecordControls(
                isRecording = isRecording,
                onRecordStart = {
                    val fileName = "Journal_${System.currentTimeMillis()}.mp4"
                    val outputFile = File(context.cacheDir, fileName)

                    audioRecorder.start(outputFile)

                    onRecordingChange(true)
                },
                onStopClick = {
                    audioRecorder.stop()
                    onRecordingChange(false)
                    timeInMillis = 0L
                }
            )
        }
    }
}

@Composable
fun RecordingTimer(timeInMillis: Long) {
    val hours = timeInMillis / 3600
    val minutes = (timeInMillis / 1000) / 60
    val seconds = (timeInMillis / 1000) % 60
    val milliSeconds = (timeInMillis % 1000) / 10
    val formattedTime = String.format("%02d:%02d:%02d", minutes, seconds, milliSeconds)

    Text(
        text = formattedTime,
        color = Color.White,
        fontSize = 70.sp,
        modifier = Modifier.padding(vertical = 32.dp)
    )
}

@Composable
fun AudioListItem(
    record: AudioRecord,
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onStopClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .background(Color(0xFF4F4F4F), shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = record.fileName, color = Color.White, fontWeight = Bold, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(5.dp))

            Text(text = record.duration, color = Color.LightGray, fontSize = 20.sp)
        }
        IconButton(
            onClick = {
                if (isPlaying) onStopClick() else onPlayClick()
            },
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Stop Audio" else "Play Audio",
                modifier = Modifier.size(30.dp),
                tint = Color.DarkGray
            )
        }
    }
}

@Composable
fun AudioLogs(
    isRecording: Boolean,
    currentlyPlayingFile: File?,
    onPlayClick: (File) -> Unit,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var recordings by remember { mutableStateOf(emptyList<AudioRecord>()) }
    val context = LocalContext.current

    val listState = rememberLazyListState()

    LaunchedEffect(isRecording) {
        if (!isRecording) {
            recordings = fetchRecordings(context)
        }
    }

    if (recordings.isEmpty()) {
        Text(text = "Your Audio Log is currently empty...", color = Color.White, fontSize = 20.sp)
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(recordings) { record ->
                    AudioListItem(
                        record = record,
                        isPlaying = currentlyPlayingFile === record.file,
                        onPlayClick = { onPlayClick(record.file) },
                        onStopClick = onStopClick

                    )
                }
            }

            val scrollProgress by remember {
                derivedStateOf {
                    val layoutInfo = listState.layoutInfo
                    if (layoutInfo.visibleItemsInfo.isEmpty()) return@derivedStateOf 0f
                    val firstItem = layoutInfo.visibleItemsInfo.first()
                    val itemsOnScreen = layoutInfo.viewportSize.height.toFloat() / firstItem.size.toFloat()
                    val exactPosition = listState.firstVisibleItemIndex.toFloat() +
                            (listState.firstVisibleItemScrollOffset.toFloat() / firstItem.size.toFloat())
                    val maxScroll = (layoutInfo.totalItemsCount.toFloat() - itemsOnScreen).coerceAtLeast(0.01f)

                    (exactPosition / maxScroll).coerceIn(0f, 1f)
                }
            }

            val showScrollbar by remember {
                derivedStateOf {
                    listState.canScrollForward || listState.canScrollBackward
                }
            }

            if (showScrollbar) {
                BoxWithConstraints(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 4.dp, top = 8.dp, bottom = 8.dp)
                        .fillMaxHeight()
                ) {
                    val trackHeight = maxHeight
                    val layoutInfo = listState.layoutInfo

                    val targetThumbHeight = if (layoutInfo.visibleItemsInfo.isNotEmpty()) {
                        val firstItem = layoutInfo.visibleItemsInfo.first()
                        val itemsOnScreen = layoutInfo.viewportSize.height.toFloat() / firstItem.size.toFloat()
                        val heightRatio = (itemsOnScreen / layoutInfo.totalItemsCount.toFloat()).coerceIn(0f, 1f)
                        (trackHeight * heightRatio).coerceAtLeast(40.dp)
                    } else {
                        40.dp
                    }

                    val animatedThumbHeight by animateDpAsState(
                        targetValue = targetThumbHeight,
                        label = "scrollbar_size_anim"
                    )
                    val targetYOffset = (trackHeight - animatedThumbHeight) * scrollProgress
                    val animatedYOffset by animateDpAsState(
                        targetValue = targetYOffset,
                        label = "scrollbar_position_anim"
                    )
                    //val totalItems = layoutInfo.totalItemsCount.toFloat()
                    //val visibleItems = layoutInfo.visibleItemsInfo.size.toFloat()
                    //val heightRatio = if (totalItems > 0) visibleItems / totalItems else 1f

                    Box(
                        modifier = Modifier
                            .offset(y = animatedYOffset)
                            .width(6.dp)
                            .height(animatedThumbHeight)
                            .background(Color.Gray, RoundedCornerShape(percent = 50))
                    )
                }
            }
        }

    }
}

fun fetchRecordings(context: Context): List<AudioRecord> {
    val directory = context.cacheDir
    val files = directory.listFiles()?.filter { it.extension == "mp4" } ?: emptyList()
    val sortedFiles = files.sortedBy { it.lastModified() }

    val retriever = MediaMetadataRetriever()

    val records = sortedFiles.map { file ->
        val dateString = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(file.lastModified()))

        var formattedDuration = "--:--:--"

        try {
            retriever.setDataSource(file.absolutePath)

            val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val timeInMillis = durationString?.toLong() ?: 0L

            val hours = (timeInMillis / (1000 * 60 * 60)).toInt()
            val minutes = (timeInMillis % (1000 * 60 * 60) / (1000 * 60)).toInt()
            val seconds = (timeInMillis % (1000 * 60) / 1000).toInt()

            formattedDuration = if (hours > 0) {
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format("%02d:%02d", minutes, seconds)
            }
        } catch (e: Exception) {

        }

        AudioRecord(
            fileName = file.name,
            duration = formattedDuration,
            date = dateString,
            file = file
        )
    }

    try {
        retriever.release()
    } catch (e: Exception) {

    }

    return records
}

data class AudioRecord(
    val fileName: String,
    val duration: String,
    val date: String,
    val file: File
)