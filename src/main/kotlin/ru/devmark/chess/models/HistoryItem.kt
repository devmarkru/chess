package ru.devmark.chess.models

data class HistoryItem(
    val turnNumber: Int,
    val white: String,
    val black: String? = null
)
