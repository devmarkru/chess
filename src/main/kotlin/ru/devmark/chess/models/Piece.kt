package ru.devmark.chess.models

data class Piece(
    var type: PieceType,
    var position: Point,
    val color: PieceColor,
    var wasMove: Boolean = false
)
