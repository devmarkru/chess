package ru.devmark.chess.resolvers

import ru.devmark.chess.models.Point
import ru.devmark.chess.pieces.Piece

class QueenTurnResolver : AbstractTurnResolver() {

    override fun getSpacesForTurn(current: Piece, pieces: Map<Point, Piece>): Set<Point> {
        val spaces = mutableSetOf<Point>()
        spaces.addAll(RECTANGULAR_RESOLVER.getSpacesForTurn(current, pieces))
        spaces.addAll(DIAGONAL_RESOLVER.getSpacesForTurn(current, pieces))
        return spaces
    }

    private companion object {
        val RECTANGULAR_RESOLVER = RectangularTurnResolver(8)
        val DIAGONAL_RESOLVER = DiagonalTurnResolver(8)
    }
}