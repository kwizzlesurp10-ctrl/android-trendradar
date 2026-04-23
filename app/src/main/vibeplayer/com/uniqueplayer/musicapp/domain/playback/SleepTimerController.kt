package com.uniqueplayer.musicapp.domain.playback

import javax.inject.Inject

class SleepTimerController @Inject constructor() {
    private var startAtMs: Long = 0L
    private var totalDurationMs: Long = 0L
    private var active: Boolean = false

    fun start(durationMs: Long, nowMs: Long = System.currentTimeMillis()) {
        startAtMs = nowMs
        totalDurationMs = durationMs.coerceAtLeast(1L)
        active = true
    }

    fun cancel() {
        active = false
        startAtMs = 0L
        totalDurationMs = 0L
    }

    fun isActive(nowMs: Long = System.currentTimeMillis()): Boolean {
        if (!active) {
            return false
        }
        if (remainingMs(nowMs) <= 0L) {
            active = false
            return false
        }
        return true
    }

    fun remainingMs(nowMs: Long = System.currentTimeMillis()): Long {
        if (!active) {
            return 0L
        }
        val elapsed = (nowMs - startAtMs).coerceAtLeast(0L)
        return (totalDurationMs - elapsed).coerceAtLeast(0L)
    }

    fun progress(nowMs: Long = System.currentTimeMillis()): Float {
        if (!active) {
            return 0f
        }
        val elapsed = (totalDurationMs - remainingMs(nowMs)).toFloat()
        return (elapsed / totalDurationMs.toFloat()).coerceIn(0f, 1f)
    }

    fun volumeForProgress(progress: Float): Float = (1f - progress.coerceIn(0f, 1f)).coerceIn(0f, 1f)
}
