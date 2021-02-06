package ru.devmark.chess.engine

import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.PieceType
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Piece
import ru.devmark.chess.resolvers.DiagonalTurnResolver
import ru.devmark.chess.resolvers.KingTurnResolver
import ru.devmark.chess.resolvers.KnightTurnResolver
import ru.devmark.chess.resolvers.PawnTurnResolver
import ru.devmark.chess.resolvers.QueenTurnResolver
import ru.devmark.chess.resolvers.RectangularTurnResolver

class BoardUtils {

    fun getSpacesUnderAttack(pieces: Map<Point, Piece>): Map<PieceColor, Set<Point>> {
        val spacesUnderAttack = mutableMapOf(
            PieceColor.WHITE to mutableSetOf<Point>(),
            PieceColor.BLACK to mutableSetOf()
        )
        pieces.values.forEach {
            spacesUnderAttack.getValue(it.color).addAll(getSpacesForTurn(it, pieces))
        }
        return spacesUnderAttack
    }

    fun isKingUnderAttack(pieces: Map<Point, Piece>, kingColor: PieceColor): Boolean {
        val spacesUnderAttack = getSpacesUnderAttack(pieces).getValue(kingColor.toggle())

        return pieces.entries
            .filter { it.value.type == PieceType.KING && it.value.color == kingColor }
            .any { it.key in spacesUnderAttack }
    }

    fun getSpacesForTurn(piece: Piece, pieces: Map<Point, Piece>): Set<Point> =
        when (piece.type) {
            PieceType.PAWN -> PAWN_RESOLVER
            PieceType.KNIGHT -> KNIGHT_RESOLVER
            PieceType.BISHOP -> BISHOP_RESOLVER
            PieceType.ROOK -> ROOK_RESOLVER
            PieceType.QUEEN -> QUEEN_RESOLVER
            PieceType.KING -> KING_RESOLVER
        }.getSpacesForTurn(piece, pieces)

    private companion object {
        val PAWN_RESOLVER = PawnTurnResolver()
        val KNIGHT_RESOLVER = KnightTurnResolver()
        val BISHOP_RESOLVER = DiagonalTurnResolver(8)
        val ROOK_RESOLVER = RectangularTurnResolver(8)
        val QUEEN_RESOLVER = QueenTurnResolver()
        val KING_RESOLVER = KingTurnResolver()
    }
}