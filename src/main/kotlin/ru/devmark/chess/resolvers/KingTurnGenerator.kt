package ru.devmark.chess.resolvers

import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Turn

class KingTurnGenerator : TurnGenerator {

    override fun getTurns(current: Piece, pieces: Map<Point, Piece>): Set<Turn> {
        val spaces = mutableSetOf<Turn>()
        spaces.addAll(generateRectangularTurns(current, pieces, MAX_RANGE))
        spaces.addAll(generateDiagonalTurns(current, pieces, MAX_RANGE))
        return spaces
    }

    private companion object {
        const val MAX_RANGE = 1
    }
}