package ru.devmark.chess.pieces

import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.Point

interface Piece {
    val color: PieceColor
    var position: Point
    var wasMove: Boolean

    // todo сделать один data-класс для всех фигур, убрав из него все методы
    fun getImageName(): String
    fun getPrice(): Int
    fun getLetter(): String

    fun getSpacesForTurn(pieces: Map<Point, Piece>): Set<Point>
}
