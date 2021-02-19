package ru.devmark.chess.engine.impl

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
        val spacesUnderAttack = mutableMapOf<PieceColor, MutableSet<Point>>()
        pieces.entries.forEach { (position, piece) ->
            spacesUnderAttack.putIfAbsent(piece.color, mutableSetOf())
            spacesUnderAttack.getValue(piece.color).addAll(getTurnsForPiece(position, pieces).map { turn -> turn.to })
        }
        return spacesUnderAttack
    }

    fun isKingUnderAttack(pieces: Map<Point, Piece>, kingColor: PieceColor): Boolean {
        val spacesUnderAttack = getSpacesUnderAttack(pieces).getValue(kingColor.other())
        val king = pieces.entries
            .first { it.value.type == PieceType.KING && it.value.color == kingColor }
        return king.key in spacesUnderAttack
    }

    fun getTurnsForPiece(position: Point, pieces: Map<Point, Piece>): Set<Turn> {
        val piece = pieces.getValue(position)
        return when (piece.type) {
            PieceType.PAWN -> PAWN_GENERATOR
            PieceType.KNIGHT -> KNIGHT_GENERATOR
            PieceType.BISHOP -> BISHOP_GENERATOR
            PieceType.ROOK -> ROOK_GENERATOR
            PieceType.QUEEN -> QUEEN_GENERATOR
            PieceType.KING -> KING_GENERATOR
        }.getTurns(position, pieces)
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