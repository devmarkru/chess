package ru.devmark.chess.resolvers

import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Turn

class BishopTurnGenerator : TurnGenerator {

    override fun getTurns(position: Point, pieces: Map<Point, Piece>): Set<Turn> =
        generateDiagonalTurns(position, pieces, MAX_RANGE)

    private companion object {
        const val MAX_RANGE = 8
    }
}