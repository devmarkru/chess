package ru.devmark.chess.resolvers

import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Turn

class KnightTurnGenerator : TurnGenerator {

    override fun getTurns(current: Piece, pieces: Map<Point, Piece>): Set<Turn> {
        val spaces = mutableSetOf<Turn>()
        spaces.addPointsIfCan(current, pieces, 1, 2)
        spaces.addPointsIfCan(current, pieces, 2, 1)
        return spaces
    }

    private fun MutableSet<Turn>.addPointsIfCan(current: Piece, pieces: Map<Point, Piece>, absX: Int, absY: Int) {
        this.addPointIfCan(current, pieces, absX, absY)
        this.addPointIfCan(current, pieces, absX, -absY)
        this.addPointIfCan(current, pieces, -absX, absY)
        this.addPointIfCan(current, pieces, -absX, -absY)
    }
}