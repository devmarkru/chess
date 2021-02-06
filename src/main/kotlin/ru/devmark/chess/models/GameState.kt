package ru.devmark.chess.models

enum class GameState(val notation: String, val isFinal: Boolean) {
    IN_PROGRESS("", false),
    CHECK("+", false),
    MATE("++", true),
    STALEMATE("=", true)
}