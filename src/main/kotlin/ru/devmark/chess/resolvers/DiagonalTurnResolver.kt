package ru.devmark.chess.resolvers

import ru.devmark.chess.models.Point
import ru.devmark.chess.pieces.Piece

class DiagonalTurnResolver(private val maxRange: Int) : AbstractTurnResolver() {

    override fun getSpacesForTurn(current: Piece, pieces: Map<Point, Piece>): Set<Point> {
        val spaces = mutableSetOf<Point>()
        var leftTop = true
        var rightTop = true
        var leftBottom = true
        var rightBottom = true
        for (i in 1..maxRange) {
            if (leftTop) {
                leftTop = spaces.addPointIfCan(current, pieces, -i, i)
            }
            if (rightTop) {
                rightTop = spaces.addPointIfCan(current, pieces, i, i)
            }
            if (leftBottom) {
                leftBottom = spaces.addPointIfCan(current, pieces, -i, -i)
            }
            if (rightBottom) {
                rightBottom = spaces.addPointIfCan(current, pieces, i, -i)
            }
        }
        return spaces
    }
}