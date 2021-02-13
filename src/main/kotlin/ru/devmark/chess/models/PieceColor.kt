package ru.devmark.chess.models

enum class PieceColor(val text: String) {
    WHITE("white"),
    BLACK("black");

    fun other(): PieceColor =
        when (this) {
            WHITE -> BLACK
            BLACK -> WHITE
        }
}
