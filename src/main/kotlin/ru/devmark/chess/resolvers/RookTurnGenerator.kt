package ru.devmark.chess.resolvers

import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Turn

class RookTurnGenerator : TurnGenerator {

    override fun getTurns(current: Piece, pieces: Map<Point, Piece>): Set<Turn> =
        generateRectangularTurns(current, pieces, MAX_RANGE)

    private companion object {
        const val MAX_RANGE = 8
    }
}