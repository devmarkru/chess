package ru.devmark.chess.resolvers

import ru.devmark.chess.models.NormalTurn
import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.PieceType
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.PromotionTurn
import ru.devmark.chess.models.Turn

class PawnTurnGenerator : TurnGenerator {

    override fun getTurns(current: Piece, pieces: Map<Point, Piece>): Set<Turn> {
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
        val promotionY = getPromotionY(current.color)
        return spaces
            .filter { it.x in 0..7 && it.y in 0..7 }
            .map {
                if (it.y == promotionY) {
                    // достигнув края доски, пешка превращается в одну из четырёх фигур
                    listOf(
                        PromotionTurn(to = it, toType = PieceType.KNIGHT),
                        PromotionTurn(to = it, toType = PieceType.BISHOP),
                        PromotionTurn(to = it, toType = PieceType.ROOK),
                        PromotionTurn(to = it, toType = PieceType.QUEEN)
                    )
                } else {
                    listOf(NormalTurn(to = it))
                }
            }
            .flatten()
            .toSet()
    }

    private fun getDirectionY(current: Piece) = when (current.color) {
        PieceColor.WHITE -> 1
        PieceColor.BLACK -> -1
    }

    private fun getPromotionY(color: PieceColor) = when (color) {
        PieceColor.WHITE -> 7
        PieceColor.BLACK -> 0
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