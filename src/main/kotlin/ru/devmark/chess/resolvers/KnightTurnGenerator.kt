package ru.devmark.chess.resolvers

import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Turn

class KnightTurnGenerator : TurnGenerator {

    override fun getTurns(position: Point, pieces: Map<Point, Piece>): Set<Turn> {
        val spaces = mutableSetOf<Turn>()
        spaces.addPointsIfCan(position, pieces, 1, 2)
        spaces.addPointsIfCan(position, pieces, 2, 1)
        return spaces
    }

    private fun MutableSet<Turn>.addPointsIfCan(position: Point, pieces: Map<Point, Piece>, absX: Int, absY: Int) {
        this.addPointIfCan(position, pieces, absX, absY)
        this.addPointIfCan(position, pieces, absX, -absY)
        this.addPointIfCan(position, pieces, -absX, absY)
        this.addPointIfCan(position, pieces, -absX, -absY)
    }
}