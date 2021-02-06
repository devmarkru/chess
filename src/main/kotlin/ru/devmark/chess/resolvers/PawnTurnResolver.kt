package ru.devmark.chess.resolvers

import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Piece

class PawnTurnResolver : AbstractTurnResolver() {

    override fun getSpacesForTurn(current: Piece, pieces: Map<Point, Piece>): Set<Point> {
        val spaces = mutableSetOf<Point>()
        val directionY = getDirectionY(current)
        val range = if (current.wasMove) 1 else 2
        val position = current.position
        for (i in 1..range) {
            val point = Point(
                position.x, position.y + directionY * i
            )
            if (point in pieces) {
                break
            } else {
                spaces += point
            }
        }
        checkAttackPoint(current, 1, directionY, pieces, spaces)
        checkAttackPoint(current, -1, directionY, pieces, spaces)
        return spaces
            .filter { it.x in 0..7 && it.y in 0..7 }
            .toSet()
    }

    private fun getDirectionY(current: Piece) = when (current.color) {
        PieceColor.WHITE -> 1
        PieceColor.BLACK -> -1
    }

    private fun checkAttackPoint(
        current: Piece,
        deltaX: Int,
        deltaY: Int,
        pieces: Map<Point, Piece>,
        spaces: MutableSet<Point>
    ) {
        val position = current.position
        val attackPoint = Point(position.x + deltaX, position.y + deltaY)
        val attackedPiece = pieces[attackPoint]
        if (attackedPiece != null && attackedPiece.color != current.color) {
            spaces += attackPoint
        }
    }
}