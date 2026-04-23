package com.uniqueplayer.musicapp.domain.playback

import com.uniqueplayer.musicapp.domain.model.MusicTrack
import javax.inject.Inject

class QueueReorderUseCase @Inject constructor() {
    fun reorder(queue: List<MusicTrack>, fromIndex: Int, toIndex: Int): List<MusicTrack> {
        if (fromIndex == toIndex || queue.isEmpty()) {
            return queue
        }
        if (fromIndex !in queue.indices || toIndex !in queue.indices) {
            return queue
        }
        val mutable = queue.toMutableList()
        val item = mutable.removeAt(fromIndex)
        mutable.add(toIndex, item)
        return mutable.toList()
    }
}
