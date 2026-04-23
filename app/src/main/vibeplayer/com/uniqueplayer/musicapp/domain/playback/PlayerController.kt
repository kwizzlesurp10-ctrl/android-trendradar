package com.uniqueplayer.musicapp.domain.playback

import com.uniqueplayer.musicapp.domain.model.PlaybackMode
import kotlinx.coroutines.flow.StateFlow

interface PlayerController {
    fun observeSnapshot(): StateFlow<PlaybackSnapshot>
    fun setQueue(trackIds: List<Long>)
    fun playTrack(trackId: Long)
    fun togglePlayPause()
    fun pause()
    fun skipToNext()
    fun skipToPrevious()
    fun refreshPosition()
    fun setVolume(volume: Float)
    fun updateMode(mode: PlaybackMode)
    fun setEqualizer(settings: EqualizerSettings)
    fun release()
}
