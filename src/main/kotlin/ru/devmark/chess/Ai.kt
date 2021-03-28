package ru.devmark.chess

import ru.devmark.chess.engine.Board
import ru.devmark.chess.engine.impl.BoardUtils
import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Turn
import ru.devmark.chess.models.TurnProfitInfo

class Ai(private val color: PieceColor) {

    fun nextTurn(board: Board): Turn {
        val pieces = HashMap(board.getPieces())
        val currentSpacesUnderEnemyAttack = utils.getSpacesUnderAttack(pieces)
            .getValue(color.other())
        val profits = board.getTurnsForColor(color)
            .entries.map { (from, turns) ->
                turns.map { turn ->
                    TurnProfitInfo(
                        from = from,
                        turn = turn,
                        profit = turn.getProfit(pieces, currentSpacesUnderEnemyAttack)
                    )
                }
            }
            .flatten()

        val maxOwnProfit = profits.maxOf { it.profit }

        val turnPriceInfo = profits.filter { it.profit == maxOwnProfit }
            .shuffled() // вносим элемент случайности для ходов с одинаковым профитом
            .first()

        return turnPriceInfo.turn
    }

    private fun Turn.getProfit(
        pieces: HashMap<Point, Piece>,
        currentSpacesUnderEnemyAttack: Set<Point>
    ): Int {
        var profit = 0
        // если в результате хода уничтожаем вражескую фигуру,
        // то прибавляем её стоимость к профиту
        this.enemyPiece?.let { profit += it.getPrice() }
        // делаем пробный ход
        this.execute(pieces)
        // определяем атакуемые клетки после выполнения хода
        val newSpacesUnderEnemyAttack = utils.getSpacesUnderAttack(pieces)
            .getValue(color.other())
        if (this.from in currentSpacesUnderEnemyAttack && this.to !in newSpacesUnderEnemyAttack) {
            // если в результате хода уводим фигуру из под атаки,
            // то прибавляем её стоимость к профиту
            profit += this.sourcePiece.getPrice()
        } else if (this.from !in currentSpacesUnderEnemyAttack && this.to in newSpacesUnderEnemyAttack) {
            // если фигура после совершения хода попадает под атаку,
            // то вычитаем её стоимость из профита
            profit -= this.sourcePiece.getPrice()
        }
        // отменяем пробный ход
        this.revert(pieces)
        return profit
    }

    private fun Piece.getPrice() = this.type.price

    private companion object {
        val utils = BoardUtils()
    }
}