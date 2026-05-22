package com.murilo.audiojournal

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

@Composable
fun RecordButton() {
    Row(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = { /* API logic here */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
        ) {
            Text(text = "RECORD")
        }
    }
}