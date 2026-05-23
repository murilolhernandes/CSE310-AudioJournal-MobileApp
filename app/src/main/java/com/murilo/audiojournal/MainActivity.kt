package com.murilo.audiojournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.murilo.audiojournal.ui.theme.AudioJournalTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape

// import androidx.compose.foundation.layout.Row

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
    Column(
        modifier
            .background(Color(0xFF4F4F4F), shape = containerShape)
            .clip(containerShape),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "00:00:00",
            color = Color.White,
            fontSize = 60.sp,
            modifier = Modifier.padding(vertical = 32.dp)
        )
        Box(
            modifier = Modifier
                //.fillMaxWidth()
                .background(Color(0xFF252525))
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            RecordButton()
        }
    }
}
