package ru.devmark.chess.resolvers

import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Turn

class QueenTurnGenerator : TurnGenerator {

    override fun getTurns(position: Point, pieces: Map<Point, Piece>): Set<Turn> {
        val spaces = mutableSetOf<Turn>()
        spaces.addAll(generateRectangularTurns(position, pieces, MAX_RANGE))
        spaces.addAll(generateDiagonalTurns(position, pieces, MAX_RANGE))
        return spaces
    }

    private companion object {
        const val MAX_RANGE = 8
    }
}