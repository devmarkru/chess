package ru.devmark.chess.pieces

import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.Point
import ru.devmark.chess.resolvers.DiagonalTurnResolver
import ru.devmark.chess.resolvers.TurnResolver

data class Bishop(
    override var position: Point,
    override val color: PieceColor,
    override var wasMove: Boolean = false
) : Piece {

    override fun getImageName(): String = "${color.text}-bishop"

    override fun getSpacesForTurn(pieces: Map<Point, Piece>): Set<Point> = RESOLVER.getSpacesForTurn(this, pieces)

    override fun getPrice(): Int = 3

    override fun getLetter(): String = "B"

    private companion object {
        val RESOLVER = DiagonalTurnResolver(8)
    }
}
