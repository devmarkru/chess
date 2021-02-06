package ru.devmark.chess.pieces

import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.Point
import ru.devmark.chess.resolvers.KnightTurnResolver

data class Knight(
    override var position: Point,
    override val color: PieceColor,
    override var wasMove: Boolean = false
) : Piece {

    override fun getImageName(): String = "${color.text}-knight"

    override fun getSpacesForTurn(pieces: Map<Point, Piece>): Set<Point> = RESOLVER.getSpacesForTurn(this, pieces)

    override fun getPrice(): Int = 3

    override fun getLetter(): String = "N"

    private companion object {
        val RESOLVER = KnightTurnResolver()
    }
}