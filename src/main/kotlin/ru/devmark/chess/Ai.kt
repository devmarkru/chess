package ru.devmark.chess

import ru.devmark.chess.engine.Board
import ru.devmark.chess.engine.BoardUtils
import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.PieceType
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.PromotionTurn
import ru.devmark.chess.models.Turn
import ru.devmark.chess.models.TurnProfitInfo

class Ai(private val color: PieceColor) {

    fun nextTurn(board: Board): Pair<Point, Turn> {
        val originalPieces = board.getPieces()
        val profits = board.getTurnsForColor(color)
            .entries.map { (from, turns) ->
                turns.map { turn ->
                    val pieces = HashMap(originalPieces) // todo уменьшить кол-во копирований
                    val toType = if (turn is PromotionTurn) { // todo инкапсулировать
                        turn.toType
                    } else null
                    utils.movePiece(pieces, from, turn.to, toType)
                    val profits = getProfits(pieces)
                    utils.movePiece(pieces, turn.to, from, toType?.let { PieceType.PAWN })
                    TurnProfitInfo(
                        from = from,
                        turn = turn,
                        ownProfit = profits.getValue(color),
                        enemyProfit = profits.getValue(color.other())
                    )
                }
            }
            .flatten()

        val maxOwnProfit = profits.maxOf { it.ownProfit }

        val turnPriceInfo = profits.filter { it.ownProfit == maxOwnProfit }
            .shuffled() // вносим элемент случайности для ходов с одинаковым профитом
            .sortedBy { it.enemyProfit } // у соперника должен быть минимальный профит
            .first()

        return turnPriceInfo.from to turnPriceInfo.turn
    }

    private fun getProfits(pieces: Map<Point, Piece>): Map<PieceColor, Int> {
        val profits = hashMapOf<PieceColor, Int>()
        val spacesUnderAttack = utils.getSpacesUnderAttack(pieces)
        pieces.forEach { (_, piece) ->
            profits.incrementProfit(piece.color, piece.getPrice())
            // если фигура может быть атакована противником, то увеличиваем его профит тоже
            if (piece.position in spacesUnderAttack.getValue(piece.color.other())) {
                profits.incrementProfit(piece.color.other(), piece.getPrice())
            }
        }
        return profits
    }

    private fun MutableMap<PieceColor, Int>.incrementProfit(color: PieceColor, value: Int) {
        this[color] = (this[color] ?: 0) + value
    }

    private fun Piece.getPrice() = this.type.price

    private companion object {
        val utils = BoardUtils()
    }
}