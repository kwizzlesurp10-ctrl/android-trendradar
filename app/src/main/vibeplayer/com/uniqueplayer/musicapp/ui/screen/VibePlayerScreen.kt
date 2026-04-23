package com.uniqueplayer.musicapp.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uniqueplayer.musicapp.domain.model.MusicTrack
import com.uniqueplayer.musicapp.ui.component.QueueDragDropState
import com.uniqueplayer.musicapp.ui.model.VibePlayerUiState
import kotlin.math.min

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VibePlayerScreen(
    uiState: VibePlayerUiState,
    onPlayPause: () -> Unit,
    onSelectTrack: (MusicTrack) -> Unit,
    onSwipeNext: () -> Unit,
    onSwipePrevious: () -> Unit,
    onDoubleTap: () -> Unit,
    onCreateSmartPlaylist: () -> Unit,
    onReorderQueue: (Int, Int) -> Unit,
    onSetEqualizerBand: (Int, Float) -> Unit,
    onSetSleepTimerMinutes: (Int) -> Unit,
    onCancelSleepTimer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dragDropState = QueueDragDropState(onMove = onReorderQueue)
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("VibePlayer", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(
            text = "Material You • ExoPlayer • Offline-first",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        PlayerGestureCard(
            uiState = uiState,
            onDoubleTap = onDoubleTap,
            onSwipeNext = onSwipeNext,
            onSwipePrevious = onSwipePrevious
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onPlayPause) {
                Text(if (uiState.isPlaying) "Pause" else "Play")
            }
            OutlinedButton(onClick = onCreateSmartPlaylist) {
                Text("Smart Playlist")
            }
        }
        QueueCard(
            uiState = uiState,
            dragDropState = dragDropState,
            onTrackTapped = onSelectTrack
        )
        if (uiState.recommendedTracks.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Recommendations", style = MaterialTheme.typography.titleMedium)
                    uiState.recommendedTracks.take(3).forEach { track ->
                        Text(
                            text = "• ${track.title} - ${track.artist}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
        LyricsSleepCard(
            uiState = uiState,
            onSleepTimerMinutes = onSetSleepTimerMinutes,
            onCancelSleepTimer = onCancelSleepTimer
        )
        EqualizerCard(bands = uiState.equalizerBandGains, onBandChanged = onSetEqualizerBand)
        VisualizationCard(samples = uiState.visualizerSamples)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayerGestureCard(
    uiState: VibePlayerUiState,
    onDoubleTap: () -> Unit,
    onSwipeNext: () -> Unit,
    onSwipePrevious: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > 40f) onSwipePrevious()
                    if (dragAmount < -40f) onSwipeNext()
                }
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(uiState.currentTrack?.title ?: "No track selected", style = MaterialTheme.typography.titleMedium)
                Text(
                    uiState.currentTrack?.artist ?: "Swipe for next/previous, double tap play/pause",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), CircleShape)
                    .combinedClickable(onClick = {}, onDoubleClick = onDoubleTap),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (uiState.isPlaying) Icons.Default.PauseCircleFilled else Icons.Default.PlayCircleFilled,
                    contentDescription = "Play/Pause",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(38.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun QueueCard(
    uiState: VibePlayerUiState,
    dragDropState: QueueDragDropState,
    onTrackTapped: (MusicTrack) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.QueueMusic, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Queue", style = MaterialTheme.typography.titleMedium)
            }
            LazyColumn(
                modifier = Modifier
                    .height(170.dp)
                    .pointerInput(uiState.queue) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { offset -> dragDropState.startDragAt(offset.y, uiState.queue.size) },
                            onDrag = { _, dragAmount -> dragDropState.updateDrag(dragAmount.y, uiState.queue.size) },
                            onDragEnd = dragDropState::endDrag,
                            onDragCancel = dragDropState::cancelDrag
                        )
                    },
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                itemsIndexed(uiState.queue, key = { _, track -> track.id }) { index, track ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = if (uiState.currentTrack?.id == track.id) {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                },
                                shape = RoundedCornerShape(10.dp)
                            )
                            .combinedClickable(onClick = { onTrackTapped(track) }, onLongClick = {}),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${index + 1}",
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                                .width(18.dp),
                            textAlign = TextAlign.End
                        )
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(track.title, maxLines = 1)
                            Text(
                                "${track.artist} • ${track.format}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LyricsSleepCard(
    uiState: VibePlayerUiState,
    onSleepTimerMinutes: (Int) -> Unit,
    onCancelSleepTimer: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Synced Lyrics + Sleep Timer", style = MaterialTheme.typography.titleMedium)
            Text(
                text = uiState.activeLyricLine?.text ?: "No synced lyric line yet",
                color = MaterialTheme.colorScheme.primary
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Sleep timer")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = uiState.sleepTimerEnabled,
                    onCheckedChange = { enabled ->
                        if (!enabled) {
                            onCancelSleepTimer()
                        } else {
                            onSleepTimerMinutes(uiState.sleepTimerMinutes)
                        }
                    }
                )
            }
            Slider(
                value = uiState.sleepTimerMinutes.toFloat(),
                onValueChange = { onSleepTimerMinutes(it.toInt()) },
                valueRange = 5f..90f,
                steps = 16
            )
            Text(
                text = uiState.sleepTimerLabel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EqualizerCard(
    bands: List<Float>,
    onBandChanged: (Int, Float) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("10-band Equalizer", style = MaterialTheme.typography.titleMedium)
            bands.forEachIndexed { index, gain ->
                Column {
                    Text("Band ${index + 1}: ${(gain * 10).toInt()} dB", style = MaterialTheme.typography.bodySmall)
                    Slider(
                        value = gain,
                        onValueChange = { onBandChanged(index, it) },
                        valueRange = -1f..1f
                    )
                }
            }
        }
    }
}

@Composable
private fun VisualizationCard(samples: List<Float>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text("Waveform + Spectrum", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Canvas(modifier = Modifier.fillMaxSize()) {
                if (samples.isEmpty()) return@Canvas
                val centerY = size.height / 2f
                val spacing = size.width / samples.size
                for (index in samples.indices) {
                    val amplitude = samples[index].coerceIn(0f, 1f)
                    val x = index * spacing
                    val barHeight = amplitude * centerY
                    drawLine(
                        color = androidx.compose.ui.graphics.Color(0xFF6750A4),
                        start = Offset(x, centerY - barHeight),
                        end = Offset(x, centerY + barHeight),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round
                    )
                }
                val radius = min(size.width, size.height) * 0.2f
                val center = Offset(size.width * 0.86f, size.height * 0.5f)
                drawCircle(
                    color = androidx.compose.ui.graphics.Color(0xFF7D5260).copy(alpha = 0.25f),
                    center = center,
                    radius = radius
                )
            }
        }
    }
}
