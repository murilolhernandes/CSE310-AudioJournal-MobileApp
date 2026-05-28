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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * A layout container that aligns the Record and Stop buttons side-by-side.
 * @param isRecording Boolean indicating if the app is actively recording.
 * @param onRecordStart Callback triggered when the record button is successfully engaged.
 * @param onStopClick Callback triggered when the stop button is clicked.
 */
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

/**
 * A UI button that handles Android runtime permissions for the microphone.
 * Only triggers the recording sequence if permission is explicitly granted by the user.
 * @param isRecording Boolean indicating if a recording is active (disables the button if true).
 * @param onRecordStart Callback triggered after permissions are verified and the user intends to record.
 */
@Composable
fun RecordButton(isRecording: Boolean, onRecordStart: () -> Unit) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            onRecordStart()
        } else {
            Toast.makeText(context, "Microphone access is required to use the journal.", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(120.dp)
    ) {
        IconButton(
            onClick = {
                val permissionStatus = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                )

                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    onRecordStart()
                } else {
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
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

/**
 * A simple UI button designed to trigger the stop recording sequence.
 * @param onStopClick Callback triggered when the button is clicked.
 */
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