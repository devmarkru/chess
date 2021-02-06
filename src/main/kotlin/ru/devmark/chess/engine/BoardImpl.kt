package ru.devmark.chess.engine

import ru.devmark.chess.models.GameState
import ru.devmark.chess.models.HistoryItem
import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.PieceType
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Piece

class BoardImpl : Board {

    private val pieces: MutableMap<Point, Piece> = initBoard()
    private val history = mutableListOf<HistoryItem>()

    override fun getPieces(): Map<Point, Piece> = pieces
    override fun getHistory(): List<HistoryItem> = history

    /**
     * для указанной фигуры возвращает все доступные для неё клетки
     * если какой-то ход ставит короля под удар, то ход исключается из результата
     */
    override fun getSpacesForTurn(piece: Piece): Set<Point> {
        val spacesForTurn = utils.getSpacesForTurn(piece, pieces)
        val result = mutableSetOf<Point>()
        val originalPosition = piece.position

        // нужно исключить каждый ход, в результате которого король окажется под ударом
        val piecesCopy = HashMap(pieces)
        var lastPosition = piece.position
        spacesForTurn.forEach { space ->
            piecesCopy.remove(lastPosition)
            piece.position = space
            piecesCopy[space] = piece
            if (!utils.isKingUnderAttack(piecesCopy, piece.color)) {
                result += space
            }
            lastPosition = piece.position
        }

        piece.position = originalPosition
        return result
    }

    override fun movePiece(from: Point, to: Point): GameState {
        val defeatedPiece = pieces[to]
        val selectedPiece = pieces.getValue(from)
        pieces.remove(from)
        pieces[to] = selectedPiece
        selectedPiece.position = to
        selectedPiece.wasMove = true

        if (selectedPiece.type == PieceType.PAWN) { // todo порефачить превращение пешки и поправить историю для этого кейса
            if (selectedPiece.position.y == 0 || selectedPiece.position.y == 7) {
                pieces[selectedPiece.position] = Piece(
                    PieceType.QUEEN,
                    selectedPiece.position,
                    selectedPiece.color,
                    true
                )
            }
        }

        val state = getGameState(pieces, selectedPiece.color.toggle())

        saveTurnHistory(selectedPiece, from, defeatedPiece, to, state)

        return state
    }

    private fun getGameState(pieces: Map<Point, Piece>, kingColor: PieceColor): GameState {
        val isKingUnderAttack = utils.isKingUnderAttack(pieces, kingColor)

        val hasAvailableTurns = pieces
            .filter { it.value.color == kingColor }
            .map { getSpacesForTurn(it.value).filter { space -> space != it.value.position } }
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
        from: Point,
        defeatedPiece: Piece?,
        to: Point,
        state: GameState
    ) {
        val turnNotation =
            "${selectedPiece.type.notation}${from.notation()}${defeatedPiece?.let { "x" } ?: "-"}${to.notation()}${state.notation}"

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
        mutableListOf(
            Piece(PieceType.PAWN, Point(0, 1), PieceColor.WHITE),
            Piece(PieceType.PAWN, Point(1, 1), PieceColor.WHITE),
            Piece(PieceType.PAWN, Point(2, 1), PieceColor.WHITE),
            Piece(PieceType.PAWN, Point(3, 1), PieceColor.WHITE),
            Piece(PieceType.PAWN, Point(4, 1), PieceColor.WHITE),
            Piece(PieceType.PAWN, Point(5, 1), PieceColor.WHITE),
            Piece(PieceType.PAWN, Point(6, 1), PieceColor.WHITE),
            Piece(PieceType.PAWN, Point(7, 1), PieceColor.WHITE),

            Piece(PieceType.PAWN, Point(0, 6), PieceColor.BLACK),
            Piece(PieceType.PAWN, Point(1, 6), PieceColor.BLACK),
            Piece(PieceType.PAWN, Point(2, 6), PieceColor.BLACK),
            Piece(PieceType.PAWN, Point(3, 6), PieceColor.BLACK),
            Piece(PieceType.PAWN, Point(4, 6), PieceColor.BLACK),
            Piece(PieceType.PAWN, Point(5, 6), PieceColor.BLACK),
            Piece(PieceType.PAWN, Point(6, 6), PieceColor.BLACK),
            Piece(PieceType.PAWN, Point(7, 6), PieceColor.BLACK),

            Piece(PieceType.ROOK, Point(0, 0), PieceColor.WHITE),
            Piece(PieceType.ROOK, Point(7, 0), PieceColor.WHITE),
            Piece(PieceType.ROOK, Point(0, 7), PieceColor.BLACK),
            Piece(PieceType.ROOK, Point(7, 7), PieceColor.BLACK),

            Piece(PieceType.BISHOP, Point(2, 0), PieceColor.WHITE),
            Piece(PieceType.BISHOP, Point(5, 0), PieceColor.WHITE),
            Piece(PieceType.BISHOP, Point(2, 7), PieceColor.BLACK),
            Piece(PieceType.BISHOP, Point(5, 7), PieceColor.BLACK),

            Piece(PieceType.QUEEN, Point(3, 7), PieceColor.BLACK),
            Piece(PieceType.QUEEN, Point(3, 0), PieceColor.WHITE),

            Piece(PieceType.KING, Point(4, 7), PieceColor.BLACK),
            Piece(PieceType.KING, Point(4, 0), PieceColor.WHITE),

            Piece(PieceType.KNIGHT, Point(1, 0), PieceColor.WHITE),
            Piece(PieceType.KNIGHT, Point(6, 0), PieceColor.WHITE),
            Piece(PieceType.KNIGHT, Point(1, 7), PieceColor.BLACK),
            Piece(PieceType.KNIGHT, Point(6, 7), PieceColor.BLACK)
        )
            .associateBy { it.position }
            .toMutableMap()

    private companion object {
        const val LETTERS = "abcdefgh"

        val utils = BoardUtils()
    }
}