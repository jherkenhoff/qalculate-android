package com.jherkenhoff.qalculate.model

class UndoManager<T : Any>(
    private val groupingWindowMs: Long = 400L
) {

    private val undoStack = ArrayDeque<T>()
    private val redoStack = ArrayDeque<T>()

    private var pendingSnapshot: T? = null
    private var lastSnapshotTime = 0L

    /** Must be called before applying the actual state change */
    fun snapshot(beforeChange: T) {
        val now = System.currentTimeMillis()

        // If this starts a new group
        if (now - lastSnapshotTime > groupingWindowMs) {
            // push previous pending snapshot
            pendingSnapshot?.let { undoStack.addLast(it) }
            pendingSnapshot = beforeChange
        }

        // If still within grouping, keep earliest snapshot in the group
        if (pendingSnapshot == null) {
            pendingSnapshot = beforeChange
        }

        lastSnapshotTime = now
        redoStack.clear() // new edits kill redo history
    }

    fun undo(currentState: T): T? {
        // Finalize pending snapshot into undo history
        pendingSnapshot?.let {
            undoStack.addLast(it)
            pendingSnapshot = null
        }

        if (undoStack.isEmpty()) return null

        // Push current to redo
        redoStack.addLast(currentState)
        return undoStack.removeLast()
    }

    fun redo(currentState: T): T? {
        if (redoStack.isEmpty()) return null

        // Push current to undo
        undoStack.addLast(currentState)
        return redoStack.removeLast()
    }

    fun clear() {
        undoStack.clear()
        redoStack.clear()
        pendingSnapshot = null
        lastSnapshotTime = 0
    }
}