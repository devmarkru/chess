package ru.devmark.chess

import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.PieceType
import ru.devmark.chess.models.Point

class BoardReader {

    fun readFileFromResource(fileName: String): Map<Point, Piece> {
        val lines = this.javaClass.getResourceAsStream(fileName)
            .bufferedReader()
            .use { it.readLines() }
        if (lines.size < 8) {
            throw RuntimeException("File contains less than 8 lines")
        }
        val pieces = mutableMapOf<Point, Piece>()
        lines.forEachIndexed { y, line ->
            if (line.length < 8) {
                throw RuntimeException("Line ${y + 1} contains less than 8 chars")
            }
            line.toCharArray().forEachIndexed { x, char ->
                when (char) {
                    'P' -> Piece(PieceType.PAWN, PieceColor.WHITE)
                    'p' -> Piece(PieceType.PAWN, PieceColor.BLACK)
                    'R' -> Piece(PieceType.ROOK, PieceColor.WHITE)
                    'r' -> Piece(PieceType.ROOK, PieceColor.BLACK)
                    'N' -> Piece(PieceType.KNIGHT, PieceColor.WHITE)
                    'n' -> Piece(PieceType.KNIGHT, PieceColor.BLACK)
                    'B' -> Piece(PieceType.BISHOP, PieceColor.WHITE)
                    'b' -> Piece(PieceType.BISHOP, PieceColor.BLACK)
                    'Q' -> Piece(PieceType.QUEEN, PieceColor.WHITE)
                    'q' -> Piece(PieceType.QUEEN, PieceColor.BLACK)
                    'K' -> Piece(PieceType.KING, PieceColor.WHITE)
                    'k' -> Piece(PieceType.KING, PieceColor.BLACK)
                    else -> null
                }
                    ?.let { piece ->
                        val point = Point(x, 7 - y)
                        pieces[point] = piece
                    }
            }
        }
        return pieces
    }
}