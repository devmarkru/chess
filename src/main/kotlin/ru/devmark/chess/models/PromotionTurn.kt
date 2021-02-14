package ru.devmark.chess.models

class PromotionTurn(
    override val from: Point,
    override val to: Point,
    override val enemyPiece: Piece?,
    val toType: PieceType
) : Turn {
    override fun execute(pieces: MutableMap<Point, Piece>) {
        val piece = pieces.getValue(from)
        pieces.remove(from)
        pieces[to] = Piece(
            type = toType,
            color = piece.color
        )
    }

    override fun revert(pieces: MutableMap<Point, Piece>) {
        val piece = pieces.getValue(to)
        pieces.remove(to)
        pieces[from] = Piece(
            type = PieceType.PAWN,
            color = piece.color
        )
        enemyPiece?.let {
            pieces[to] = Piece(
                type = it.type,
                color = it.color
            )
        }
    }
}