package com.uniqueplayer.musicapp.domain.playback

import com.google.common.truth.Truth.assertThat
import com.uniqueplayer.musicapp.domain.model.LyricLine
import com.uniqueplayer.musicapp.domain.model.MusicTrack
import org.junit.Test

class PlaybackDomainTest {
    @Test
    fun `lyrics sync returns active lyric line`() {
        val lyrics = listOf(
            LyricLine(0, "a"),
            LyricLine(5_000, "b"),
            LyricLine(10_000, "c")
        )
        val engine = LyricsSyncEngine()

        assertThat(engine.findCurrentLyric(lyrics, 0)?.text).isEqualTo("a")
        assertThat(engine.findCurrentLyric(lyrics, 8_000)?.text).isEqualTo("b")
        assertThat(engine.findCurrentLyric(lyrics, 11_000)?.text).isEqualTo("c")
    }

    @Test
    fun `queue reorder moves item correctly`() {
        val tracks = listOf(
            demoTrack(1L),
            demoTrack(2L),
            demoTrack(3L)
        )
        val useCase = QueueReorderUseCase()

        val reordered = useCase.reorder(tracks, fromIndex = 0, toIndex = 2)

        assertThat(reordered.map { it.id }).containsExactly(2L, 3L, 1L).inOrder()
    }

    @Test
    fun `equalizer clamps gains to valid range`() {
        val settings = EqualizerSettings.default()

        val updated = settings.withBandValue(0, 5f)

        assertThat(updated.bandGains[0]).isEqualTo(1f)
        assertThat(updated.bandGains.size).isEqualTo(10)
    }

    @Test
    fun `visualizer generates deterministic sample size`() {
        val engine = AudioVisualizerEngine()

        val frame = engine.generateFrame(positionMs = 1_000L, playing = true)

        assertThat(frame.size).isEqualTo(64)
    }

    @Test
    fun `sleep timer fades volume down`() {
        val controller = SleepTimerController()

        val startVolume = controller.volumeForProgress(0f)
        val endVolume = controller.volumeForProgress(1f)

        assertThat(startVolume).isEqualTo(1f)
        assertThat(endVolume).isEqualTo(0f)
    }

    private fun demoTrack(id: Long): MusicTrack =
        MusicTrack(
            id = id,
            title = "Track $id",
            artist = "Artist",
            album = "Album",
            uri = "content://media/external/audio/media/$id",
            durationMs = 1000L,
            format = "audio/mpeg"
        )
}
