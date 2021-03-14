package ru.devmark.chess.resolvers

import ru.devmark.chess.models.NormalTurn
import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.PieceType
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.PromotionTurn
import ru.devmark.chess.models.Turn

class PawnTurnGenerator : TurnGenerator {

    override fun getTurns(position: Point, pieces: Map<Point, Piece>): Set<Turn> {
        val turns = mutableSetOf<Turn>()
        val current = pieces.getValue(position)
        // первый ход пешка может сделать на 2 клетки вперёд
        val range = if (position.y == getStartY(current.color)) 2 else 1
        val from = position
        val directionY = getDirectionY(current.color)
        for (i in 1..range) {
            val to = Point(from.x, from.y + directionY * i)
            if (to in pieces) {
                break
            } else {
                turns.addTurnWithPromotionCheck(position, current, to, null)
            }
        }
        addAttackPoint(position, current, 1, directionY, pieces, turns)
        addAttackPoint(position, current, -1, directionY, pieces, turns)

        return turns
            .filter { it.to.x in 0..7 && it.to.y in 0..7 }
            .toSet()
    }

    private fun getStartY(color: PieceColor) = when (color) {
        PieceColor.WHITE -> 1
        PieceColor.BLACK -> 6
    }

    private fun getDirectionY(color: PieceColor) = when (color) {
        PieceColor.WHITE -> 1
        PieceColor.BLACK -> -1
    }

    private fun getPromotionY(color: PieceColor) = when (color) {
        PieceColor.WHITE -> 7
        PieceColor.BLACK -> 0
    }

    private fun addAttackPoint(
        position: Point,
        current: Piece,
        deltaX: Int,
        deltaY: Int,
        pieces: Map<Point, Piece>,
        turns: MutableSet<Turn>
    ) {
        val attackPoint = Point(position.x + deltaX, position.y + deltaY)
        val enemyPiece = pieces[attackPoint]
        if (enemyPiece != null && enemyPiece.color != current.color) {
            turns.addTurnWithPromotionCheck(position, current, attackPoint, enemyPiece)
        }
    }

    private fun MutableSet<Turn>.addTurnWithPromotionCheck(
        position: Point,
        current: Piece,
        to: Point,
        enemyPiece: Piece?
    ) {
        val promotionY = getPromotionY(current.color)
        val from = position
        if (to.y == promotionY) {
            this += PieceType.values().filter { it.useForPromotion }
                .map { toType ->
                    PromotionTurn(
                        sourcePiece = current,
                        from = from,
                        to = to,
                        enemyPiece = enemyPiece,
                        toType = toType
                    )
                }
        } else {
            this += NormalTurn(sourcePiece = current, from = from, to = to, enemyPiece = enemyPiece)
        }
    }
}