package ru.devmark.chess.models

interface Turn {
    val sourcePiece: Piece
    val from: Point
    val to: Point
    val enemyPiece: Piece?

    fun execute(pieces: MutableMap<Point, Piece>)
    fun revert(pieces: MutableMap<Point, Piece>)
}
