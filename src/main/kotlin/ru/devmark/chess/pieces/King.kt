package ru.devmark.chess.pieces

import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.Point
import ru.devmark.chess.resolvers.KingTurnResolver

data class King(
    override var position: Point,
    override val color: PieceColor,
    override var wasMove: Boolean = false
) : Piece {

    override fun getImageName(): String = "${color.text}-king"

    override fun getSpacesForTurn(pieces: Map<Point, Piece>): Set<Point> = RESOLVER.getSpacesForTurn(this, pieces)

    override fun getPrice(): Int = 1_000_000

    override fun getLetter(): String = "K"

    private companion object {
        val RESOLVER = KingTurnResolver()
    }
}