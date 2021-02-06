package ru.devmark.chess.pieces

import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.Point
import ru.devmark.chess.resolvers.QueenTurnResolver

data class Queen(
    override var position: Point,
    override val color: PieceColor,
    override var wasMove: Boolean = false
) : Piece {

    override fun getImageName(): String = "${color.text}-queen"

    override fun getSpacesForTurn(pieces: Map<Point, Piece>): Set<Point> = RESOLVER.getSpacesForTurn(this, pieces)

    override fun getPrice(): Int = 9

    override fun getLetter(): String = "Q"

    private companion object {
        val RESOLVER = QueenTurnResolver()
    }
}
