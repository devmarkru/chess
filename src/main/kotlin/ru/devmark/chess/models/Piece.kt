package ru.devmark.chess.models

data class Piece(
    val type: PieceType,
    var position: Point,
    val color: PieceColor,
    var wasMove: Boolean = false
)
