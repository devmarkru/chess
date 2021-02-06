package ru.devmark.chess.engine

import ru.devmark.chess.models.GameState
import ru.devmark.chess.models.HistoryItem
import ru.devmark.chess.models.Point
import ru.devmark.chess.pieces.Piece

interface Board {

    fun getPieces(): Map<Point, Piece>

    fun getHistory(): List<HistoryItem>

    fun getSpacesForTurn(piece: Piece): Set<Point>

    fun movePiece(from: Point, to: Point): GameState
}