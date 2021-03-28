package ru.devmark.chess.engine

import ru.devmark.chess.models.GameState
import ru.devmark.chess.models.HistoryItem
import ru.devmark.chess.models.Piece
import ru.devmark.chess.models.PieceColor
import ru.devmark.chess.models.Point
import ru.devmark.chess.models.Turn

interface Board {

    fun load(initialPieces: Map<Point, Piece>)

    fun getPieces(): Map<Point, Piece>

    fun getHistory(): List<HistoryItem>

    fun getTurnsForPiece(position: Point): Set<Turn>

    fun getTurnsForColor(color: PieceColor): Map<Point, Set<Turn>>

    fun executeTurn(turn: Turn): GameState
}