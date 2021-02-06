package ru.devmark.chess.engine

import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.Point
import ru.devmark.chess.pieces.King
import ru.devmark.chess.pieces.Piece

class BoardUtils {

    fun getSpacesUnderAttack(pieces: Map<Point, Piece>): Map<PieceColor, Set<Point>> {
        val spacesUnderAttack = mutableMapOf(
            PieceColor.WHITE to mutableSetOf<Point>(),
            PieceColor.BLACK to mutableSetOf()
        )
        pieces.values.forEach {
            spacesUnderAttack.getValue(it.color).addAll(it.getSpacesForTurn(pieces))
        }
        return spacesUnderAttack
    }

    fun isKingUnderAttack(pieces: Map<Point, Piece>, kingColor: PieceColor): Boolean {
        val spacesUnderAttack = getSpacesUnderAttack(pieces).getValue(kingColor.toggle())

        return pieces.entries
            .filter { it.value is King && it.value.color == kingColor }
            .any { it.key in spacesUnderAttack }
    }
}