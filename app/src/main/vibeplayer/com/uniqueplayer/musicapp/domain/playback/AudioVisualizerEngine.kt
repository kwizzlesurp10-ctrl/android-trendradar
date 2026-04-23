package com.uniqueplayer.musicapp.domain.playback

import javax.inject.Inject
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class AudioVisualizerEngine @Inject constructor() {
    fun generateFrame(positionMs: Long, playing: Boolean, samples: Int = SAMPLE_COUNT): List<Float> {
        if (!playing) {
            return List(samples) { 0f }
        }
        val phase = positionMs / 1000f
        return List(samples) { index ->
            val x = index.toFloat() / samples
            val harmonic = sin((2f * PI * (x + phase)).toFloat())
            val overtone = cos((4f * PI * (x + phase * 0.5f)).toFloat())
            ((abs(harmonic) * 0.7f) + (abs(overtone) * 0.3f)).coerceIn(0f, 1f)
        }
    }

    companion object {
        private const val SAMPLE_COUNT = 64
    }
}
