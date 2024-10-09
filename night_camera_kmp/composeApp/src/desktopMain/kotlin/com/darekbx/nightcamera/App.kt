package com.darekbx.nightcamera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darekbx.nightcamera.commands.Caller
import com.darekbx.nightcamera.commands.RotateLeft
import com.darekbx.nightcamera.commands.RotateRight
import com.darekbx.nightcamera.commands.TakePhoto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var inProgress by remember { mutableStateOf(false) }
        var statusCode by remember { mutableIntStateOf(-1) }
        var image by remember { mutableStateOf<ImageBitmap?>(null) }
        var exposureTime by remember { mutableIntStateOf(1) }

        Box(Modifier.fillMaxSize()) {

            Box(
                Modifier.fillMaxSize()
                    .padding(start = 64.dp, end = 64.dp, top = 8.dp, bottom = 96.dp)
                    .background(Color.LightGray)
            ) {
                image?.let {
                    Image(
                        bitmap = it,
                        contentDescription = null,
                        contentScale = ContentScale.Inside,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Row(Modifier.align(Alignment.BottomCenter).padding(32.dp)) {
                Button(
                    onClick = {
                        inProgress = true
                        CoroutineScope(Dispatchers.IO).launch {
                            statusCode = RotateLeft().invoke()
                            inProgress = false
                        }
                    },
                    enabled = !inProgress,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }
                Button(
                    onClick = {
                        inProgress = true
                        CoroutineScope(Dispatchers.IO).launch {
                            image = TakePhoto().invoke(exposureTime = exposureTime)
                            inProgress = false
                        }
                    },
                    enabled = !inProgress,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Take photo")
                }

                TextField(
                    modifier = Modifier.width(130.dp),
                    value = "$exposureTime",
                    onValueChange = {
                        exposureTime = it.toIntOrNull() ?: 1
                    }
                )

                Button(
                    onClick = {
                        inProgress = true
                        CoroutineScope(Dispatchers.IO).launch {
                            statusCode = RotateRight().invoke()
                            inProgress = false
                        }
                    },
                    enabled = !inProgress,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .align(Alignment.BottomCenter)
            ) {
                Divider(modifier = Modifier.fillMaxWidth())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 8.dp),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        text = Caller.SERVER_ADDRESS
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (statusCode != -1) {
                            Text(
                                modifier = Modifier.padding(
                                    start = 4.dp,
                                    end = 4.dp,
                                    bottom = 8.dp
                                ),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                text = "$statusCode"
                            )
                        }
                        if (inProgress) {
                            CircularProgressIndicator(
                                Modifier.padding(end = 8.dp, start = 4.dp).size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}