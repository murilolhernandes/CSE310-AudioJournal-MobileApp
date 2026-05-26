package com.murilo.audiojournal

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


@Composable
fun RecordControls(
    isRecording: Boolean,
    onRecordStart: () -> Unit,
    onStopClick: () -> Unit
    ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center

        ) {
            Row() {
                RecordButton(
                    isRecording = isRecording,
                    onRecordStart = onRecordStart
                )
                Spacer(modifier = Modifier.width(80.dp))

                StopButton(
                    onStopClick = onStopClick
                )
            }
        }
    }
}

@Composable
fun RecordButton(isRecording: Boolean, onRecordStart: () -> Unit) {
    val context = LocalContext.current

    val permissionLaucher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Microphone Permission Granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "We need the mic to record audio!", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(120.dp)
    ) {
        IconButton(
            onClick = {
                permissionLaucher.launch(Manifest.permission.RECORD_AUDIO)
                onRecordStart()
            },
            enabled = !isRecording,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Red,
                disabledContentColor = Color(0xFF552222)),
            modifier = Modifier.size(80.dp)
        ) {
           Icon(
               imageVector = Icons.Default.Mic,
               contentDescription = "Microphone Icon",
               modifier = Modifier.size(40.dp),
               tint = if (isRecording) Color.Gray else Color.White
           )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = if (isRecording) "RECORDING" else "RECORD",
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StopButton(onStopClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(120.dp)
    ) {
        IconButton(
            onClick = { onStopClick() },
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Gray),
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Stop,
                contentDescription = "Stop Icon",
                modifier = Modifier.size(40.dp),
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "STOP",
            fontWeight = FontWeight.Bold,
            color = Gray,
            fontSize = 20.sp
        )
    }
}