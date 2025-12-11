package com.jherkenhoff.qalculate.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class UndoState<T> (
    val undoStack: ArrayDeque<T> = ArrayDeque<T>(),
    val redoStack: ArrayDeque<T> = ArrayDeque<T>(),
) {
    val canUndo get() = undoStack.isNotEmpty()
    val canRedo get() = redoStack.isNotEmpty()
}

class UndoManager<T : Any>(
    private val groupingWindowMs: Long = 400L
) {

    private val _state = MutableStateFlow(UndoState<T>())
    val state: StateFlow<UndoState<T>> = _state

    private var pendingSnapshot: T? = null
    private var lastSnapshotTime = 0L

    /** Must be called before applying the actual state change */
    fun snapshot(beforeChange: T) {
        val now = System.currentTimeMillis()

        // If this starts a new group
        if (now - lastSnapshotTime > groupingWindowMs) {
            // push previous pending snapshot
            pendingSnapshot?.let { _state.value.undoStack.addLast(it) }
            pendingSnapshot = beforeChange
        }

        // If still within grouping, keep earliest snapshot in the group
        if (pendingSnapshot == null) {
            pendingSnapshot = beforeChange
        }

        lastSnapshotTime = now
        _state.value.redoStack.clear() // new edits kill redo history
    }

    fun undo(currentState: T): T? {
        // Finalize pending snapshot into undo history
        pendingSnapshot?.let {
            _state.value.undoStack.addLast(it)
            pendingSnapshot = null
        }

        if (!_state.value.canUndo) return null

        // Push current to redo
        _state.value.redoStack.addLast(currentState)
        return _state.value.undoStack.removeLast()
    }

    fun redo(currentState: T): T? {
        if (!_state.value.canRedo) return null

        // Push current to undo
        _state.value.undoStack.addLast(currentState)
        return _state.value.redoStack.removeLast()
    }

    fun clear() {
        _state.value.undoStack.clear()
        _state.value.redoStack.clear()
        pendingSnapshot = null
        lastSnapshotTime = 0
    }
}