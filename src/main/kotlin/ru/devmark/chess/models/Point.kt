package ru.devmark.chess.models

data class Point(
    val x: Int,
    val y: Int
) {
    fun notation(): String = "${LETTERS[this.x]}${this.y + 1}"

    private companion object {
        const val LETTERS = "abcdefgh"
    }
}