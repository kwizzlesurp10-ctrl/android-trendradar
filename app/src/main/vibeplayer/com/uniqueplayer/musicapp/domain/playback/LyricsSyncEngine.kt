package com.uniqueplayer.musicapp.domain.playback

import com.uniqueplayer.musicapp.domain.model.LyricLine
import javax.inject.Inject

class LyricsSyncEngine @Inject constructor() {
    fun findCurrentLyric(lines: List<LyricLine>, positionMs: Long): LyricLine? {
        if (lines.isEmpty()) {
            return null
        }
        var low = 0
        var high = lines.lastIndex
        var activeIndex = -1
        while (low <= high) {
            val mid = (low + high) ushr 1
            if (lines[mid].timestampMs <= positionMs) {
                activeIndex = mid
                low = mid + 1
            } else {
                high = mid - 1
            }
        }
        return if (activeIndex == -1) null else lines[activeIndex]
    }
}
