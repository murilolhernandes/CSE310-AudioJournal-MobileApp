package com.murilo.audiojournal

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import kotlinx.coroutines.delay

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
    Column(
        modifier = Modifier.fillMaxSize()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Greeting()

        Spacer(modifier = Modifier.height(30.dp))

        RecordingContainer(modifier = Modifier.padding(horizontal = 20.dp))

        Spacer(modifier = Modifier.height(32.dp))

        AudioLogs()
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
fun RecordingContainer(modifier: Modifier = Modifier) {
    val containerShape = RoundedCornerShape(12.dp)
    var isRecording by remember { mutableStateOf(false) }
    var timeInSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            while (true) {
                delay(1000L)
                timeInSeconds += 1
            }
        }
    }

    Column(
        modifier
            .background(Color(0xFF4F4F4F), shape = containerShape)
            .clip(containerShape),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RecordingTimer(timeInSeconds = timeInSeconds)
        Box(
            modifier = Modifier
                .background(Color(0xFF252525))
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            RecordControls(
                isRecording = isRecording,
                onRecordStart = { isRecording = true },
                onStopClick = {
                    isRecording = false
                    timeInSeconds = 0
                }
            )
        }
    }
}

@Composable
fun RecordingTimer(timeInSeconds: Int) {
    val hours = timeInSeconds / 3600
    val minutes = (timeInSeconds % 3600) / 60
    val seconds = timeInSeconds % 60
    val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Text(
        text = formattedTime,
        color = Color.White,
        fontSize = 70.sp,
        modifier = Modifier.padding(vertical = 32.dp)
    )
}

@Composable
fun AudioListItem(record: AudioRecord) {
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
            onClick = {},
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play Audio Log Icon",
                modifier = Modifier.size(30.dp),
                tint = Color.DarkGray
            )
        }
    }
}

@Composable
fun AudioLogs() {
    val containerShape = RoundedCornerShape(12.dp)

    val dummyRecordings = listOf(
        AudioRecord("Journal_Entry_1.mp3", "00:03:12", "May 20, 2026"),
        AudioRecord("Journal_Entry_2.mp3", "00:01:45", "May 21, 2026"),
        AudioRecord("Journal_Entry_3.mp3", "00:00:30", "May 22, 2026")
    )
    LazyColumn() {
        items(dummyRecordings) { record ->
            AudioListItem(record = record)
        }
    }
}

data class AudioRecord(
    val fileName: String,
    val duration: String,
    val date: String
)