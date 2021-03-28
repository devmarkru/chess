package ru.devmark.chess.engine.impl

import ru.devmark.chess.engine.Board
import ru.devmark.chess.models.GameState
import ru.devmark.chess.models.HistoryItem
import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Turn

class BoardImpl : Board {
    private val pieces = mutableMapOf<Point, Piece>()
    private val history = mutableListOf<HistoryItem>()

    override fun getPieces(): Map<Point, Piece> = pieces
    override fun getHistory(): List<HistoryItem> = history

    override fun load(initialPieces: Map<Point, Piece>) {
        pieces.putAll(initialPieces)
    }

    /**
     * для указанной фигуры возвращает все доступные для неё клетки
     * если какой-то ход ставит короля под удар, то ход исключается из результата
     */
    override fun getTurnsForPiece(position: Point): Set<Turn> {
        val turns = UTILS.getTurnsForPiece(position, pieces)
        val result = mutableSetOf<Turn>()
        val kingColor = pieces.getValue(position).color

        // нужно исключить каждый ход фигуры, который ставит её короля под удар
        turns.forEach { turn ->
            turn.execute(pieces)
            if (!UTILS.isKingUnderAttack(pieces, kingColor)) {
                result += turn
            }
            turn.revert(pieces)
        }
        return result
    }

    override fun getTurnsForColor(color: PieceColor): Map<Point, Set<Turn>> =
        pieces.filter { it.value.color == color }
            .map { it.key to getTurnsForPiece(it.key) }
            .filter { it.second.isNotEmpty() }
            .toMap()

    override fun executeTurn(turn: Turn): GameState {
        val selectedPiece = pieces.getValue(turn.from)
        turn.execute(pieces)

        val state = getGameState(pieces, selectedPiece.color.other())

        saveTurnHistory(selectedPiece, turn, state)

        return state
    }

    private fun getGameState(pieces: Map<Point, Piece>, kingColor: PieceColor): GameState {
        val isKingUnderAttack = UTILS.isKingUnderAttack(pieces, kingColor)

        val hasAvailableTurns = pieces
            .filter { it.value.color == kingColor }
            .map { getTurnsForPiece(it.key) }
            .flatten()
            .isNotEmpty()

        return when {
            isKingUnderAttack && hasAvailableTurns -> GameState.CHECK
            isKingUnderAttack && !hasAvailableTurns -> GameState.MATE // король под ударом, ходов нет - мат
            !isKingUnderAttack && !hasAvailableTurns -> GameState.STALEMATE // король не под ударом, но ходов нет - пат
            else -> GameState.IN_PROGRESS
        }
    }

    private fun saveTurnHistory(
        selectedPiece: Piece,
        turn: Turn,
        state: GameState
    ) {
        val turnNotation = "$turn${state.notation}"

        if (selectedPiece.color == PieceColor.WHITE) {
            // белые всегда ходят первыми, поэтому для записи их хода всегда создаём новый элемент в истории
            history.add(
                HistoryItem(
                    turnNumber = history.size + 1,
                    white = turnNotation
                )
            )
        } else {
            // чёрные всегда ходят вторыми, поэтому достаём из истории последний элемент и дополняем его
            val last = history.removeLast()
            history.add(last.copy(black = turnNotation))
        }
    }

    private companion object {
        val UTILS = BoardUtils()
    }
}