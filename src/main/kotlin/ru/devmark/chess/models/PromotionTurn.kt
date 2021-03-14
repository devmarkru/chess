package ru.devmark.chess.models

class PromotionTurn(
    override val sourcePiece: Piece,
    override val from: Point,
    override val to: Point,
    override val enemyPiece: Piece?,
    val toType: PieceType
) : Turn {
    override fun execute(pieces: MutableMap<Point, Piece>) {
        pieces.remove(from)
        pieces[to] = sourcePiece.copy(type = toType)
    }

    override fun revert(pieces: MutableMap<Point, Piece>) {
        pieces.remove(to)
        pieces[from] = sourcePiece
        enemyPiece?.let { pieces[to] = it }
    }

    override fun toString(): String =
        "${sourcePiece.type.notation}${from.notation()}${enemyPiece?.type?.let { "x" } ?: "-"}${to.notation()}${toType.notation}"
}