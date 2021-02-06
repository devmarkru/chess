package ru.devmark.chess.resolvers

import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Piece

class RectangularTurnResolver(private val maxRange: Int) : AbstractTurnResolver() {

    override fun getSpacesForTurn(current: Piece, pieces: Map<Point, Piece>): Set<Point> {
        val spaces = mutableSetOf<Point>()
        var left = true
        var right = true
        var top = true
        var bottom = true
        for (i in 1..maxRange) {
            if (left) {
                left = spaces.addPointIfCan(current, pieces, -i, 0)
            }
            if (right) {
                right = spaces.addPointIfCan(current, pieces, i, 0)
            }
            if (top) {
                top = spaces.addPointIfCan(current, pieces, 0, i)
            }
            if (bottom) {
                bottom = spaces.addPointIfCan(current, pieces, 0, -i)
            }
        }
        return spaces
    }
}