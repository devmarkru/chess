package ru.devmark.chess.engine

import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.PieceType
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Turn
import ru.devmark.chess.resolvers.BishopTurnGenerator
import ru.devmark.chess.resolvers.KingTurnGenerator
import ru.devmark.chess.resolvers.KnightTurnGenerator
import ru.devmark.chess.resolvers.PawnTurnGenerator
import ru.devmark.chess.resolvers.QueenTurnGenerator
import ru.devmark.chess.resolvers.RookTurnGenerator

class BoardUtils {

    fun getSpacesUnderAttack(pieces: Map<Point, Piece>): Map<PieceColor, Set<Point>> {
        val spacesUnderAttack = mutableMapOf(
            PieceColor.WHITE to mutableSetOf<Point>(),
            PieceColor.BLACK to mutableSetOf()
        )
        pieces.values.forEach {
            spacesUnderAttack.getValue(it.color).addAll(getTurnsForPiece(it, pieces).map { turn -> turn.to })
        }
        return spacesUnderAttack
    }

    fun isKingUnderAttack(pieces: Map<Point, Piece>, kingColor: PieceColor): Boolean {
        val spacesUnderAttack = getSpacesUnderAttack(pieces).getValue(kingColor.other())
        val king = pieces.values
            .first { it.type == PieceType.KING && it.color == kingColor }
        return king.position in spacesUnderAttack
    }

    fun getTurnsForPiece(piece: Piece, pieces: Map<Point, Piece>): Set<Turn> =
        when (piece.type) {
            PieceType.PAWN -> PAWN_GENERATOR
            PieceType.KNIGHT -> KNIGHT_GENERATOR
            PieceType.BISHOP -> BISHOP_GENERATOR
            PieceType.ROOK -> ROOK_GENERATOR
            PieceType.QUEEN -> QUEEN_GENERATOR
            PieceType.KING -> KING_GENERATOR
        }.getTurns(piece, pieces)

    fun movePiece(pieces: MutableMap<Point, Piece>, from: Point, to: Point, promotionType: PieceType?) {
        val piece = pieces.getValue(from)
        pieces.remove(from)
        piece.position = to
        promotionType?.let { piece.type = promotionType }
        pieces[to] = piece
    }

    private companion object {
        val PAWN_GENERATOR = PawnTurnGenerator()
        val KNIGHT_GENERATOR = KnightTurnGenerator()
        val BISHOP_GENERATOR = BishopTurnGenerator()
        val ROOK_GENERATOR = RookTurnGenerator()
        val QUEEN_GENERATOR = QueenTurnGenerator()
        val KING_GENERATOR = KingTurnGenerator()
    }
}