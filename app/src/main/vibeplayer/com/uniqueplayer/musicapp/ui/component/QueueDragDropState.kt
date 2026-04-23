package com.uniqueplayer.musicapp.ui.component

class QueueDragDropState(
    private val onMove: (Int, Int) -> Unit
) {
    private var draggingIndex: Int? = null
    private var dragOffsetPx: Float = 0f

    fun startDragAt(offsetY: Float, itemCount: Int) {
        if (itemCount <= 0) {
            draggingIndex = null
            return
        }
        val index = (offsetY / ROW_HEIGHT_PX).toInt().coerceIn(0, itemCount - 1)
        draggingIndex = index
        dragOffsetPx = 0f
    }

    fun updateDrag(deltaY: Float, itemCount: Int) {
        val source = draggingIndex ?: return
        if (itemCount <= 1) {
            return
        }
        dragOffsetPx += deltaY
        val shift = (dragOffsetPx / ROW_HEIGHT_PX).toInt()
        if (shift == 0) {
            return
        }
        val target = (source + shift).coerceIn(0, itemCount - 1)
        if (target != source) {
            onMove(source, target)
            draggingIndex = target
            dragOffsetPx = 0f
        }
    }

    fun endDrag() {
        draggingIndex = null
        dragOffsetPx = 0f
    }

    fun cancelDrag() {
        endDrag()
    }

    private companion object {
        const val ROW_HEIGHT_PX = 64f
    }
}
