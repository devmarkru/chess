package ru.devmark.chess.engine

import ru.devmark.chess.models.GameState
import ru.devmark.chess.models.HistoryItem
import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.PieceType
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.PromotionTurn
import ru.devmark.chess.models.Turn

class BoardImpl : Board {

    private val pieces: MutableMap<Point, Piece> = initBoard()
    // todo в истории хранить объекты Turn
    private val history = mutableListOf<HistoryItem>()

    override fun getPieces(): Map<Point, Piece> = pieces
    override fun getHistory(): List<HistoryItem> = history

    /**
     * для указанной фигуры возвращает все доступные для неё клетки
     * если какой-то ход ставит короля под удар, то ход исключается из результата
     */
    override fun getTurnsForPiece(position: Point): Set<Turn> {
        val turns = utils.getTurnsForPiece(position, pieces)
        val result = mutableSetOf<Turn>()
        val kingColor = pieces.getValue(position).color

        // нужно исключить каждый ход фигуры, который ставит её короля под удар
        turns.forEach { turn ->
            turn.execute(pieces)
            if (!utils.isKingUnderAttack(pieces, kingColor)) {
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
        val isKingUnderAttack = utils.isKingUnderAttack(pieces, kingColor)

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
        val from = turn.from
        val to = turn.to
        val enemyPieceType = turn.enemyPiece
        val toType = if (turn is PromotionTurn) {
            turn.toType
        } else null
        // todo добавить в Turn тип исходной фигуры и переопределить toString()
        val turnNotation =
            "${selectedPiece.type.notation}${from.notation()}${enemyPieceType?.let { "x" } ?: "-"}${to.notation()}${toType?.notation ?: ""}${state.notation}"

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

    private fun Point.notation(): String = "${LETTERS[this.x]}${this.y + 1}"

    private fun initBoard(): MutableMap<Point, Piece> =
        mutableMapOf(
            Point(0, 1) to Piece(PieceType.PAWN, PieceColor.WHITE),
            Point(1, 1) to Piece(PieceType.PAWN, PieceColor.WHITE),
            Point(2, 1) to Piece(PieceType.PAWN, PieceColor.WHITE),
            Point(3, 1) to Piece(PieceType.PAWN, PieceColor.WHITE),
            Point(4, 1) to Piece(PieceType.PAWN, PieceColor.WHITE),
            Point(5, 1) to Piece(PieceType.PAWN, PieceColor.WHITE),
            Point(6, 1) to Piece(PieceType.PAWN, PieceColor.WHITE),
            Point(7, 1) to Piece(PieceType.PAWN, PieceColor.WHITE),

            Point(0, 6) to Piece(PieceType.PAWN, PieceColor.BLACK),
            Point(1, 6) to Piece(PieceType.PAWN, PieceColor.BLACK),
            Point(2, 6) to Piece(PieceType.PAWN, PieceColor.BLACK),
            Point(3, 6) to Piece(PieceType.PAWN, PieceColor.BLACK),
            Point(4, 6) to Piece(PieceType.PAWN, PieceColor.BLACK),
            Point(5, 6) to Piece(PieceType.PAWN, PieceColor.BLACK),
            Point(6, 6) to Piece(PieceType.PAWN, PieceColor.BLACK),
            Point(7, 6) to Piece(PieceType.PAWN, PieceColor.BLACK),

            Point(0, 0) to Piece(PieceType.ROOK, PieceColor.WHITE),
            Point(7, 0) to Piece(PieceType.ROOK, PieceColor.WHITE),
            Point(0, 7) to Piece(PieceType.ROOK, PieceColor.BLACK),
            Point(7, 7) to Piece(PieceType.ROOK, PieceColor.BLACK),

            Point(2, 0) to Piece(PieceType.BISHOP, PieceColor.WHITE),
            Point(5, 0) to Piece(PieceType.BISHOP, PieceColor.WHITE),
            Point(2, 7) to Piece(PieceType.BISHOP, PieceColor.BLACK),
            Point(5, 7) to Piece(PieceType.BISHOP, PieceColor.BLACK),

            Point(3, 7) to Piece(PieceType.QUEEN, PieceColor.BLACK),
            Point(3, 0) to Piece(PieceType.QUEEN, PieceColor.WHITE),

            Point(4, 7) to Piece(PieceType.KING, PieceColor.BLACK),
            Point(4, 0) to Piece(PieceType.KING, PieceColor.WHITE),

            Point(1, 0) to Piece(PieceType.KNIGHT, PieceColor.WHITE),
            Point(6, 0) to Piece(PieceType.KNIGHT, PieceColor.WHITE),
            Point(1, 7) to Piece(PieceType.KNIGHT, PieceColor.BLACK),
            Point(6, 7) to Piece(PieceType.KNIGHT, PieceColor.BLACK)
        )

    private companion object {
        const val LETTERS = "abcdefgh"

        val utils = BoardUtils()
    }
}