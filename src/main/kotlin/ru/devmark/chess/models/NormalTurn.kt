package ru.devmark.chess.models

class NormalTurn(
    override val from: Point,
    override val to: Point,
    override val enemyPiece: Piece? = null
) : Turn {
    override fun execute(pieces: MutableMap<Point, Piece>) {
        val piece = pieces.getValue(from)
        pieces.remove(from)
        pieces[to] = Piece(
            type = piece.type,
            color = piece.color
        )
    }

    override fun revert(pieces: MutableMap<Point, Piece>) {
        val piece = pieces.getValue(to)
        pieces.remove(to)
        pieces[from] = Piece(
            type = piece.type,
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