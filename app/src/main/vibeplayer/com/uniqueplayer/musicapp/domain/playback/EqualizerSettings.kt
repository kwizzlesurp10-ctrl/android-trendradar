package com.uniqueplayer.musicapp.domain.playback

data class EqualizerSettings(
    val bandGains: List<Float>
) {
    fun withBandValue(index: Int, value: Float): EqualizerSettings {
        if (index !in bandGains.indices) {
            return this
        }
        val next = bandGains.toMutableList()
        next[index] = value.coerceIn(-1f, 1f)
        return copy(bandGains = next)
    }

    companion object {
        fun default(): EqualizerSettings = EqualizerSettings(List(10) { 0f })
    }
}
