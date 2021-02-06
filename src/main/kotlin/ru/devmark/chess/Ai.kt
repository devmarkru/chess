package ru.devmark.chess

import ru.devmark.chess.engine.Board
import ru.devmark.chess.engine.BoardUtils
import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.TurnInfo
import ru.devmark.chess.models.Piece

// todo поправить кейс, когда король под шахом, но другая фигура может его защитить
class Ai(val color: PieceColor) {

    fun nextTurn(board: Board): TurnInfo {
        val pieces = board.getPieces()
        val spacesUnderAttack = utils.getSpacesUnderAttack(pieces).getValue(color.toggle())
        return rescue(board, pieces, spacesUnderAttack)
            ?: attack(board, pieces, spacesUnderAttack)
    }

    private fun rescue(board: Board, pieces: Map<Point, Piece>, spacesUnderAttack: Set<Point>): TurnInfo? {
        val aiPieces = pieces.values.filter { it.color == color }

        return aiPieces.filter { it.position in spacesUnderAttack }
            // из тех фигур, что могут быть атакованы, выбираем наиболее ценную
            .sortedByDescending { it.getPrice() }
            // среди доступных ей ходов выбираем ту клетку, которая сейчас не атакована
            .map { it to board.getSpacesForTurn(it).firstOrNull { space -> space !in spacesUnderAttack } }
            .firstOrNull { it.second != null }
            ?.let {
                TurnInfo(
                    from = it.first.position,
                    to = it.second!!,
                    profit = 0
                )
            }
    }

    private fun attack(board: Board, pieces: Map<Point, Piece>, spacesUnderAttack: Set<Point>): TurnInfo {
        val aiPieces = pieces.values.filter { it.color == color }
        val availableTurns = mutableSetOf<TurnInfo>()

        aiPieces.forEach { piece ->
            // для каждой своей фигуры ищем все доступные ходы
            availableTurns += board.getSpacesForTurn(piece)
                // исключаем клетки, которые могут быть атакованы соперником в следующем ходу
                .filter { space -> space !in spacesUnderAttack }
                .map { space ->
                    val enemyPiece = pieces[space]
                    TurnInfo(
                        from = piece.position,
                        to = space,
                        profit = if (enemyPiece != null && enemyPiece.color != color) {
                            enemyPiece.getPrice()
                        } else {
                            0
                        }
                    )
                }
        }
        // среди доступных ходов ищем максимальный профит
        val maxProfit = availableTurns.map { it.profit }.maxOrNull() ?: 0
        // если есть несколько ходов с одинаковым профитом, то выбор среди них делаем случайно
        return availableTurns.filter { it.profit == maxProfit }.shuffled().first()
    }

    private fun Piece.getPrice() = this.type.price

    private companion object {
        val utils = BoardUtils()
    }
}