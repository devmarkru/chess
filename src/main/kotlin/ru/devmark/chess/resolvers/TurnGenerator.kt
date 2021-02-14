package ru.devmark.chess.resolvers

import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Turn

interface TurnGenerator {

    fun getTurns(position: Point, pieces: Map<Point, Piece>): Set<Turn>
}