package ru.devmark.chess.resolvers

import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Piece

abstract class AbstractTurnResolver : TurnResolver {

    protected fun MutableSet<Point>.addPointIfCan(
        current: Piece,
        pieces: Map<Point, Piece>,
        deltaX: Int,
        deltaY: Int
    ): Boolean {
        val position = current.position
        val point = Point(position.x + deltaX, position.y + deltaY)
        val otherPiece = pieces[point]
        return point.takeIf { it.x in 0..7 && it.y in 0..7 }
            ?.let {
                when {
                    otherPiece == null -> { // нет фигуры
                        this += point
                        true
                    }
                    otherPiece.color != current.color -> { // вражеская фигура
                        this += point
                        false
                    }
                    else -> { // своя фигура
                        false
                    }
                }
            } ?: false
    }
}