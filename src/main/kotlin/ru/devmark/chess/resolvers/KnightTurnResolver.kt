package ru.devmark.chess.resolvers

import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Piece

class KnightTurnResolver : AbstractTurnResolver() {

    override fun getSpacesForTurn(current: Piece, pieces: Map<Point, Piece>): Set<Point> {
        val spaces = mutableSetOf<Point>()
        spaces.addPointsIfCan(current, pieces, 1, 2)
        spaces.addPointsIfCan(current, pieces, 2, 1)
        return spaces
    }

    private fun MutableSet<Point>.addPointsIfCan(current: Piece, pieces: Map<Point, Piece>, absX: Int, absY: Int) {
        this.addPointIfCan(current, pieces, absX, absY)
        this.addPointIfCan(current, pieces, absX, -absY)
        this.addPointIfCan(current, pieces, -absX, absY)
        this.addPointIfCan(current, pieces, -absX, -absY)
    }
}